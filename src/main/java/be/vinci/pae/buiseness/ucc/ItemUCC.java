package be.vinci.pae.buiseness.ucc;

import be.vinci.pae.buiseness.dto.ItemDTO;
import java.util.List;

public interface ItemUCC {

  /**
   * call the dao to add the item.
   *
   * @param item the item
   * @param user the user
   * @return the id of the item added
   */
  int addItem(ItemDTO item, int user);

  /**
   * call the dao to get the details of the item with the id in params.
   *
   * @param id the id of the object
   * @return item details
   */
  ItemDTO getDetails(int id);

  /**
   * call the dao to get the the items with the filter and the input in params.
   *
   * @param filter date, type, state, or name, depending on what the user wants.
   * @param input  search input user has already put.
   * @return list of items
   */
  List<ItemDTO> getItems(String filter, String input);

  /**
   * call the dao to add an interest.
   *
   * @param itemId         the item id
   * @param userId         the user's who's sending a request
   * @param callMe         if the user's want to be called
   * @param phoneNumber    the user's phone number
   * @param availabilities the user's availabilities
   */
  void addInterest(int itemId, int userId, boolean callMe,
      String phoneNumber, String availabilities);

  /**
   * call the DAO to change the itemCondition to cancel.
   *
   * @param idItem    the item id
   * @param userId    the userId sending a request
   * @param condition the new condition of the item
   */
  void changeItemCondition(int idItem, int userId, String condition);

  /**
   * call the dao to get all the last items.
   *
   * @param isConnected true if the user is connected
   * @return list of last items
   */
  List<ItemDTO> getLastItemsOffered(boolean isConnected);

  void itemCollectedOrNot(int itemId, boolean itemCollected, int reqUsrId);

  List<ItemDTO> memberItemsByItemCondition(String itemCondition, int userId, boolean isOfferor);

  List<ItemDTO> getMyItems(int id, String state, boolean mine);

  /**
   * call to add recipient to a item.
   *
   * @param idItem      the id of the item
   * @param idRecipient the id of de recipient
   * @return return 1 if the recipient is added, 0 if not
   */
  int addRecipient(int idItem, int idRecipient);

  /**
   * call update some info of an item.
   *
   * @param item   the id of the item
   * @param userId user who wants to update
   * @return return 1 if the item is updated, 0 if not
   */
  int updateItem(ItemDTO item, int userId);


  /**
   * call to offer again an item.
   *
   * @param idItem the id of the item
   * @param userId user who want to offer again
   */
  void offerAgain(int idItem, int userId);

  void updateItemUrl(int itemId, String img);

  void rateItem(int itemId, int nbStars, String comment);

}
