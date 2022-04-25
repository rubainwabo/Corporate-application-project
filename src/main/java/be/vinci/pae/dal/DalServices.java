package be.vinci.pae.dal;

public interface DalServices {

  /**
   * allow to start the transaction.
   */
  void start();

  /**
   * allow to end the transaction and commit it.
   */
  void commit();

  /**
   * if a exception is handled all previous actions will be cancelled.
   */
  void rollBack();
}
