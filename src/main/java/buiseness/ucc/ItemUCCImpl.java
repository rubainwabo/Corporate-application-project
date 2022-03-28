package buiseness.ucc;

import buiseness.domain.dto.ItemDTO;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dal.services.DateDAO;
import dal.services.ItemDAO;
import jakarta.inject.Inject;
import java.util.List;

public class ItemUCCImpl implements ItemUCC {

  @Inject
  private ItemDAO myItemDAOService;

  @Inject
  private DateDAO myDateDAOService;

  @Override
  public int addItem(ItemDTO item, int userId) {
    int itemId = -1;
    itemId = myItemDAOService.addItem(item, userId);
    if (itemId <= 0) {
      throw new IllegalArgumentException("fail insert in db : addItemItemUCC");
    }
    myDateDAOService.addDate(itemId);
    return itemId;
  }

  @Override
  public ItemDTO getDetails(int id) {
    return myItemDAOService.getOneById(id);
  }

  @Override
  public void addInterest(int itemId, ObjectNode objectNode, int userId) {
    myItemDAOService.addInterest(itemId, objectNode, userId);
  }

  @Override
  public void cancelOffer(int idItem, int userId) {
    myItemDAOService.cancelOffer(idItem, userId);
  }

  @Override
  public List<ItemDTO> getLastItemsOffered(boolean isConnected) {
    if (isConnected) {
      return myItemDAOService.getLastItemsOffered(0);
    }
    return myItemDAOService.getLastItemsOffered(12);
  }
}