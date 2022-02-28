package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import utils.Config;

public class DalServices {

  private Connection con = null;
  private String url = Config.getProperty("URL");
  private String pseudo = Config.getProperty("Pseudo");
  private String mdp = Config.getProperty("MotDePasse");

  public DalServices() throws SQLException {
    con = DriverManager.getConnection(url, pseudo, mdp);
  }

  public PreparedStatement getPreparedStatement(String query) throws SQLException {
    return con.prepareStatement(query);
  }
}
