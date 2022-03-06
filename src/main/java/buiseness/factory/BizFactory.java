package buiseness.factory;

import buiseness.domain.UserDTO;

public interface BizFactory {

  /**
   * creates an empty user.
   *
   * @return an empty user
   */
  UserDTO getUser();
}
