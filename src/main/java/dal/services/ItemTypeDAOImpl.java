package dal.services;

import dal.DalBackService;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ItemTypeDAOImpl implements ItemTypeDAO {

  @Inject
  private DalBackService myBackService;

  @Override
  public int addItemType(String itemTypeName) {
    try (PreparedStatement psItemType = myBackService.getPreparedStatementWithId(
        "INSERT INTO projet.item_type VALUES (DEFAULT,?)", Statement.RETURN_GENERATED_KEYS)) {
      psItemType.setString(1, itemTypeName);
      psItemType.executeUpdate();
      ResultSet rs = psItemType.getGeneratedKeys();
      int generatedKey = 0;
      if (rs.next()) {
        generatedKey = rs.getInt(1);
      }
      return generatedKey;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return -1;
  }
}
