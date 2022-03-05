package buiseness.domain;

import org.mindrot.jbcrypt.BCrypt;

public class UserImpl implements User, UserDTO {

  private int id;
  private String password;
  private String username;
  private String state;
  private String reasonForConnectionRefusal;

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
  public String getReasonForConnectionRefusal(String msg) {
    return reasonForConnectionRefusal;
  }

  @Override
  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public void setReasonForConnectionRefusal(String reasonForConnectionRefusal) {
    this.reasonForConnectionRefusal = reasonForConnectionRefusal;
  }
}