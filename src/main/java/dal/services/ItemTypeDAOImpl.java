package dal.services;

import dal.DalBackService;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;

public class ItemTypeDAOImpl implements ItemTypeDAO {

  @Inject
  private DalBackService myBackService;

  @Override
  public int addItemType(String itemTypeName) {
    try (PreparedStatement psItemType = myBackService.getPreparedStatement(
        "INSERT INTO projet.item_type VALUES (DEFAULT,?)")) {
      psItemType.setString(1, itemTypeName);
      return psItemType.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return -1;
  }
}
