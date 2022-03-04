package utils;

import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface TokenService {

  /**
   * allows the creation of a token via a certain algo and a lifetime.
   *
   * @param id       the user id
   * @param algo     the algo used
   * @param lifeTime the lifeTime of the token
   * @return the created token
   */
  String createToken(int id, Algorithm algo, long lifeTime);

  /**
   * will create an objectNode containing all user information (id, token(s),username,rememberMe).
   *
   * @param id         user id
   * @param username   user username
   * @param rememberMe if he want to be remembered
   * @return an objectNode containing all user information
   */
  ObjectNode login(int id, String username, boolean rememberMe);

  /**
   * verifies the validity of the token.
   *
   * @param token a refresh token
   * @return true if it is valid, else false
   */
  boolean verifyRefreshToken(String token);

  /**
   * creates a refresh and access tokens.
   *
   * @param id user's id
   * @return the created token
   */
  ObjectNode getRefreshedTokens(int id);

  /**
   * verifies if the token is a JWT.
   *
   * @param token a refresh token(usually)
   * @return true if it's a JWT, else false
   */
  boolean isJWT(String token);
}
