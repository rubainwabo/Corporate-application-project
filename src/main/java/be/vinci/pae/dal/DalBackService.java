package be.vinci.pae.dal;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface DalBackService {

  /**
   * retrieves a prepare statement based on the query passed in parameter.
   *
   * @param query the query we want to call on the database
   * @return a prepare statement based on the query passed in parameter
   * @throws SQLException when an sql exception occurs
   */
  PreparedStatement getPreparedStatement(String query) throws SQLException;

  /**
   * retrieves a prepare statement based on the query passed in parameter.
   *
   * @param query      the query we want to call on the database
   * @param idReturned the id of the statement
   * @return a prepare statement based on the query passed in parameter
   * @throws SQLException when an sql exception occurs
   */
  PreparedStatement getPreparedStatementWithId(String query, int idReturned) throws SQLException;

}
