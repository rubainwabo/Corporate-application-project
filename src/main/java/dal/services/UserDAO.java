package dal.services;

import buiseness.domain.UserDTO;

public interface UserDAO {
    public UserDTO login(String login,String password);
}
