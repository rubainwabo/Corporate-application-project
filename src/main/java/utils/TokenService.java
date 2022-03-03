package utils;

import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface TokenService {
   String createToken(int id, Algorithm algo,long lifeTime);
   ObjectNode localStorageLogin (int id, String pseudo, boolean rememberMe);
   boolean verifyRefreshToken(String token);
   String getAccessToken(int id);
  }
