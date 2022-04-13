package buiseness.factory;

import buiseness.dto.DateDTO;
import buiseness.dto.ItemDTO;
import buiseness.dto.ItemTypeDTO;
import buiseness.dto.UserDTO;

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

  DateDTO getDate();
}
