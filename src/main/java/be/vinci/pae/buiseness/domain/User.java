package be.vinci.pae.buiseness.domain;

import be.vinci.pae.buiseness.dto.UserDTO;

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
   * will check that the state passed in parameter is denied.
   *
   * @return true if the state is denied
   */
  boolean isDenied();

  /**
   * will check that the state passed in parameter is waiting.
   *
   * @return true if the state is waiting
   */
  boolean isWaiting();

  boolean isAdmin();


}
