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
import utils.exception.UsernameAlreadyExists;


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
      if (user == null) {
        myDalServices.rollBack();
        throw new PasswordOrUsernameException("username or password incorrect");
      }
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
    } catch (Exception e) {
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
    } catch (Exception e) {
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
    } catch (Exception e) {
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
    } catch (Exception e) {
      myDalServices.rollBack();
      throw new BizzException("Erreur lors de la connexion à la db");
    }
  }

  public User getOneById(int id) {
    try {
      myDalServices.start(false);
      var usr = (User) myUserDAO.getOneById(id);
      myDalServices.commit(false);
      return usr;
    } catch (Exception e) {
      myDalServices.commit(false);
      throw new BizzException("Erreur lors de la connexion à la db");
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
      myDalServices.commit(false);
      throw new BizzException("Erreur lors de la connexion à la db");
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
      myDalServices.commit(false);
      throw new BizzException("Erreur lors de la connexion à la db");
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
      myDalServices.rollBack();
      throw new BizzException("Erreur lors de la connexion à la db");
    }
  }

  @Override
  public ObjectNode register(UserDTO user) throws UsernameAlreadyExists {
    try {
      myDalServices.start(true);

      User user1 = (User) user;
      user1.setPassword(user1.hashPassword(user1.getPassword()));

      UserDTO userExist = myUserDAO.getOneByUsername(user.getUserName());

      if (userExist != null) {
        myDalServices.rollBack();
        throw new UsernameAlreadyExists("username already exists");
      }
      System.out.println("demande de l'id");
      int idUser = myUserDAO.register(user1);

      var userConnected = myTokenService.login(idUser, user.getUserName(), false);
      myDalServices.commit(true);
      return userConnected;
    } catch (Exception e) {
      myDalServices.rollBack();
      throw new BizzException("Erreur lors de la connexion à la db");
    }


  }
}
