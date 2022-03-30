package dal.services;

import buiseness.dto.ItemTypeDTO;
import java.util.List;

public interface ItemTypeDAO {

  /**
   * retrives to add an item type to the db.
   *
   * @param itemTypeName the itemTypeName
   * @return the id of the itemType added
   */
  ItemTypeDTO addItemType(String itemTypeName);

  /**
   * retrives to get all itemType as a list.
   *
   * @return a list of all the itemType
   */
  List<ItemTypeDTO> getAllItemType();
}
