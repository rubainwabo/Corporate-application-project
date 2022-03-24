package buiseness.factory;

import buiseness.domain.dto.ItemDTO;
import buiseness.domain.dto.ItemTypeDTO;
import buiseness.domain.dto.UserDTO;

public interface BizFactory {

  /**
   * creates an empty user.
   *
   * @return an empty user
   */
  UserDTO getUser();

  /**
   * creates an empty item.
   *
   * @return an empty item
   */
  ItemDTO getItem();

  /**
   * creates an empty itemType.
   *
   * @return an empty itemType
   */
  ItemTypeDTO getItemType();

}
