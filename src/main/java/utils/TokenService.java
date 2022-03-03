package utils;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface TokenService {
   String createToken(int id);
   ObjectNode localStorageLogin (int id, String pseudo, boolean rememberMe);
  }
