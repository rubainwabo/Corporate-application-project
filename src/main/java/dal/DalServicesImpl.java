package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import utils.Config;

public class DalServicesImpl implements DalServices {

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
}
