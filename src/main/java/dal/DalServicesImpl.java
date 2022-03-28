package dal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;
import utils.Config;
import utils.exception.FatalException;

public class DalServicesImpl implements DalServices, DalBackService {

  private static BasicDataSource ds = new BasicDataSource();
  private static ThreadLocal<Connection> mapThreadConnection;

  static {
    ds.setUrl(Config.getProperty("URL"));
    ds.setUsername(Config.getProperty("Username"));
    ds.setPassword(Config.getProperty("Password"));
  }


  public DalServicesImpl() {
    mapThreadConnection = new ThreadLocal<>();
  }

  @Override
  public PreparedStatement getPreparedStatement(String query) throws SQLException {
    return mapThreadConnection.get().prepareStatement(query);
  }

  @Override
  public PreparedStatement getPreparedStatementWithId(String query, int idReturned)
      throws SQLException {
    return mapThreadConnection.get().prepareStatement(query, idReturned);
  }

  @Override
  public void start(boolean isTransaction) {
    try {
      Connection con = ds.getConnection();
      mapThreadConnection.set(con);
      if (isTransaction) {
        con.setAutoCommit(false);
      }
    } catch (Exception e) {
      throw new FatalException("Echec lors de la connexion à la db");
    }
  }

  @Override
  public void commit(boolean isTransaction) {
    try {
      Connection con = mapThreadConnection.get();
      if (isTransaction) {
        con.commit();
        con.setAutoCommit(true);
      }
      mapThreadConnection.remove();
    } catch (Exception e) {
      throw new FatalException("Echec lors de la connexion à la db");
    }
  }

  @Override
  public void rollBack() {
    try {
      Connection con = mapThreadConnection.get();
      con.rollback();
      mapThreadConnection.remove();
    } catch (Exception e) {
      throw new FatalException("Echec lors de la connexion à la db");
    }
  }
}