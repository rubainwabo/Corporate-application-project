package dal.services;

import buiseness.domain.UserDTO;

public interface UserDAO {

  /**
   * allows you to retrieve a user against a username.
   *
   * @param username the user username
   * @return the found user or null if it does not exist
   */
  UserDTO getOneByUsername(String username);

}
