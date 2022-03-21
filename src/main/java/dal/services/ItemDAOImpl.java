package dal.services;

import buiseness.domain.dto.ItemDTO;
import buiseness.factory.BizFactory;
import dal.DalBackService;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemDAOImpl implements ItemDAO {

  @Inject
  private DalBackService myBackService;

  @Inject
  private BizFactory myBizFactoryService;

  @Override
  public int addItem(ItemDTO item, int offerorId) {
    // get ps to insert item
    try (PreparedStatement ps = myBackService.getPreparedStatement(
        "insert into projet.items (id_item,description,url_picture,state,offeror,item_type,time_slot) VALUES (DEFAULT,?,?,?,?,?,?)")) {
      // ps to find lastId insere
      ps.setString(1, item.getDescription());
      ps.setString(2, item.getUrlPicture());
      ps.setString(3, "offered");
      ps.setInt(4, offerorId);
      ps.setString(6, item.getTimeSlot());

      try (PreparedStatement psIdItem = myBackService.getPreparedStatement(
          "select id_item_type from projet.item_type where \"item_type_name\" = ? ")) {
        psIdItem.setString(1, item.getItemtype());
        try (ResultSet rsIdItem = psIdItem.executeQuery()) {
          if (rsIdItem.next()) {
            ps.setInt(5, rsIdItem.getInt(1));
          }
        }
      }
      // need to return the id insered ( for the moment return 1 if ok)
      return ps.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return -1;
  }

  @Override
  public ItemDTO getOneById(int id) {
    try (PreparedStatement ps = myBackService.getPreparedStatement(
        "select id_item,item_type,description,url_picture,offeror,time_slot,state from projet.items where id_item=?")) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        ItemDTO item = myBizFactoryService.getItem();
        if (!rs.next()) {
          return null;
        }
        // PS to get the string of the item_type from the id of the previous PS
        try (PreparedStatement psTypeString = myBackService.getPreparedStatement(
            "Select item_type_name from projet.item_type where id_item_type = " + rs.getInt(2))) {
          try (ResultSet rsTypeString = psTypeString.executeQuery()) {
            if (!rsTypeString.next()) {
              return null;
            }
            var itemTypeAsString = rsTypeString.getString(1);
            item.setId(rs.getInt(1));
            item.setItemtype(itemTypeAsString);
            item.setDescription(rs.getString(3));
            item.setUrlPicture(rs.getString(4));

            // PS to get the string of the offeror (lastName + firstName) from the id of the previous PS
            try (PreparedStatement psOfferorAsString = myBackService.getPreparedStatement(
                "Select last_name,first_name from projet.members where user_id = " + rs.getInt(
                    5))) {
              try (ResultSet rsOfferorAsString = psOfferorAsString.executeQuery()) {
                if (!rsOfferorAsString.next()) {
                  return null;
                }
                String offeror =
                    rsOfferorAsString.getString(1) + " " + rsOfferorAsString.getString(2);
                item.setOfferor(offeror);
                item.setTimeSlot(rs.getString(6));
                item.setState(rs.getString(7));

                try (PreparedStatement psNbrPlpInterest = myBackService.getPreparedStatement(
                    "Select count(member) from projet.interests where item = " + rs.getInt(1))) {
                  try (ResultSet rsNbrPlpInterest = psNbrPlpInterest.executeQuery()) {
                    if (!rsNbrPlpInterest.next()) {
                      return null;
                    }
                    item.setNumberOfPeopleInterested(rsNbrPlpInterest.getInt(1));
                    return item;
                  }
                }
              }
            }
          }
        }
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
      return null;
    }
  }
}