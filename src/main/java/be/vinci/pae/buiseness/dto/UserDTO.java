package be.vinci.pae.buiseness.dto;


import be.vinci.pae.buiseness.impl.UserImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = UserImpl.class)
public interface UserDTO {

  int getVersion();

  void setVersion(int version);

  /**
   * get the user's username.
   *
   * @return user username
   */
  String getUserName();

  /**
   * change the user's username by the one passed in parameter.
   *
   * @param username the new username
   */
  void setUserName(String username);

  /**
   * get the user's id.
   *
   * @return the user's id
   */
  int getId();

  /**
   * change the user's id by the one passed in parameter.
   *
   * @param id the new user's id
   */
  void setId(int id);

  /**
   * get the user's password.
   *
   * @return user password
   */
  String getPassword();

  /**
   * change the user's password by the one passed in parameter.
   *
   * @param password the new user's password
   */
  void setPassword(String password);

  /**
   * get the user's state.
   *
   * @return user state
   */
  String getState();

  /**
   * change the user's state by the one passed in parameter.
   *
   * @param state the new user's state
   */
  void setState(String state);

  /**
   * allows to retrieve the reason of refusal of a user.
   *
   * @return the refusal message
   */
  String getReasonForConnectionRefusal();

  /**
   * change the user's reasonForConnectionRefusal by the one passed in parameter.
   *
   * @param reasonForConnectionRefusal the new user's reasonForConnectionRefusal
   */
  void setReasonForConnectionRefusal(String reasonForConnectionRefusal);

  String getLastName();

  void setLastName(String lastName);

  String getFirstName();

  void setFirstName(String firstName);

  String getCity();

  void setCity(String city);

  String getStreet();

  void setStreet(String street);

  int getBuildingNumber();

  void setBuildingNumber(int buildingNumber);

  int getUnitNumber();

  void setUnitNumber(int unitNumber);

  int getPostCode();

  void setPostCode(int postCode);

  String getUrlPhoto();

  void setUrlPhoto(String urlPhoto);

  String getPhoneNumber();

  void setPhoneNumber(String phoneNumber);

  String getRole();

  void setRole(String role);

  int getNbrItemOffered();

  void setNbrItemOffered(int nbrItemOffrered);

  int getNbrGiftenItems();

  void setNbrGiftenItems(int nbrGiftenItems);

  int getNbrItemReceived();

  void setNbrItemReceived(int nbrItemReceived);

  int getNbrItemNotTaken();

  void setNbrItemNotTaken(int nbrItemNotTaken);

}
