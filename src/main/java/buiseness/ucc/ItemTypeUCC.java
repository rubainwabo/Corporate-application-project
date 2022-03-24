package buiseness.ucc;

public interface ItemTypeUCC {

  /**
   * call the itemTypeDAO to add the item.
   *
   * @param itemType the itemType
   * @return the itemType id added
   */
  int addItemType(String itemType);
}
