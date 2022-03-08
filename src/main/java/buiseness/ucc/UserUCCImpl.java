package buiseness.ucc;

import buiseness.domain.User;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dal.services.UserDAO;
import jakarta.inject.Inject;
import utils.TokenService;
import utils.exception.InvalidTokenException;
import utils.exception.PasswordOrUsernameException;
import utils.exception.ReasonForConnectionRefusalException;
import utils.exception.UserInvalidException;
import utils.exception.UserOnHoldException;

public class UserUCCImpl implements UserUCC {

  @Inject
  private UserDAO myUserDAO;

  @Inject
  private TokenService myTokenService;

  @Override
  public ObjectNode login(String username, String password, boolean rememberMe)
      throws PasswordOrUsernameException, ReasonForConnectionRefusalException,
      UserOnHoldException, UserInvalidException {
    User user = (User) myUserDAO.getOneByUsername(username);
    if (user == null) {
      throw new UserInvalidException("username or password incorrect");
    }
    if (!user.checkPassword(password)) {
      throw new PasswordOrUsernameException("username or password incorrect");
    }
    if (user.isDenied(user.getState())) {
      throw new ReasonForConnectionRefusalException(user.getReasonForConnectionRefusal());
    }
    if (user.isWaiting(user.getState())) {
      throw new UserOnHoldException("user on hold");
    }
    return myTokenService.login(user.getId(), username, rememberMe);
  }

  @Override
  public ObjectNode refreshToken(String token) throws InvalidTokenException {
    if (!myTokenService.isJWT(token) || !myTokenService.verifyRefreshToken(token)) {
      throw new InvalidTokenException("invalid token");
    }
    var userId = myTokenService.getUserId(token);
    return myTokenService.getRefreshedTokens(userId);
  }
}