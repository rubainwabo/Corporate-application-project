package buiseness.ucc;

import buiseness.domain.User;
import buiseness.domain.UserDTO;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dal.services.UserDAO;
import jakarta.inject.Inject;
import java.util.List;
import utils.TokenService;
import utils.exception.InvalidStateException;
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
    if (user.isDenied()) {
      throw new ReasonForConnectionRefusalException(user.getReasonForConnectionRefusal());
    }
    if (user.isWaiting()) {
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

  @Override
  public User getOneById(int id) {
    return (User) myUserDAO.getOneById(id);
  }

  public boolean checkAdmin(int id) {
    User myUser = (User) myUserDAO.getOneById(id);
    return myUser.isAdmin();
  }

  public boolean checkWaitingOrDenied(int id) {
    User myUser = (User) myUserDAO.getOneById(id);
    return !myUser.isWaiting() && !myUser.isDenied();
  }

  public List<UserDTO> getUsersDenied() {
    return myUserDAO.getAllUserByState("denied");
  }

  @Override
  public List<UserDTO> getUserWaiting() {
    return myUserDAO.getAllUserByState("waiting");
  }

  @Override
  public boolean changeState(int id, String state, String refusalReason)
      throws InvalidStateException {
    System.out.println("here" + state);
    if (!state.equals("denied") && !state.equals("valid")) {
      throw new InvalidStateException("Trying to insert invalid state");
    }

    if (myUserDAO.getOneById(id) == null) {
      return false;
    }
    myUserDAO.changeState(id, state, refusalReason);
    return true;
  }
}