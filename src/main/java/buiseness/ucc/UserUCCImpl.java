package buiseness.ucc;

import buiseness.domain.User;
import buiseness.dto.UserDTO;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dal.DalServices;
import dal.services.UserDAO;
import jakarta.inject.Inject;
import java.util.List;
import utils.TokenService;
import utils.exception.BizzException;
import utils.exception.InvalidStateException;
import utils.exception.InvalidTokenException;
import utils.exception.PasswordOrUsernameException;
import utils.exception.ReasonForConnectionRefusalException;
import utils.exception.UserOnHoldException;

public class UserUCCImpl implements UserUCC {

  @Inject
  private UserDAO myUserDAO;

  @Inject
  private TokenService myTokenService;

  @Inject
  private DalServices myDalServices;

  @Override
  public ObjectNode login(String username, String password, boolean rememberMe) {
    try {
      myDalServices.start(false);
      User user = (User) myUserDAO.getOneByUsername(username);
      if (!user.checkPassword(password)) {
        throw new PasswordOrUsernameException("username or password incorrect");
      }
      if (user.isDenied()) {
        throw new ReasonForConnectionRefusalException(user.getReasonForConnectionRefusal());
      }
      if (user.isWaiting()) {
        throw new UserOnHoldException("user on hold");
      }
      var token = myTokenService.login(user.getId(), username, rememberMe);
      myDalServices.commit(false);
      return token;
    } catch (Exception e) {
      try {
        myDalServices.commit(false);
      } catch (Exception ex) {
        throw new BizzException(ex);
      }
      throw new BizzException(e);
    }
  }

  @Override
  public ObjectNode refreshToken(String token) {
    if (!myTokenService.isJWT(token) || !myTokenService.verifyRefreshToken(token)) {
      throw new InvalidTokenException("invalid token");
    }
    var userId = myTokenService.getUserId(token);
    return myTokenService.getRefreshedTokens(userId);
  }

  @Override
  public List<UserDTO> getUsersByState(String state) {
    try {
      myDalServices.start(false);
      if (state.equals("denied") || state.equals("valid") || state.equals("waiting")) {
        var list = myUserDAO.getAllUserByState(state);
        myDalServices.commit(false);
        return list;
      } else {
        throw new InvalidStateException("state invalide");
      }
    } catch (Exception e) {
      try {
        myDalServices.commit(false);
      } catch (Exception ex) {
        throw new BizzException(ex);
      }
      throw new BizzException(e);
    }
  }

  @Override
  public String getPhoneNumber(int userId) {
    try {
      myDalServices.start(false);
      String str = myUserDAO.getPhoneNumber(userId);
      myDalServices.commit(false);
      return str;
    } catch (Exception e) {
      try {
        myDalServices.commit(false);
      } catch (Exception ex) {
        throw new BizzException(ex);
      }
      throw new BizzException(e);
    }
  }

  @Override
  public void addPhoneNumber(int userId, String phoneNumber) {
    try {
      myDalServices.start(true);
      myUserDAO.addPhoneNumber(userId, phoneNumber);
      myDalServices.commit(true);
    } catch (Exception e) {
      try {
        myDalServices.rollBack();
      } catch (Exception ex) {
        throw new BizzException(ex);
      }
      throw new BizzException(e);
    }
  }

  public User getOneById(int id) {
    try {
      myDalServices.start(false);
      var usr = (User) myUserDAO.getOneById(id);
      myDalServices.commit(false);
      return usr;
    } catch (Exception e) {
      try {
        myDalServices.commit(false);
      } catch (Exception ex) {
        throw new BizzException(ex);
      }
      throw new BizzException(e);
    }
  }

  public boolean checkAdmin(int id) {
    try {
      myDalServices.start(false);
      User myUser = (User) myUserDAO.getOneById(id);
      boolean isAdmin = myUser.isAdmin();
      myDalServices.commit(false);
      return isAdmin;
    } catch (Exception e) {
      try {
        myDalServices.commit(false);
      } catch (Exception ex) {
        throw new BizzException(ex);
      }
      throw new BizzException(e);
    }
  }

  public boolean checkWaitingOrDenied(int id) {
    try {
      myDalServices.start(false);
      User myUser = (User) myUserDAO.getOneById(id);
      boolean isValid = !myUser.isWaiting() && !myUser.isDenied();
      myDalServices.commit(false);
      return isValid;
    } catch (Exception e) {
      try {
        myDalServices.commit(false);
      } catch (Exception ex) {
        throw new BizzException(ex);
      }
      throw new BizzException(e);
    }
  }

  @Override
  public boolean changeState(int id, String state, String refusalReason, boolean admin) {
    try {
      if (!state.equals("denied") && !state.equals("valid")) {
        throw new InvalidStateException("Trying to insert invalid state");
      }
      myDalServices.start(true);
      if (myUserDAO.getOneById(id) == null) {
        myDalServices.commit(false);
        return false;
      }
      myUserDAO.changeState(id, state, refusalReason, admin);
      myDalServices.commit(true);
      return true;
    } catch (Exception e) {
      try {
        myDalServices.rollBack();
      } catch (Exception ex) {
        throw new BizzException(ex);
      }
      throw new BizzException(e);
    }
  }
}