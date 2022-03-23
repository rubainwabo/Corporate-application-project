package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import utils.Config;

public class DalServicesImpl implements DalServices, DalBackService {

  private Connection con = null;
  private String url = Config.getProperty("URL");
  private String username = Config.getProperty("Username");
  private String password = Config.getProperty("Password");

  public DalServicesImpl() throws SQLException {
    con = DriverManager.getConnection(url, username, password);
  }

  @Override
  public PreparedStatement getPreparedStatement(String query) throws SQLException {
    return con.prepareStatement(query);
  }

  @Override
  public PreparedStatement getPreparedStatementWithId(String query, int idReturned)
      throws SQLException {
    return con.prepareStatement(query, idReturned);
  }

  @Override
  public void start() {
    try {
      con.setAutoCommit(false);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void commit() {
    try {
      con.commit();
      con.setAutoCommit(true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void rollBack() {
    try {
      con.rollback();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}