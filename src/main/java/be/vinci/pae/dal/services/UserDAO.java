package be.vinci.pae.dal.services;

import be.vinci.pae.buiseness.dto.UserDTO;
import java.util.List;

public interface UserDAO {

  /**
   * allows you to retrieve a user against a username.
   *
   * @param username the user username
   * @return the found user or null if it does not exist
   */
  UserDTO getOneByUsername(String username);

  /**
   * allows to retrieve all the users of the db with the state in the parameters.
   *
   * @return returns a list of users with the state in parameter
   */
  List<UserDTO> getAllUserByState(String state);

  /**
   * retrives to get an user by the id in params.
   *
   * @param id the user id
   * @return the user finded
   */
  UserDTO getOneById(int id);

  /**
   * update the phone number of the user.
   *
   * @param userId      the userId
   * @param phoneNumber the user phoneNumber
   */
  void addPhoneNumber(int userId, String phoneNumber);

  boolean updateProfile(int id, String username, String firstName, String lastName,
      String street, int number, int postcode, String box, String city, String phone);

  boolean updatePassword(int id, String password);

  int register(UserDTO user);

  /**
   * Changes the state of a user.
   *
   * @param userId           : user we want to change his state
   * @param newState         : state we want to put
   * @param newRefusalReason : his refusal reason
   */
  void changeState(int userId, String newState, String newRefusalReason, boolean admin,
      int version);

  List<UserDTO> getAllUsersFiltered(String name, String city, String postCode);

  List<String> getAutocompleteList(String val);

  /**
   * retrives to get all users who are interested in the item with the id idItem.
   *
   * @param idItem the id of the item
   * @return all users who are interested
   */
  List<UserDTO> getUserInterest(int idItem);
}
