package utils;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
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
   * creates a refresh and access tokens.
   *
   * @param id user's id
   * @return the created token
   */
  ObjectNode getRefreshedTokens(int id);

  DecodedJWT getVerifyToken(String token);

  DecodedJWT getVerifyRefreshToken(String token);
}
