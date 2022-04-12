package dal.services;

import buiseness.dto.ItemDTO;
import buiseness.factory.BizFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dal.DalBackService;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import utils.exception.FatalException;

public class ItemDAOImpl implements ItemDAO {

  @Inject
  private DalBackService myBackService;

  @Inject
  private BizFactory myBizFactoryService;

  @Override
  public int addItem(ItemDTO item, int offerorId) {

    try (PreparedStatement ps = myBackService.getPreparedStatementWithId(
        "insert into projet.items "
            + "(id_item,description,url_picture,item_condition,offeror,item_type,time_slot) "
            + "VALUES (DEFAULT,?,?,?,?,?,?)",
        Statement.RETURN_GENERATED_KEYS)) {
      // ps to find lastId insere
      ps.setString(1, item.getDescription());
      ps.setString(2, item.getUrlPicture());
      ps.setString(3, "offered");
      ps.setInt(4, offerorId);
      ps.setString(6, item.getTimeSlot());

      try (PreparedStatement psIdItem = myBackService.getPreparedStatement(
          "select id_item_type from projet.item_type where item_type_name = ? ")) {
        psIdItem.setString(1, item.getItemtype());
        try (ResultSet rsIdItem = psIdItem.executeQuery()) {
          if (!rsIdItem.next()) {
            throw new FatalException("Type d'item inexistant");
          }
          ps.setInt(5, rsIdItem.getInt(1));
        }
      }
      ps.executeUpdate();
      ResultSet rs = ps.getGeneratedKeys();
      int generatedKey = 0;
      if (rs.next()) {
        generatedKey = rs.getInt(1);
      }
      return generatedKey;
    } catch (Exception e) {
      throw new FatalException(e);
    }
  }

