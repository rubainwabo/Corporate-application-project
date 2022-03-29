package dal;

public interface DalServices {

  /**
   * allow to start the transaction.
   */
  void start(boolean isTransaction);

  /**
   * allow to end the transaction and commit it.
   */
  void commit(boolean isTransaction);

  /**
   * if a exception is handled all previous actions will be cancelled.
   */
  void rollBack();
}
