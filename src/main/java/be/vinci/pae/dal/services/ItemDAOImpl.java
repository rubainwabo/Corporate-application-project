package be.vinci.pae.dal.services;

import be.vinci.pae.buiseness.dto.ItemDTO;
import be.vinci.pae.buiseness.factory.BizFactory;
import be.vinci.pae.dal.DalBackService;
import be.vinci.pae.utils.exception.ConflictException;
import be.vinci.pae.utils.exception.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ItemDAOImpl implements ItemDAO {

  @Inject
  private DalBackService myBackService;

  @Inject
  private BizFactory myBizFactoryService;


  @Override
  public List<ItemDTO> getFiltered(String filter, String input) {
    String add;
    switch (filter) {
      default:
        add = "";
        break;
      case "type":
        add = "and it.item_type_name LIKE " + "'" + input + "%'";
        break;
      case "name":
        add = "and i.offeror in (SELECT m.user_id FROM projet.members m "
            + "WHERE m.last_name LIKE " + "'" + input + "%')";
        break;
      case "state":
        add = "and i.item_condition LIKE " + "'" + input + "%'";
        break;
      case "date":
        String[] date = input.split("-");
        add = "and i.id_item in (SELECT d.item FROM projet.dates d "
            + "WHERE TO_TIMESTAMP('" + date[0] + "', 'YYYY/MM/DD') "
            + "<= d._date and TO_TIMESTAMP('"
            + date[1] + "', 'YYYY/MM/DD') >= d._date)";
        break;
    }

    String query = "select i.id_item, i.description, i.url_picture,rating, i.comment, "
        + "i.item_condition, i.time_slot, i.offeror, it.item_type_name, "
        + "i.recipient, i.number_of_people_interested, "
        + "i.version,max(d._date) as maxDate "
        + "from projet.items i, projet.item_type it, projet.dates d "
        + "where i.id_item=d.item and i.item_type=it.id_item_type "
        + add
        + " GROUP BY i.id_item, i.description, i.url_picture, "
        + "i.number_of_people_interested, it.item_type_name ORDER BY maxDate desc";
    return getItemDTOs(query, false);
  }

  @Override
  public int addItem(ItemDTO item, int offerorId) {

    try (PreparedStatement ps = myBackService.getPreparedStatementWithId(
        "insert into projet.items "
            + "(id_item,description,url_picture,item_condition,"
            + "offeror,item_type,time_slot,number_of_people_interested,version) "
            + "VALUES (DEFAULT,?,?,?,?,?,?,0,0)",
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
            + "i.offeror,i.time_slot,i.item_condition,i.number_of_people_interested,"
            + "m.last_name,m.first_name,m2.last_name,m2.first_name,i.recipient,"
            + "i.rating,i.version "
            + "from projet.items i LEFT JOIN projet.members m2 on i.recipient=m2.user_id,"
            + "projet.item_type t,projet.dates d,projet.members m "
            + "where i.id_item=? and i.item_type = "
            + "t.id_item_type and d.item=" + id
            + " and m.user_id = i.offeror")) {
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
        item.setOfferorId(rs.getInt(5));
        item.setTimeSlot(rs.getString(6));
        item.setItemCondition(rs.getString(7));
        item.setNumberOfPeopleInterested(rs.getInt(8));
        item.setOfferor(rs.getString(9) + " " + rs.getString(10));
        if (rs.getString(11) != null) {
          item.setRecipient(rs.getString(11) + " " + rs.getString(12));
        }
        item.setRecipientId(rs.getInt(13));
        item.setRating(rs.getInt(14));
        item.setVersion(rs.getInt(15));
        return item;
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new FatalException(e);
    }
  }

  @Override
  public void addInterest(int idItem, int interestUserId, boolean callMe,
      String phoneNumber, String availabilities, int version) {
    try (PreparedStatement ps = myBackService.getPreparedStatement(
        "insert into projet.interests (_date,member,item) VALUES(?,?,?)")) {
      ps.setString(1, availabilities);
      ps.setInt(2, interestUserId);
      ps.setInt(3, idItem);
      ps.executeUpdate();
      try (PreparedStatement psNbrPeopleInteresed = myBackService.getPreparedStatement(
          "update projet.items set number_of_people_interested = 1 + number_of_people_interested, "
              + "version =version+1 "
              + "where id_item = " + idItem + " "
              + "and version=" + version)) {
        if (psNbrPeopleInteresed.executeUpdate() == 0) {
          verifyVersion(idItem, version);
        }
      }
      try (PreparedStatement psNotif = myBackService.getPreparedStatement(
          "insert into projet.notifications (id_notification,is_viewed,text,person,item) "
              + "VALUES (default,false,?,?,?)")) {
        String interestUsrName = "";
        try (PreparedStatement psOfferor = myBackService.getPreparedStatement(
            "select offeror i,m.username from projet.items i,projet.members m"
                + " where id_item = " + idItem + " and m.user_id=" + interestUserId)) {
          try (ResultSet rsOfferor = psOfferor.executeQuery()) {
            if (!rsOfferor.next()) {
              throw new FatalException(
                  "probleme dans la recuperation de l'offreur ou de "
                      + "l'username de la personne interessé");
            }
            psNotif.setInt(2, rsOfferor.getInt(1));
            interestUsrName = rsOfferor.getString(2);
          }
          psNotif.setInt(3, idItem);
        }
        String phoneNumerStr =
            callMe && !phoneNumber.isBlank() ? ", vous pouvez la contacter via son numéro : "
                + phoneNumber + " " : "";

        psNotif.setString(1,
            interestUsrName + " est interessé par votre offre" + phoneNumerStr);
        psNotif.executeUpdate();
      }
    } catch (Exception e) {
      throw new FatalException(e);
    }
  }

  @Override
  public void changeItemCondition(int idItem, int userId, String condition, int version) {
    String query = "update projet.items set item_condition ='" + condition + "', "
        + "version = version+1";
    if (condition.equals("cancelled") || condition.equals("offered")) {
      query += ", recipient=null";
    }
    query += " where id_item = " + idItem + " AND version = " + version;
    try (PreparedStatement psChangeCondition = myBackService.getPreparedStatement(query)) {
      if (psChangeCondition.executeUpdate() == 0) {
        verifyVersion(idItem, 1);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  @Override
  public List<ItemDTO> getLastItemsOffered(int limit) {
    String limite = limit > 0 ? "LIMIT " + limit : "";

    String query = "select i.id_item, i.description, i.url_picture,rating, i.comment, "
        + "i.item_condition, i.time_slot, i.offeror, it.item_type_name, i.recipient, "
        + "i.number_of_people_interested, "
        + "i.version,max(d._date) as maxDate "
        + "from projet.items i, "
        + "projet.item_type it, projet.dates d "
        + "where (i.item_condition='offered' OR i.item_condition='Assigned') "
        + "and i.id_item=d.item and i.item_type=it.id_item_type "
        + "GROUP BY i.id_item, i.description, i.url_picture, i.number_of_people_interested, "
        + "it.item_type_name ORDER BY maxDate desc " + limite;

    return getItemDTOs(query, false);
  }

  @Override
  public List<ItemDTO> getMyItems(int id, String state, int type, boolean mine) {
    String query = "select i.id_item, i.description, i.url_picture,rating, i.comment, "
        + "i.item_condition, i.time_slot, i.offeror, it.item_type_name, "
        + "i.recipient, i.number_of_people_interested,i.version, "
        + "m.last_name,m.first_name,m2.last_name,m2.first_name, "
        + "max(d._date) as maxDate "
        + "from projet.items i LEFT JOIN projet.members m2 on i.recipient=m2.user_id, "
        + "projet.item_type it, projet.dates d, projet.members m "
        + "where i.item_type=it.id_item_type and i.id_item=d.item "
        + "and m.user_id = i.offeror "
        + (mine ? "and i.offeror=" : "and i.recipient=") + id + " and i.item_condition='" + state
        + "'";
    if (type != 0) {
      query = query + " and i.item_type=" + type + " ";
    }
    query += "GROUP BY i.id_item, i.description, i.url_picture, "
        + "i.number_of_people_interested,m.last_name,m.first_name,"
        + "m2.last_name,m2.first_name, "
        + "it.item_type_name ORDER BY maxDate desc ";

    return getItemDTOs(query, true);
  }

  private List<ItemDTO> getItemDTOs(String query, boolean withNames) {
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
          item.setRating(rs.getInt(4));

          item.setComment(rs.getString(5));
          item.setItemCondition(rs.getString(6));
          item.setTimeSlot(rs.getString(7));

          item.setOfferorId(rs.getInt(8));
          item.setItemtype(rs.getString(9));
          item.setRecipientId(rs.getInt(10));
          item.setNumberOfPeopleInterested(rs.getInt(11));
          item.setVersion(rs.getInt(12));
          if (withNames) {
            item.setOfferor(rs.getString(13) + " " + rs.getString(14));
            if (rs.getString(15) != null) {
              item.setRecipient(rs.getString(15) + " " + rs.getString(16));
            }
          }
          arrayItemDTO.add(item);
        }
      }
      return arrayItemDTO;
    } catch (Exception e) {
      throw new FatalException(e);
    }
  }

  @Override
  public void itemCollectedOrNot(ItemDTO item, boolean itemCollected, int version) {
    String query = itemCollected ? "Update projet.items set item_condition='gifted',"
        + "version=version+1 "
        + "where id_item=" + item.getId() + "and version=" + version :
        "Update projet.items set item_condition='offered'"
            + " where id_item=" + item.getId();

    int idRecipient = item.getRecipientId();
    try (PreparedStatement ps = myBackService.getPreparedStatement(
        query)) {
      if (ps.executeUpdate() == 0) {
        verifyVersion(item.getId(), version);
      }
      if (!itemCollected) {
        try (PreparedStatement psUpdtItmNotTkn = myBackService.getPreparedStatement(
            "update projet.members set nb_of_item_not_taken= nb_of_item_not_taken "
                + "+ 1 where user_id=" + idRecipient)) {
          psUpdtItmNotTkn.executeUpdate();
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public int addRecipient(int idItem, int idRecipient, int version) {
    try (PreparedStatement ps = myBackService.getPreparedStatement(""
        + "update projet.items set recipient=" + idRecipient
        + ", item_condition='Assigned', version=version+1 "
        + "WHERE id_item=" + idItem + " and version = " + version)) {
      if (ps.executeUpdate() == 0) {
        verifyVersion(idItem, version);
      }
      try (PreparedStatement psNotif = myBackService.getPreparedStatement(
          "INSERT INTO projet.notifications (id_notification,is_viewed,text,person,item)"
              + " VALUES (default,false,?,?,?)"
      )) {
        ItemDTO item = getOneById(idItem);
        psNotif.setString(1, item.getOfferor() + " vous a attribué un objet");
        psNotif.setInt(2, idRecipient);
        psNotif.setInt(3, idItem);
        return psNotif.executeUpdate();
      }
    } catch (Exception e) {
      throw new FatalException(e);
    }
  }

  @Override
  public List<ItemDTO> memberItemsByItemCondition(String itemCondition, int userId,
      boolean isOfferor) {
    String query = "select i.id_item,i.url_picture,it.item_type_name,i.description from"
        + " projet.items i,projet.item_type it "
        + "where it.id_item_type=i.item_type and i.item_condition='"
        + itemCondition + "' " + "and ";
    query += isOfferor ? "i.offeror= " + userId : "i.recipient=" + userId;
    try (PreparedStatement ps = myBackService.getPreparedStatement(query)) {
      ArrayList<ItemDTO> itemDTOS = new ArrayList<>();
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          ItemDTO itemDTO = myBizFactoryService.getItem();
          itemDTO.setId(rs.getInt(1));
          itemDTO.setUrlPicture(rs.getString(2));
          itemDTO.setItemtype(rs.getString(3));
          itemDTO.setDescription(rs.getString(4));
          itemDTOS.add(itemDTO);
        }
        return itemDTOS;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException(e);
    }
  }

  @Override
  public int updateItem(ItemDTO item) {
    try (PreparedStatement psUpdate = myBackService.getPreparedStatement(
        "UPDATE projet.items set description=?, "
            + "version = version+1, "
            + (item.getUrlPicture() != null ? "url_picture=?," : "")
            + " time_slot=? WHERE id_item=" + item.getId() + " and version=" + item.getVersion()
    )) {
      psUpdate.setString(1, item.getDescription());
      if (item.getUrlPicture() != null) {
        psUpdate.setString(2, item.getUrlPicture());
        psUpdate.setString(3, item.getTimeSlot());
      } else {
        psUpdate.setString(2, item.getTimeSlot());
      }
      if (psUpdate.executeUpdate() == 0) {
        verifyVersion(item.getId(), item.getVersion());
      }
      return item.getId();
    } catch (Exception e) {
      throw new FatalException(e);
    }
  }

  @Override
  public void rateItem(int itemId, int nbStars, String comment, int version) {
    try (PreparedStatement psUpdate = myBackService.getPreparedStatement(
        "UPDATE projet.items set rating=?, comment=?,version=version+1"
            + " WHERE id_item="
            + itemId + " and version=" + version
    )) {
      psUpdate.setInt(1, nbStars);
      psUpdate.setString(2, comment);
      if (psUpdate.executeUpdate() == 0) {
        verifyVersion(itemId, version);
      }

    } catch (Exception e) {
      throw new FatalException(e);
    }
  }

  @Override
  public void updateItemOfInvalidMember(int memberId) {
    // case where we are an invalid offeror
    updateItemOfInvalidMemberWithCondition(true, memberId);
    // case we are an invalid recipient
    updateItemOfInvalidMemberWithCondition(false, memberId);
  }

  private void updateItemConditionOfInvalidOfferor(int offerorId, String itemCondition) {
    try (PreparedStatement ps = myBackService.getPreparedStatement(
        "update projet.items set item_condition ='" + itemCondition
            + "' where offeror = " + offerorId + " "
            + "and item_condition='Assigned'")) {
      ps.executeUpdate();
    } catch (Exception e) {
      throw new FatalException(e);
    }
  }

  private void updateItemOfInvalidMemberWithCondition(boolean isOfferor, int memberId) {
    var items = isOfferor ? getMyItems(memberId, "Assigned", 0, true)
        : getMyItems(memberId, "Assigned", 0, false);
    if (items.size() > 0) {
      String itemCondition = isOfferor ? "invalid offeror" : "invalid recipient";
      String query = "insert into projet.notifications "
          + "(id_notification,is_viewed,text,person,item)"
          + " VALUES (default,false,?,?,?)";
      for (ItemDTO item : items) {
        try (PreparedStatement ps = myBackService.getPreparedStatement(query)) {
          ps.setString(1, (isOfferor ? item.getOfferor() : item.getRecipient())
              + " ne sera pas disponible pour votre rendez-vous");
          ps.setInt(2, isOfferor ? item.getRecipientId() : item.getOfferorId());
          ps.setInt(3, item.getId());
          ps.executeUpdate();
          if (!isOfferor) {
            changeItemCondition(item.getId(), item.getOfferorId(), itemCondition,
                item.getVersion());
          }
        } catch (Exception e) {
          throw new FatalException(e);
        }
      }
      if (isOfferor) {
        updateItemConditionOfInvalidOfferor(memberId, itemCondition);
      }
    }
  }


  private void verifyVersion(int itemId, int version) {

    String queryVersion = "SELECT version FROM projet.items"
        + " WHERE  id_item  = " + itemId;

    try (PreparedStatement psVersion = myBackService.getPreparedStatement(
        queryVersion)) {
      try (ResultSet verifVersion = psVersion.executeQuery()) {

        if (verifVersion.next() && verifVersion.getInt(1) != version) {
          throw new ConflictException("accès concurrent, veuillez réessayer plus tard");
        }

      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
