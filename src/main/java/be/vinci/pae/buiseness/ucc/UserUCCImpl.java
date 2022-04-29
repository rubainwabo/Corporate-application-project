package be.vinci.pae.buiseness.ucc;

import be.vinci.pae.buiseness.domain.User;
import be.vinci.pae.buiseness.dto.ItemDTO;
import be.vinci.pae.buiseness.dto.UserDTO;
import be.vinci.pae.dal.DalServices;
import be.vinci.pae.dal.services.ItemDAO;
import be.vinci.pae.dal.services.UserDAO;
import be.vinci.pae.utils.exception.InvalidStateException;
import be.vinci.pae.utils.exception.PasswordOrUsernameException;
import be.vinci.pae.utils.exception.ReasonForConnectionRefusalException;
import be.vinci.pae.utils.exception.UserInvalidException;
import be.vinci.pae.utils.exception.UserOnHoldException;
import be.vinci.pae.utils.exception.UsernameAlreadyExists;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;


public class UserUCCImpl implements UserUCC {

  @Inject
  private UserDAO myUserDAO;

  @Inject
  private DalServices myDalServices;

  @Inject
  private ItemDAO myItemDAOService;

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
        throw new InvalidStateException("invalid state");
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
  public boolean changeState(int id, String state, String refusalReason, boolean admin,
      int version) {
    try {
      if (!state.equals("denied") && !state.equals("valid")) {
        throw new InvalidStateException("Trying to insert invalid state");
      }
      myDalServices.start();
      if (myUserDAO.getOneById(id) == null) {
        myDalServices.commit();
        return false;
      }
      myUserDAO.changeState(id, state, refusalReason, admin, version);
      myDalServices.commit();
      return true;
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }
  }

  @Override
  public boolean updateProfile(int id, String username, String firstName, String lastName,
      String street, int number, int postcode, String box, String city, String phone) {
    try {
      myDalServices.start();
      boolean ret = myUserDAO.updateProfile(id, username, firstName, lastName, street, number,
          postcode, box,
          city, phone);
      myDalServices.commit();
      return ret;
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }
  }

  @Override
  public boolean updatePassword(int id, String password) {
    try {
      myDalServices.start();
      boolean ret = myUserDAO.updatePassword(id, password);
      myDalServices.commit();
      return ret;
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
      int idUser = myUserDAO.register(user1);
      myDalServices.commit();
      return idUser;
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }
  }

  @Override
  public List<UserDTO> getAllUsersFiltered(String name, String city, String postCode) {
    try {
      myDalServices.start();
      var membersFiltered = myUserDAO.getAllUsersFiltered(name, city, postCode);
      myDalServices.commit();
      return membersFiltered;
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }
  }

  @Override
  public List<String> getAutocompleteList(String val) {
    if (val.isBlank()) {
      return new ArrayList<>();
    }
    try {
      myDalServices.start();
      var autocompleteList = myUserDAO.getAutocompleteList(val);
      myDalServices.commit();
      return autocompleteList;
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }
  }

  @Override
  public List<UserDTO> getUsersInterest(int reqUserId, int idItem) {
    try {
      myDalServices.start();
      ItemDTO itemDTO = myItemDAOService.getOneById(idItem);
      if (reqUserId != itemDTO.getOfferorId()) {
        throw new UserInvalidException(
            "la personne essayant de faire la requête n'est pas l'offereur de l'objet");
      }
      List<UserDTO> list = myUserDAO.getUserInterest(idItem);
      myDalServices.commit();
      return list;
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }
  }
}
