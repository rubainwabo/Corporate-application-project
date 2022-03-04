package buiseness.domain;

import org.mindrot.jbcrypt.BCrypt;

public class UserImpl implements User, UserDTO {

  private final String[] possibleState = {"valid", "waiting", "denied"};
  private int id;
  private String password;
  private String username;
  private String state;

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
  public boolean checkState(String state) {
    for (String e : this.possibleState) {
      if (e.equals(state)) {
        return true;
      }
    }
    return false;
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
}