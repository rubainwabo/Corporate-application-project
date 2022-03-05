package dal;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface DalServices {

  /**
   * retrieves a prepare statement based on the query passed in parameter.
   *
   * @param query the query we want to call on the database
   * @return a prepare statement based on the query passed in parameter
   * @throws SQLException
   */
  PreparedStatement getPreparedStatement(String query) throws SQLException;
}