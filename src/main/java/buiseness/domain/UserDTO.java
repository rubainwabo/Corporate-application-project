package buiseness.domain;

public interface UserDTO {

  /**
   * get the user's username.
   *
   * @return user username
   */
  String getUserName();

  /**
   * change the user's username by the one passed in parameter.
   *
   * @param username the new username
   */
  void setUserName(String username);

  /**
   * get the user's id.
   *
   * @return the user's id
   */
  int getId();

  /**
   * change the user's id by the one passed in parameter.
   *
   * @param id the new user's id
   */
  void setId(int id);

  /**
   * get the user's password.
   *
   * @return user password
   */
  String getPassword();

  /**
   * change the user's password by the one passed in parameter.
   *
   * @param password the new user's password
   */
  void setPassword(String password);

  /**
   * get the user's state.
   *
   * @return user state
   */
  String getState();

  /**
   * change the user's state by the one passed in parameter.
   *
   * @param state the new user's state
   */
  void setState(String state);

  /**
   * allows to retrieve the reason of refusal of a user.
   *
   * @param msg the reason of the connection refusal
   * @return the refusal message
   */
  String getReasonForConnectionRefusal(String msg);

  /**
   * change the user's reasonForConnectionRefusal by the one passed in parameter.
   *
   * @param reasonForConnectionRefusal the new user's reasonForConnectionRefusal
   */
  void setReasonForConnectionRefusal(String reasonForConnectionRefusal);

  /**
   * change the user's username by the one passed in parameter.
   *
   * @param username the new user's username
   */
  void setUsername(String username);
}
