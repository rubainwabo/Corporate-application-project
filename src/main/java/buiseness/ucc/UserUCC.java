package buiseness.ucc;

import buiseness.domain.User;
import buiseness.domain.UserDTO;
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

  User getOneById(int id);

  boolean checkAdmin(int id);

  boolean checkWaitingOrDenied(int id);

  /**
   * allows to retrieve all the users of the db with the role refused.
   *
   * @return a list of users with denied role
   */
  List<UserDTO> getUsersDenied();

  /**
   * allows to retrieve all the users of the db with the state waiting.
   *
   * @return a list of users with waiting state
   */
  List<UserDTO> getUserWaiting();
}
