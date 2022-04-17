package buiseness.ucc;

import buiseness.dto.ItemDTO;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dal.DalServices;
import dal.services.DateDAO;
import dal.services.ItemDAO;
import jakarta.inject.Inject;
import java.util.List;

public class ItemUCCImpl implements ItemUCC {

  @Inject
  private ItemDAO myItemDAOService;

  @Inject
  private DateDAO myDateDAOService;

  @Inject
  private DalServices myDalServices;

  @Override
  public int addItem(ItemDTO item, int userId) {
    try {
      myDalServices.start();
      int itemId = myItemDAOService.addItem(item, userId);
      myDateDAOService.addDate(itemId);
      myDalServices.commit();
      return itemId;
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }
  }

  @Override
  public ItemDTO getDetails(int id) {
    try {
      myDalServices.start();
      var item = myItemDAOService.getOneById(id);
      myDalServices.commit();
      return item;
    } catch (Exception e) {
      myDalServices.commit();
      throw e;
    }
  }

  @Override
  public void addInterest(int itemId, ObjectNode objectNode, int userId) {
    try {
      myDalServices.start();
      myItemDAOService.addInterest(itemId, objectNode, userId);
      myDalServices.commit();
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }
  }

  @Override
  public void cancelOffer(int idItem, int userId) {
    try {
      myDalServices.start();
      myItemDAOService.cancelOffer(idItem, userId);
      myDalServices.commit();
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }
  }

  @Override
  public List<ItemDTO> getMyItems(int id, String state) {
    try {
      myDalServices.start();
      List<ItemDTO> list = myItemDAOService.getMyItems(id, state);
      myDalServices.commit();
      return list;
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }
  }

  @Override
  public List<ItemDTO> getLastItemsOffered(boolean isConnected) {
    try {
      myDalServices.start();
      List<ItemDTO> list;
      if (isConnected) {
        list = myItemDAOService.getLastItemsOffered(0);
        myDalServices.commit();
        return list;
      }
      list = myItemDAOService.getLastItemsOffered(12);
      myDalServices.commit();
      return list;
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }
  }

  @Override
  public int addRecipient(int idItem, int idRecipient) {
    try {
      myDalServices.start();

      int itemId = myItemDAOService.addRecipient(idItem, idRecipient);
      myDalServices.commit();
      return itemId;
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }

  }

  @Override
  public int updateItem(ItemDTO item) {
    try {
      myDalServices.start();

      int result = myItemDAOService.updateItem(item);
      myDalServices.commit();
      return result;
    } catch (Exception e) {

      myDalServices.rollBack();

      throw e;
    }
  }
}
