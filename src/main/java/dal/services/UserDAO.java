package dal.services;

import buiseness.domain.UserDTO;

public interface UserDAO {

  UserDTO getOne(String pseudo);
}
