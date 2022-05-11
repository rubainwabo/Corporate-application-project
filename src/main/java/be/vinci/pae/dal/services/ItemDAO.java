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
   * @param idItem        the item id
   * @param userId        the user's who's sending a request
   * @param callMe        if the user's want to be called
   * @param phoneNumber   the user's phone number
   * @param availabilitie the user's availabilities
   * @param version       the version of the item
   */
  void addInterest(int idItem, int userId, boolean callMe,
      String phoneNumber, String availabilitie, int version);

  /**
   * retrives to change the itemCondition to cancelled.
   *
   * @param idItem    the idItem
   * @param userId    the user who send the request
   * @param condition the new condition of the item
   */
  void changeItemCondition(int idItem, int userId, String condition, int version);

  List<ItemDTO> getMyItems(int id, String state, int type, boolean mine);

  /**
   * retrives to get all the last item with the itemCondition offered.
   *
   * @param limit the limit of rows
   * @return a list of item
   */
  List<ItemDTO> getLastItemsOffered(int limit);

  void itemCollectedOrNot(ItemDTO itemDTO, boolean itemCollected, int version);

  List<ItemDTO> memberItemsByItemCondition(String itemCondition, int userId, boolean isOfferor);

  /**
   * retrives to add a recipient to an item.
   *
   * @param idItem      the id of the item
   * @param idRecipient the id of de recipient
   * @return return 1 if the recipient is added, 0 if not
   */
  int addRecipient(int idItem, int idRecipient, int version);

  /**
   * retrives to update some info of an item.
   *
   * @param item the id of the item
   * @return return 1 if the item is updated, 0 if not
   */
  int updateItem(ItemDTO item);

  void rateItem(int itemId, int nbStars, String comment, int version);

  void updateItemOfInvalidMember(int memberId);


}
