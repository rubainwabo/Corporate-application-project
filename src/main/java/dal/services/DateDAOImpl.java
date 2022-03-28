package dal.services;

import dal.DalBackService;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DateDAOImpl implements DateDAO {

  @Inject
  private DalBackService myBackService;

  @Override
  public void addDate(int itemId) {
    try (PreparedStatement psDate = myBackService.getPreparedStatement(
        "insert into projet.dates (id_date,_date,item) values (DEFAULT,?,'" + itemId + "')")) {
      psDate.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
      psDate.executeUpdate();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }
}