package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import utils.Config;

public class DalServicesImpl implements DalServices {

  private Connection con = null;
  private String url = Config.getProperty("URL");
  private String pseudo = Config.getProperty("Pseudo");
  private String mdp = Config.getProperty("MotDePasse");

  public DalServicesImpl() throws SQLException {
    con = DriverManager.getConnection(url, pseudo, mdp);
  }
  
  @Override
  public PreparedStatement getPreparedStatement(String query) throws SQLException {
    return con.prepareStatement(query);
  }
}
