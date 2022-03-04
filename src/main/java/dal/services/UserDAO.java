package dal.services;

import buiseness.domain.UserDTO;

public interface UserDAO {

  /**
   * allows you to retrieve a user against a nickname.
   *
   * @param username the user nickName
   * @return the found user or null if it does not exist
   */
  UserDTO getOne(String username);
}
