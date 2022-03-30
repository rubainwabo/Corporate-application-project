package dal.services;

import java.sql.SQLException;

public interface DateDAO {

  /**
   * add the date to the table dates.
   *
   * @param itemId the itemId
   */
  void addDate(int itemId) throws SQLException;
}
