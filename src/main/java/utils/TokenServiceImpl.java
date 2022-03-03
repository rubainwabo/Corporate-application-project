package utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Date;

public class TokenServiceImpl implements TokenService {

  private final Algorithm jwtAlgorithmAcess = Algorithm.HMAC256(Config.getProperty("JWTAccess"));
  private final Algorithm jwtAlgorithmRefresh = Algorithm.HMAC256(Config.getProperty("JWTRefresh"));
  private final ObjectMapper jsonMapper = new ObjectMapper();
  private final long tokenAcessLifeTime = 1000000;
  private final long tokenRefreshLifeTime = 1000000000;

  @Override
  public String createToken(int id, Algorithm algo, long lifeTime) {
    String token = null;
    long tokenLifeTime = System.currentTimeMillis() + (lifeTime);
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
  public ObjectNode localStorageLogin(int id, String pseudo, boolean rememberMe) {
    String tokenAccess = this.createToken(id, jwtAlgorithmAcess, tokenAcessLifeTime);
    String tokenRefresh = this.createToken(id, jwtAlgorithmRefresh, tokenRefreshLifeTime);

    return jsonMapper.createObjectNode()
        .put("tokenRefresh", tokenRefresh)
        .put("accessToken", tokenAccess)
        .put("id", id)
        .put("pseudo", pseudo)
        .put("rememberMe", rememberMe);
  }

  @Override
  public String getAccessToken(int id) {
    return this.createToken(id, jwtAlgorithmAcess, tokenAcessLifeTime);
  }


  @Override
  public boolean verifyRefreshToken(String token) {
    try {
      JWT.require(jwtAlgorithmRefresh).withIssuer("auth0").build().verify(token);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
}