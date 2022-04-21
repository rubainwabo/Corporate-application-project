package buiseness.ucc;

import buiseness.domain.User;
import buiseness.dto.UserDTO;
import java.util.List;


public interface UserUCC {

  /**
   * allows you to connect a user.
   *
   * @param username the username of the person trying to connect
   * @param password his password
   * @return an objectNode which will be composed of his token(s), id, username,rememberMe
   */

  UserDTO login(String username, String password);

  /**
   * changes the state of an user.
   *
   * @return true if correctly changed.
   */
  boolean changeState(int id, String state, String refusalReason, boolean admin);

  /**
   * updates the profile of an user.
   *
   * @return true if correctly changed.
   */
  boolean updateProfile(int id, String username, String firstName, String lastName,
      String street, int number, int postcode, String box, String city, String phone);

  /**
   * updates the password of an user.
   *
   * @param id       id of the user we want to change password.
   * @param password new password.
   * @return true if changed.
   */
  boolean updatePassword(int id, String password);

  /**
   * returns the user.
   *
   * @param id id of the user we want to get.
   * @return user we want.
   */
  User getOneById(int id);

  /**
   * checks if user is admin.
   *
   * @param id id of the user we want to check
   * @return true if admin.
   */
  boolean checkAdmin(int id);

  /**
   * checks if user is waiting or denied.
   *
   * @param id id of the user we want to check
   * @return true if waiting or denied.
   */
  boolean checkWaitingOrDenied(int id);

  /**
   * allows to retrieve all the users of the db with the state.
   *
   * @return a list of users with the state in params
   */
  List<UserDTO> getUsersByState(String state);

  /**
   * call tha dao to insert the phone number to the specific user id.
   *
   * @param userId      the userId
   * @param phoneNumber the phone number
   */
  void addPhoneNumber(int userId, String phoneNumber);

  /**
   * call tha dao to insert the phone number to the specific user id.
   *
   * @param user user we want to register.
   * @return id of the user.
   */
  int register(UserDTO user);

  /**
   * call to get all users who are interested in the item with the id idItem.
   *
   * @param idItem the id of the item
   * @return all users who are interested
   */
  List<UserDTO> getUsersIterest(int idItem);
}
