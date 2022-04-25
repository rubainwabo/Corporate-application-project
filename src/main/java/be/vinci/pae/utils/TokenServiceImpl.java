package be.vinci.pae.utils;

import be.vinci.pae.buiseness.dto.UserDTO;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Date;


public class TokenServiceImpl implements TokenService {

  // 2 JWT secret code to distinguish between an access and a refresh token
  private final Algorithm jwtAlgorithmAccess = Algorithm.HMAC256(Config.getProperty("JWTAccess"));
  private final Algorithm jwtAlgorithmRefresh = Algorithm.HMAC256(Config.getProperty("JWTRefresh"));
  private final ObjectMapper jsonMapper = new ObjectMapper();

  private final long tokenAccessLifeTime = 1000000;
  private final long tokenRefreshLifeTime = 2147483647;


  @Override
  public String createToken(int id, Algorithm algo, long lifeTime) {
    String token = null;
    // create a date from now + its lifetime
    long tokenLifeTime = new Date().getTime() + (lifeTime);
    try {
      token = JWT.create().withIssuer("auth0")
          .withClaim("user", id)
          .withExpiresAt(new Date(tokenLifeTime)).sign(algo);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return token;
  }

  @Override
  public ObjectNode login(UserDTO user, boolean rememberMe) {
    int id = user.getId();
    String tokenAccess = this.createToken(id, jwtAlgorithmAccess, tokenAccessLifeTime);
    String tokenRefresh = null;
    // if he wants to be remembered, we add a refresh token
    if (rememberMe) {
      tokenRefresh = this.createToken(id, jwtAlgorithmRefresh, tokenRefreshLifeTime);
    }
    // inserts all the data that will be saved in the user's localStorage into a ObjectNode
    return jsonMapper.createObjectNode()
        .put("tokenRefresh", tokenRefresh)
        .put("accessToken", tokenAccess)
        .put("id", id)
        .put("username", user.getUserName())
        .put("rememberMe", rememberMe)
        .put("role", user.getRole());
  }

  @Override
  public ObjectNode getRefreshedTokens(int id) {

    String tokenAccess = this.createToken(id, jwtAlgorithmAccess, tokenAccessLifeTime);
    String tokenRefresh = this.createToken(id, jwtAlgorithmRefresh, tokenRefreshLifeTime);
    return jsonMapper.createObjectNode()
        .put("tokenRefresh", tokenRefresh)
        .put("accessToken", tokenAccess);
  }

  @Override
  public DecodedJWT getVerifyToken(String token) {
    return JWT.require(jwtAlgorithmAccess).withIssuer("auth0").build().verify(token);
  }

  @Override
  public DecodedJWT getVerifyRefreshToken(String token) {
    return JWT.require(jwtAlgorithmRefresh).withIssuer("auth0").build().verify(token);
  }
}
