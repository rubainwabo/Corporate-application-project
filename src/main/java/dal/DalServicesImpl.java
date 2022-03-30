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
  public PreparedStatement getPreparedStatement(String query) {
    try {
      return mapThreadConnection.get().prepareStatement(query);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public PreparedStatement getPreparedStatementWithId(String query, int idReturned) {
    try {
      return mapThreadConnection.get().prepareStatement(query, idReturned);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
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
      throw new FatalException(e);
    }
  }

  @Override
  public void commit(boolean isTransaction) {
    try {
      Connection con = mapThreadConnection.get();
      if (isTransaction) {
        con.commit();
      }
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
