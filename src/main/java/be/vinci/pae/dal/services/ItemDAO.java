package be.vinci.pae.dal.services;

import be.vinci.pae.buiseness.dto.ItemDTO;
import java.util.List;

public interface ItemDAO {


  /**
   * returns list of items after filter by name, state depending on input.
   *
   * @param filter we wanna have.
   * @param input  input of the user.
   * @return a list of item
   */
  List<ItemDTO> getFiltered(String filter, String input);

  /**
   * add the item to the database.
   *
   * @param item   the item to add
   * @param userId the offeror
   * @return the id of the item added
   */
  int addItem(ItemDTO item, int userId);

  /**
   * retrives the item by the id in params.
   *
   * @param id the item id
   * @return the item
   */
  ItemDTO getOneById(int id);

  /**
   * try to add an interest for an object.
   *
   * @param idItem the item id
   * @param userId the user who's sending a request
   * @param callMe if the user want to be called
   * @param userId the user who send the request
   */
  void addInterest(int idItem, int userId, boolean callMe,
      String phoneNumber, String avaibatilities);

  /**
   * retrives to change the itemCondition to cancelled.
   *
   * @param idItem    the idItem
   * @param userId    the user who send the request
   * @param condition the new condition of the item
   */
  void changeItemCondition(int idItem, int userId, String condition);

  List<ItemDTO> getMyItems(int id, String state, boolean mine);

  /**
   * retrives to get all the last item with the itemCondition offered.
   *
   * @param limit the limit of rows
   * @return a list of item
   */
  List<ItemDTO> getLastItemsOffered(int limit);

  void itemCollectedOrNot(ItemDTO itemDTO, boolean itemCollected);

  List<ItemDTO> memberItemsByItemCondition(String itemCondition, int userId, boolean isOfferor);

  /**
   * retrives to add a recipient to an item.
   *
   * @param idItem      the id of the item
   * @param idRecipient the id of de recipient
   * @return return 1 if the recipient is added, 0 if not
   */
  int addRecipient(int idItem, int idRecipient);

  /**
   * retrives to update some info of an item.
   *
   * @param item the id of the item
   * @return return 1 if the item is updated, 0 if not
   */
  int updateItem(ItemDTO item);

  void rateItem(int itemId, int nbStars, String comment);

}
