package buiseness.ucc;

import buiseness.domain.User;
import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dal.services.UserDAO;
import jakarta.inject.Inject;
import utils.TokenService;

public class UserUCCImpl implements UserUCC {

  @Inject
  private UserDAO myUserDAO;

  @Inject
  private TokenService myTokenService;

  @Override
  public ObjectNode login(String username, String password, boolean rememberMe) {
    User user = (User) myUserDAO.getOne(username);
    if (user == null) {
      return null;
    }
    if (!user.checkPassword(password)) {
      return null;
    }
    if (!user.checkState(user.getState()) || !user.getState().equals("valid")) {
      return null;
    }
    return myTokenService.login(user.getId(), username, rememberMe);
  }

  @Override
  public ObjectNode refreshToken(String token) {
    if (!myTokenService.isJWT(token)) {
      return null;
    }
    if (!myTokenService.verifyRefreshToken(token)) {
      return null;
    }
    var idUser = JWT.decode(token).getClaim("user").asInt();
    return myTokenService.getRefreshedTokens(idUser);
  }
}