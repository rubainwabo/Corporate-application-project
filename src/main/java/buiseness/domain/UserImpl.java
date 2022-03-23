package buiseness.domain;

import org.mindrot.jbcrypt.BCrypt;

public class UserImpl implements User {

  private int id;
  private String password;
  private String username;
  private String state;
  private String reasonForConnectionRefusal;
  private String lastName;
  private String firstName;
  private String city;
  private String street;
  private String postCode;
  private String buildingNumber;

  public UserImpl() {
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
  public boolean isDenied(String state) {
    return state.equals("denied");
  }

  @Override
  public boolean isWaiting(String state) {
    return state.equals("waiting");
  }

  @Override
  public String getUserName() {
    return username;
  }

  @Override
  public void setUserName(String username) {
    this.username = username;
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
  public void setUsername(String username) {

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
  public String getPostCode() {
    return postCode;
  }

  @Override
  public void setPostCode(String postCode) {
    this.postCode = postCode;
  }

  @Override
  public String getBuildingNumber() {
    return buildingNumber;
  }

  @Override
  public void setBuildingNumber(String buildingNumber) {
    this.buildingNumber = buildingNumber;
  }
}