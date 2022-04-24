package buiseness.impl;

import buiseness.domain.User;
import org.mindrot.jbcrypt.BCrypt;

public class UserImpl implements User {

  private int id;
  private String password;
  private String userName;
  private String state;
  private String reasonForConnectionRefusal;
  private String lastName;
  private String firstName;
  private String city;
  private String street;
  private int postCode;
  private int buildingNumber;
  private int unitNumber;
  private String urlPhoto;
  private String phoneNumber;
  private String role;
  private int nbrItemOffered;
  private int nbrGiftenItems;
  private int nbrItemReceived;
  private int nbrItemNotTaken;

  public UserImpl() {
  }

  @Override
  public int getNbrItemNotTaken() {
    return nbrItemNotTaken;
  }

  @Override
  public void setNbrItemNotTaken(int nbrItemNotTaken) {
    this.nbrItemNotTaken = nbrItemNotTaken;
  }

  @Override
  public int getNbrItemOffered() {
    return nbrItemOffered;
  }

  @Override
  public void setNbrItemOffered(int nbrItemOffered) {
    this.nbrItemOffered = nbrItemOffered;
  }

  @Override
  public int getNbrGiftenItems() {
    return nbrGiftenItems;
  }

  @Override
  public void setNbrGiftenItems(int nbrGiftenItems) {
    this.nbrGiftenItems = nbrGiftenItems;
  }

  @Override
  public int getNbrItemReceived() {
    return nbrItemReceived;
  }

  @Override
  public void setNbrItemReceived(int nbrItemReceived) {
    this.nbrItemReceived = nbrItemReceived;
  }

  @Override
  public String getRole() {
    return role;
  }

  @Override
  public void setRole(String role) {
    this.role = role;
  }

  @Override
  public boolean checkPassword(String password) {
    return BCrypt.checkpw(password, this.password);
  }

  @Override
  public String hashPassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt());
  }

  @Override
  public boolean isDenied() {
    return this.state.equals("denied");
  }

  @Override
  public boolean isWaiting() {
    return this.state.equals("waiting");
  }

  @Override
  public boolean isAdmin() {
    return this.role.equals("admin");
  }

  public int getUnitNumber() {
    return unitNumber;
  }

  public void setUnitNumber(int unitNumber) {
    this.unitNumber = unitNumber;
  }

  public String getUrlPhoto() {
    return urlPhoto;
  }

  public void setUrlPhoto(String urlPhoto) {
    this.urlPhoto = urlPhoto;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  @Override
  public String getUserName() {
    return userName;
  }

  @Override
  public void setUserName(String userName) {
    this.userName = userName;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String getState() {
    return this.state;
  }

  public void setState(String state) {
    this.state = state;
  }

  @Override
  public String getReasonForConnectionRefusal() {
    return reasonForConnectionRefusal;
  }

  @Override
  public void setReasonForConnectionRefusal(String reasonForConnectionRefusal) {
    this.reasonForConnectionRefusal = reasonForConnectionRefusal;
  }

  @Override
  public String getLastName() {
    return lastName;
  }

  @Override
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Override
  public String getFirstName() {
    return firstName;
  }

  @Override
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @Override
  public String getCity() {
    return city;
  }

  @Override
  public void setCity(String city) {
    this.city = city;
  }

  @Override
  public String getStreet() {
    return street;
  }

  @Override
  public void setStreet(String street) {
    this.street = street;
  }

  @Override
  public int getPostCode() {
    return postCode;
  }

  @Override
  public void setPostCode(int postCode) {
    this.postCode = postCode;
  }

  @Override
  public int getBuildingNumber() {
    return buildingNumber;
  }

  @Override
  public void setBuildingNumber(int buildingNumber) {
    this.buildingNumber = buildingNumber;
  }
}
