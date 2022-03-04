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

  /**
   * allows you to retrieve a user against an id.
   *
   * @param id the user id
   * @return the found user or null if it does not exist
   */
  UserDTO getOneById(int id);
}
