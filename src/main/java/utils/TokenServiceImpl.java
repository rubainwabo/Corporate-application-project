package utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Base64;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;


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
  public ObjectNode login(int id, String username, boolean rememberMe) {
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
        .put("username", username)
        .put("rememberMe", rememberMe);
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
  public boolean verifyRefreshToken(String token) {
    try {
      // check if the token is expired, and if it uses the right algo + the right secret JTW
      JWT.require(jwtAlgorithmRefresh).withIssuer("auth0").build().verify(token);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }


  @Override
  public boolean isJWT(String token) {
    String[] jwtSplitted = token.split("\\.");
    // The JWT is composed of three parts
    if (jwtSplitted.length != 3) {
      return false;
    }
    try {
      String jsonFirstPart = new String(Base64.getDecoder().decode(jwtSplitted[0]));
      // The first part of the JWT is a JSON
      JSONObject firstPart = new JSONObject(jsonFirstPart);
      // The first part has the attribute "alg"
      if (!firstPart.has("alg")) {
        return false;
      }
    } catch (JSONException err) {
      return false;
    }
    return true;
  }

  @Override
  public int getUserId(String token) {
    return JWT.decode(token).getClaim("user").asInt();
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
