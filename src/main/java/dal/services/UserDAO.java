package dal.services;

import buiseness.domain.dto.UserDTO;
import java.util.List;

public interface UserDAO {

  /**
   * allows you to retrieve a user against a username.
   *
   * @param username the user username
   * @return the found user or null if it does not exist
   */
  UserDTO getOneByUsername(String username);

  /**
   * allows to retrieve all the users of the db with the state in the parameters.
   *
   * @return returns a list of users with the state in parameter
   */
  List<UserDTO> getAllUserByState(String state);

  UserDTO getOneById(int id);

  String getPhoneNumber(int userId);

}
