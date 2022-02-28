package dal.services;

import buiseness.domain.UserDTO;

public interface UserDAO {

  public UserDTO getOne(String pseudo);
}
