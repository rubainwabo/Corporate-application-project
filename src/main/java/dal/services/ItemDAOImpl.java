package dal.services;

import buiseness.domain.dto.ItemDTO;
import dal.DalBackService;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;

public class ItemDAOImpl implements ItemDAO {

  @Inject
  DalBackService myBackService;

  public int getItemType(String type) {
    return 0;
  }

  @Override
  public int addItem(ItemDTO item, int offerorId) {
    // get ps to insert item
    try (PreparedStatement ps = myBackService.getPreparedStatement(
        "insert into projet.items (id_item,description,url_picture,state,offeror,item_type,time_slot) VALUES (DEFAULT,?,?,?,?,?,?)")) {
      // ps to find lastId insere

      ps.setString(1, item.getDescription());
      ps.setString(2, item.getUrlPicture());
      ps.setString(3, item.getState());
      ps.setInt(4, offerorId);
      ps.setString(6, LocalDateTime.now().toString());

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
}