  @Override
  public ItemDTO getOneById(int id) {
    try (PreparedStatement ps = myBackService.getPreparedStatement(
        "select i.id_item,t.item_type_name,i.description,i.url_picture,"
            + "i.offeror,i.time_slot,i.item_condition,i.number_of_people_interested, max(d._date), "
            + "m.last_name,m.first_name "
            + "from projet.items i,projet.item_type t,projet.dates d,projet.members m "
            + "where i.id_item=? and i.item_type = "
            + "t.id_item_type and d.item=" + id
            + " and m.user_id = i.offeror GROUP BY i.id_item,t.item_type_name,m.last_name,m.first_name")) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        ItemDTO item = myBizFactoryService.getItem();
        if (!rs.next()) {
          throw new FatalException("Echec lors de l'exécution du rs getOneById");
        }
        item.setId(rs.getInt(1));
        item.setItemtype(rs.getString(2));
        item.setDescription(rs.getString(3));
        item.setUrlPicture(rs.getString(4));
        item.setTimeSlot(rs.getString(6));
        item.setItemCondition(rs.getString(7));
        item.setNumberOfPeopleInterested(rs.getInt(8));
        item.setLastDateOffered(rs.getTimestamp(9));
        item.setOfferor(rs.getString(10) + " " + rs.getString(11));
        return item;
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new FatalException(e);
    }
  }

  @Override
  public void addInterest(int idItem, ObjectNode objectNode, int interestUserId) {
    try (PreparedStatement ps = myBackService.getPreparedStatement(
        "insert into projet.interests (_date,member,item) VALUES(?,?,?)")) {
      ps.setString(1, objectNode.get("availabilities").asText());
      ps.setInt(2, interestUserId);
      ps.setInt(3, idItem);
      ps.executeUpdate();
      try (PreparedStatement psNbrPeopleInteresed = myBackService.getPreparedStatement(
          "update projet.items set number_of_people_interested = 1 + number_of_people_interested"
              + " where id_item = " + idItem)) {
        psNbrPeopleInteresed.executeUpdate();
      }
      try (PreparedStatement psNotif = myBackService.getPreparedStatement(
          "insert into projet.notifications (id_notification,is_viewed,text,person,item) "
              + "VALUES (default,false,?,?,?)")) {
        String urlPicture;
        try (PreparedStatement psOfferor = myBackService.getPreparedStatement(
            "select offeror,url_picture from projet.items where id_item = "
                + idItem)) {
          try (ResultSet rsOfferor = psOfferor.executeQuery()) {
            if (!rsOfferor.next()) {
              throw new FatalException(
                  "probleme dans la recuperation de l'offreur");
            }
            urlPicture = rsOfferor.getString(2);
            psNotif.setInt(2, rsOfferor.getInt(1));
          }
          psNotif.setInt(3, idItem);
        }
        String interestUsrName;
        try (PreparedStatement psInterestUserAsString = myBackService.getPreparedStatement(
            "Select username from projet.members where user_id = " + interestUserId)) {
          try (ResultSet rsInterestUserAsString = psInterestUserAsString.executeQuery()) {
            if (!rsInterestUserAsString.next()) {
              throw new FatalException(
                  "Echec de la requete : récupération du username de la personne interessé impossible");
            }
            interestUsrName = rsInterestUserAsString.getString(1);
          }
        }
        String phoneNumerStr =
            (objectNode.get("callMe").asBoolean() && !objectNode.get("phoneNumber").asText()
                .isBlank()) ? ", vous pouvez la contacter sur via son numéro : " + objectNode.get(
                    "phoneNumber")
                .asText() + " " : " ";

        psNotif.setString(1,
            interestUsrName + " est interessé par votre offre" + phoneNumerStr + urlPicture);
        psNotif.executeUpdate();
      }
    } catch (Exception e) {
      throw new FatalException(e);
    }
  }

  @Override
  public void cancelOffer(int idItem, int userId) {
    try (PreparedStatement psVerifyUser = myBackService.getPreparedStatement(
        "select offeror from projet.items where id_item = " + idItem)) {
      try (ResultSet rsVerifyUser = psVerifyUser.executeQuery()) {
        if (!rsVerifyUser.next()) {
          throw new FatalException("prblm cancelOffer user request");
        }
        if (rsVerifyUser.getInt(1) != userId) {
          throw new FatalException(
              "cet utilisateur n'est pas l'auteur de l'offre, il ne peut donc pas l'annuler");
        }
        try (PreparedStatement psCancelOffer = myBackService.getPreparedStatement(
            "update projet.items set item_condition = 'cancelled' where id_item = "
                + idItem)) {
          psCancelOffer.executeUpdate();
        }
      }
    } catch (Exception e) {
      throw new FatalException(e);
    }
  }

  @Override
  public List<ItemDTO> getLastItemsOffered(int limit) {
    String limite = limit > 0 ? "LIMIT " + limit : "";

    String query = "select i.id_item, i.description, i.url_picture,it.item_type_name, "
        + " i.number_of_people_interested,max(d._date) as maxDate from projet.items i,"
        + "projet.item_type it, projet.dates d "
        + "where i.item_condition='offered' and i.id_item=d.item and i.item_type=it.id_item_type"
        + " GROUP BY i.id_item, i.description, i.url_picture, i.number_of_people_interested, "
        + "it.item_type_name ORDER BY maxDate " + limite;

    return getItemDTOS(query);
  }

  @Override
  public List<ItemDTO> getAllOffered(int id) {
    String query =
        "select id_item, description, url_picture, "
            + "it.item_type_name,number_of_people_interested "
            + "from projet.items i, projet.item_type it "
            + "where offeror ='" + id + "' and i.item_type = it.id_item_type "
            + "and i.item_condition != 'cancelled'";
    return getItemDTOS(query);

  }

  private List<ItemDTO> getItemDTOS(String query) {
    ArrayList<ItemDTO> arrayItemDTO = new ArrayList<>();
    try (PreparedStatement ps = myBackService.getPreparedStatement(
        query
    )) {
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          ItemDTO item = myBizFactoryService.getItem();
          item.setId(rs.getInt(1));
          item.setDescription(rs.getString(2));
          item.setUrlPicture(rs.getString(3));
          item.setItemtype(rs.getString(4));
          item.setNumberOfPeopleInterested(rs.getInt(5));
          arrayItemDTO.add(item);
        }
      }

      return arrayItemDTO;
    } catch (Exception e) {
      throw new FatalException(e);
    }
  }

}