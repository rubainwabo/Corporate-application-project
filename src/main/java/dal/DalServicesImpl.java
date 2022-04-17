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

  public DalServicesImpl() {
    ds.setUrl(Config.getProperty("URL"));
    ds.setUsername(Config.getProperty("Username"));
    ds.setPassword(Config.getProperty("Password"));
    mapThreadConnection = new ThreadLocal<>();
  }

  @Override
  public PreparedStatement getPreparedStatement(String query) throws SQLException {
    if (mapThreadConnection.get() == null) {
      throw new FatalException("There is no connection available");
    }
    return mapThreadConnection.get().prepareStatement(query);
  }

  @Override
  public PreparedStatement getPreparedStatementWithId(String query, int idReturned)
      throws SQLException {
    if (mapThreadConnection.get() == null) {
      throw new FatalException("There is no connection available");
    }
    return mapThreadConnection.get().prepareStatement(query, idReturned);
  }

  @Override
  public void start() {
    try {
      if (mapThreadConnection.get() != null) {
        throw new FatalException("There is a connection started");
      }
      Connection con = ds.getConnection();
      con.setAutoCommit(false);
      mapThreadConnection.set(con);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public void commit() {
    try {
      Connection con = mapThreadConnection.get();
      con.commit();
      con.close();
      mapThreadConnection.set(null);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public void rollBack() {
    try {
      Connection con = mapThreadConnection.get();
      con.rollback();
      con.close();
      mapThreadConnection.set(null);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }
}
