package buiseness.ucc;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface UserUCC {

  /**
   * allows you to connect a user.
   *
   * @param username the username of the person trying to connect
   * @param password his password
   * @return an objectNode which will be composed of his token(s), his id, his nickname and if he
   * wants to be remembered
   */
  ObjectNode login(String username, String password, boolean rememberMe);

  /**
   * verify the refresh token and create 2 token (1 refresh and 1 access).
   *
   * @param token token of the request
   * @return an acess and refresh token
   */
  public ObjectNode refreshToken(String token);
}
