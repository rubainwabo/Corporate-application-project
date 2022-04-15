package buiseness.ucc;

import buiseness.domain.User;
import buiseness.dto.UserDTO;
import dal.DalServices;
import dal.services.UserDAO;
import jakarta.inject.Inject;
import java.util.List;
import utils.exception.BizzException;
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
      myDalServices.start(false);
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
      myDalServices.commit(false);
      return user;
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

  @Override
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

  @Override
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

  @Override
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

  @Override
  public int register(UserDTO user) {
    try {
      myDalServices.start(true);

      User user1 = (User) user;
      user1.setPassword(user1.hashPassword(user1.getPassword()));

      UserDTO userExist = myUserDAO.getOneByUsername(user.getUserName());

      if (userExist != null) {
        myDalServices.rollBack();
        throw new UsernameAlreadyExists("username already exists");
      }
      int idUser = myUserDAO.register(user1);
      myDalServices.commit(true);
      return idUser;
    } catch (Exception e) {
      try {
        myDalServices.rollBack();
      } catch (Exception ex) {
        throw new BizzException(ex);
      }
      throw new BizzException(e);
    }
  }

  @Override
  public List<UserDTO> getAllUserFiltred(String name, String city, String postCode) {
    try {
      myDalServices.start(false);
      var memberFiltred = myUserDAO.getAllUserFiltred(name, city, postCode);
      myDalServices.commit(false);
      return memberFiltred;
    } catch (Exception e) {
      try {
        myDalServices.commit(false);
      } catch (Exception ex) {
        throw new BizzException(ex);
      }
      throw new BizzException(e);
    }
  }
}
