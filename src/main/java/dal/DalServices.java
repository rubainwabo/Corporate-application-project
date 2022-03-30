package dal;

import java.sql.SQLException;

public interface DalServices {

  /**
   * allow to start the transaction.
   */
  void start(boolean isTransaction) throws SQLException;

  /**
   * allow to end the transaction and commit it.
   */
  void commit(boolean isTransaction) throws SQLException;

  /**
   * if a exception is handled all previous actions will be cancelled.
   */
  void rollBack() throws SQLException;
}
