package utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Date;

public class TokenServiceImpl implements TokenService {
  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTAccess"));
  private final ObjectMapper jsonMapper = new ObjectMapper();

  @Override
  public String createToken(int id) {
    String token=null;
    // add +- 15 min token life's 1000000
    long tokenLifeTime = System.currentTimeMillis() + (1000000);
    try {
      token = JWT.create().withIssuer("auth0")
          .withClaim("user", id)
          .withExpiresAt(new Date(tokenLifeTime)).sign(this.jwtAlgorithm);
    }catch (Exception e){
      e.printStackTrace();
    }
    return token;
  }

  @Override
  public ObjectNode localStorageLogin(int id, String pseudo, boolean rememberMe) {
    String token = this.createToken(id);
    return jsonMapper.createObjectNode()
        .put("token", token)
        .put("id", id)
        .put("pseudo", pseudo)
        .put("rememberMe", rememberMe);
  }
}
