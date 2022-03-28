package dal.services;

import buiseness.dto.ItemDTO;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;

public interface ItemDAO {

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
   * @param idItem     the item id
   * @param objectNode the node getted from the front
   * @param userId     the user who send the request
   */
  void addInterest(int idItem, ObjectNode objectNode, int userId);

  /**
   * retrives to change the itemCondition to cancelled.
   *
   * @param idItem the idItem
   * @param userId the user who send the request
   */
  void cancelOffer(int idItem, int userId);

  /**
   * retrives to get all the last item with the itemCondition offered.
   *
   * @param limit the limit of rows
   * @return a list of item
   */
  List<ItemDTO> getLastItemsOffered(int limit);
}
