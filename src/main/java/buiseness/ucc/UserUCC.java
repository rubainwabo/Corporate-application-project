package buiseness.ucc;

import buiseness.domain.User;
import buiseness.dto.UserDTO;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import utils.exception.InvalidTokenException;
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

  ObjectNode login(String username, String password, boolean rememberMe)
      throws PasswordOrUsernameException, ReasonForConnectionRefusalException,
      UserOnHoldException, UserInvalidException;

  /**
   * verify the refresh token and create 2 token (1 refresh and 1 access).
   *
   * @param token token of the request
   * @return an acess and refresh token
   */
  ObjectNode refreshToken(String token) throws InvalidTokenException;

  boolean changeState(int id, String state, String refusalReason, boolean admin);

  /**
   * updates the profile of an user.
   * @return true if correctly changed.
   */
  boolean updateProfile(int id, String username,String firstName, String lastName,
      String street, int number, int postcode, String box, String city, String phone);

  /**
   * updates the password of an user.
   * @param id id of the user we want to change password.
   * @param password new password.
   * @return true if changed.
   */
  boolean updatePassword(int id,String password);

  User getOneById(int id);

  /**
   * checks if user is admin.
   * @param id id of the user we want to check
   * @return true if admin.
   */
  boolean checkAdmin(int id);

  /**
   * checks if user is waiting or denied.
   * @param id id of the user we want to check
   * @return true if waiting or denied.
   */
  boolean checkWaitingOrDenied(int id);

  /**
   * allows to retrieve all the users of the db with the state.
   *
   * @return a list of users with the state in params
   */
  List<UserDTO> getUsersByState(String state);

  /**
   * retrives the phone number of the user.
   *
   * @param userId the user id
   * @return the phone number of the user
   */
  String getPhoneNumber(int userId);

  /**
   * call tha dao to insert the phone number to the specific user id.
   *
   * @param userId      the userId
   * @param phoneNumber the phone number
   */
  void addPhoneNumber(int userId, String phoneNumber);

  /**
   * call tha dao to insert the phone number to the specific user id.
   *
   * @param user user we want to register.
   * @return id of the user.
   */
  int register(UserDTO user);
}
