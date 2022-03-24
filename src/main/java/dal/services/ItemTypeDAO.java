package dal.services;

public interface ItemTypeDAO {

  /**
   * retrives to add an item type to the db.
   *
   * @param itemTypeName the itemTypeName
   * @return the id of the itemType added
   */
  int addItemType(String itemTypeName);
}
