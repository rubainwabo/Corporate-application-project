package dal.services;

import buiseness.dto.UserDTO;
import java.sql.SQLException;
import java.util.List;

public interface UserDAO {

  /**
   * allows you to retrieve a user against a username.
   *
   * @param username the user username
   * @return the found user or null if it does not exist
   */
  UserDTO getOneByUsername(String username) throws SQLException;

  /**
   * allows to retrieve all the users of the db with the state in the parameters.
   *
   * @return returns a list of users with the state in parameter
   */
  List<UserDTO> getAllUserByState(String state) throws SQLException;

  /**
   * retrives to get an user by the id in params.
   *
   * @param id the user id
   * @return the user finded
   */
  UserDTO getOneById(int id) throws SQLException;

  /**
   * retrives to get the phone number of an user.
   *
   * @param userId the userId
   * @return the phoneNumber
   */
  String getPhoneNumber(int userId) throws SQLException;

  /**
   * update the phone number of the user.
   *
   * @param userId      the userId
   * @param phoneNumber the user phoneNumber
   */
  void addPhoneNumber(int userId, String phoneNumber) throws SQLException;

  int register(UserDTO user);

  /**
   * Changes the state of a user.
   *
   * @param userId           : user we want to change his state
   * @param newState         : state we want to put
   * @param newRefusalReason : his refusal reason
   */
  void changeState(int userId, String newState, String newRefusalReason, boolean admin)
      throws SQLException;

  /**
   * get all users who are interested in the item with the id idItem
   *
   * @param idItem the id of the item
   * @return all users who are interested
   */
  List<UserDTO> getUserInterest(int idItem);
}
