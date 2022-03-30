package buiseness.ucc;

import buiseness.dto.ItemDTO;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
   * call the dao to add an interest.
   *
   * @param itemId     the itemID
   * @param objectNode the node getted by the front
   * @param userId     the user who's sending a request
   * @return
   */
  void addInterest(int itemId, ObjectNode objectNode, int userId);

  /**
   * call the DAO to change the itemCondition to cancel.
   *
   * @param idItem the item id
   * @param userId the userId sending a request
   */
  void cancelOffer(int idItem, int userId);

  /**
   * call the dao to get all the last items.
   *
   * @param isConnected true if the user is connected
   * @return list of last items
   */
  List<ItemDTO> getLastItemsOffered(boolean isConnected);

  List<ItemDTO> getAllItemsOffered(int id);
}
