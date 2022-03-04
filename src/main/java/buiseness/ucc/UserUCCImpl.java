package buiseness.ucc;

import buiseness.domain.User;
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
    return myTokenService.localStorageLogin(user.getId(), username, rememberMe);
  }

  @Override
  public String refreshToken(int id, String token) {
    if (!myTokenService.verifyRefreshToken(token)) {
      return null;
    }
    return myTokenService.getAccessToken(id);
  }
}