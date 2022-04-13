package buiseness.ucc;

import buiseness.domain.User;
import buiseness.dto.UserDTO;
import java.util.List;
import utils.exception.InvalidStateException;
import utils.exception.PasswordOrUsernameException;
import utils.exception.ReasonForConnectionRefusalException;
import utils.exception.UserInvalidException;
import utils.exception.UserOnHoldException;


public interface UserUCC {

  /**
   * allows you to connect a user.
   *
   * @param username the username of the person trying to connect
   * @param password his password
   * @return an objectNode which will be composed of his token(s), id, username,rememberMe
   */

  UserDTO login(String username, String password, boolean rememberMe)
      throws PasswordOrUsernameException, ReasonForConnectionRefusalException,
      UserOnHoldException, UserInvalidException;
  
  boolean changeState(int id, String state, String refusalReason, boolean admin)
      throws InvalidStateException, InvalidStateException;

  User getOneById(int id);

  boolean checkAdmin(int id);

  boolean checkWaitingOrDenied(int id);

  /**
   * allows to retrieve all the users of the db with the state.
   *
   * @return a list of users with the state in params
   */
  List<UserDTO> getUsersByState(String state);

  /**
   * call tha dao to insert the phone number to the specific user id.
   *
   * @param userId      the userId
   * @param phoneNumber the phone number
   */
  void addPhoneNumber(int userId, String phoneNumber);

  int register(UserDTO user);
}
