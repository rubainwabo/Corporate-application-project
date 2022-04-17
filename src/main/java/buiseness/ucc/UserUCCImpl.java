package buiseness.ucc;

import buiseness.domain.User;
import buiseness.dto.UserDTO;
import dal.DalServices;
import dal.services.UserDAO;
import jakarta.inject.Inject;
import java.util.List;
import utils.exception.InvalidStateException;
import utils.exception.PasswordOrUsernameException;
import utils.exception.ReasonForConnectionRefusalException;
import utils.exception.UserOnHoldException;
import utils.exception.UsernameAlreadyExists;


public class UserUCCImpl implements UserUCC {

  @Inject
  private UserDAO myUserDAO;

  @Inject
  private DalServices myDalServices;

  @Override
  public UserDTO login(String username, String password) {
    try {
      myDalServices.start();
      User user = (User) myUserDAO.getOneByUsername(username);
      if (user == null) {
        throw new PasswordOrUsernameException("username or password incorrect");
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
      myDalServices.commit();
      return user;
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }
  }

  @Override
  public List<UserDTO> getUsersByState(String state) {
    try {
      myDalServices.start();
      if (state.equals("denied") || state.equals("valid") || state.equals("waiting")) {
        var list = myUserDAO.getAllUserByState(state);
        myDalServices.commit();
        return list;
      } else {
        throw new InvalidStateException("state invalide");
      }
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }
  }

  @Override
  public void addPhoneNumber(int userId, String phoneNumber) {
    try {
      myDalServices.start();
      myUserDAO.addPhoneNumber(userId, phoneNumber);
      myDalServices.commit();
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }
  }

  @Override
  public User getOneById(int id) {
    try {
      myDalServices.start();
      var usr = (User) myUserDAO.getOneById(id);
      myDalServices.commit();
      return usr;
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }
  }

  @Override
  public boolean checkAdmin(int id) {
    try {
      myDalServices.start();
      User myUser = (User) myUserDAO.getOneById(id);
      boolean isAdmin = myUser.isAdmin();
      myDalServices.commit();
      return isAdmin;
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }
  }

  @Override
  public boolean checkWaitingOrDenied(int id) {
    try {
      myDalServices.start();
      User myUser = (User) myUserDAO.getOneById(id);
      boolean isValid = !myUser.isWaiting() && !myUser.isDenied();
      myDalServices.commit();
      return isValid;
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }
  }

  @Override
  public boolean changeState(int id, String state, String refusalReason, boolean admin) {
    try {
      if (!state.equals("denied") && !state.equals("valid")) {
        throw new InvalidStateException("Trying to insert invalid state");
      }
      myDalServices.start();
      if (myUserDAO.getOneById(id) == null) {
        myDalServices.commit();
        return false;
      }
      myUserDAO.changeState(id, state, refusalReason, admin);
      myDalServices.commit();
      return true;
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }
  }

  @Override
  public int register(UserDTO user) {
    try {
      myDalServices.start();
      User user1 = (User) user;
      user1.setPassword(user1.hashPassword(user1.getPassword()));
      UserDTO userExist = myUserDAO.getOneByUsername(user.getUserName());
      if (userExist != null) {
        throw new UsernameAlreadyExists("username already exists");
      }
      System.out.println("demande de l'id");
      int idUser = myUserDAO.register(user1);
      //var userConnected = myTokenService.login(idUser, user.getUserName(), false);
      myDalServices.commit();
      return idUser;
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }
  }

  @Override
  public List<UserDTO> getUsersIterest(int idItem) {
    try {
      myDalServices.start();
      List<UserDTO> list = myUserDAO.getUserInterest(idItem);
      myDalServices.commit();
      return list;
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }
  }
}
