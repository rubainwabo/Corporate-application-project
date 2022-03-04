package buiseness.domain;

public interface User extends UserDTO {

  /**
   * checks if the password entered by the user matches his hashed password.
   *
   * @param password input password
   * @return true if the password is checked, else false
   */
  boolean checkPassword(String password);

  /**
   * hash the password in params.
   *
   * @param password user password
   * @return the hashed password
   */
  String hashPassword(String password);

  /**
   * will check that the state passed in parameter is one of the possible states.
   *
   * @param state user state
   * @return true if the state exist
   */
  boolean checkState(String state);
}
