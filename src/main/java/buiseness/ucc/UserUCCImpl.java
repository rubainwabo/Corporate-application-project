package buiseness.ucc;

import buiseness.domain.bizclass.User;
import buiseness.domain.dto.UserDTO;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dal.DalServices;
import dal.services.UserDAO;
import jakarta.inject.Inject;
import java.util.List;
import utils.TokenService;
import utils.exception.BizzException;
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
        myDalServices.commit(false);
        throw new PasswordOrUsernameException("username or password incorrect");
      }
      if (user.isDenied()) {
        myDalServices.commit(false);
        throw new ReasonForConnectionRefusalException(user.getReasonForConnectionRefusal());
      }
      if (user.isWaiting()) {
        myDalServices.commit(false);
        throw new UserOnHoldException("user on hold");
      }
      var token = myTokenService.login(user.getId(), username, rememberMe);
      myDalServices.commit(false);
      return token;
    } catch(Exception e) {
      myDalServices.commit(false);
      throw new BizzException("Erreur lors de la connexion à la db");
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
        myDalServices.commit(false);
        throw new BizzException("state invalide");
      }
    } catch(Exception e) {
      myDalServices.commit(false);
      throw new BizzException("Erreur lors de la connexion à la db");
    }
  }

  @Override
  public String getPhoneNumber(int userId) {
    try {
      myDalServices.start(false);
      String str = myUserDAO.getPhoneNumber(userId);
      myDalServices.commit(false);
      return str;
    } catch(Exception e) {
      myDalServices.commit(false);
      throw new BizzException("Erreur lors de la connexion à la db");
    }
  }

  @Override
  public void addPhoneNumber(int userId, String phoneNumber) {
    try {
      myDalServices.start(true);
      myUserDAO.addPhoneNumber(userId, phoneNumber);
      myDalServices.commit(true);
    } catch(Exception e) {
      myDalServices.rollBack();
      throw new BizzException("Erreur lors de la connexion à la db");
    }
  }
}