package buiseness.ucc;

import buiseness.dto.ItemDTO;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dal.DalServices;
import dal.services.DateDAO;
import dal.services.ItemDAO;
import jakarta.inject.Inject;
import java.util.List;
import utils.exception.BizzException;

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
      myDalServices.start(true);
      int itemId = myItemDAOService.addItem(item, userId);
      myDateDAOService.addDate(itemId);
      myDalServices.commit(true);
      return itemId;
    } catch (Exception e) {
      try {
        myDalServices.rollBack();
      } catch (Exception ex) {
        throw new BizzException(ex);
      }
      throw new BizzException(e);
    }
  }

  @Override
  public ItemDTO getDetails(int id) {
    try {
      myDalServices.start(false);
      var item = myItemDAOService.getOneById(id);
      myDalServices.commit(false);
      return item;
    } catch (Exception e) {
      try {
        myDalServices.commit(false);
      } catch (Exception ex) {
        throw new BizzException(ex);
      }
      throw new BizzException(e);
    }
  }

  @Override
  public void addInterest(int itemId, ObjectNode objectNode, int userId) {
    try {
      myDalServices.start(true);
      myItemDAOService.addInterest(itemId, objectNode, userId);
      myDalServices.commit(true);
    } catch (Exception e) {
      try {
        myDalServices.rollBack();
      } catch (Exception ex) {
        throw new BizzException(ex);
      }
      throw new BizzException(e);
    }
  }

  @Override
  public void cancelOffer(int idItem, int userId) {
    try {
      myDalServices.start(true);
      myItemDAOService.cancelOffer(idItem, userId);
      myDalServices.commit(true);
    } catch (Exception e) {
      try {
        myDalServices.rollBack();
      } catch (Exception ex) {
        throw new BizzException(ex);
      }
      throw new BizzException(e);
    }
  }

  @Override
  public List<ItemDTO> getAllItemsOffered(int id) {
    try {
      myDalServices.start(false);
      List<ItemDTO> list;

      list = myItemDAOService.getAllOffered(id);
      myDalServices.commit(false);
      return list;
    } catch (Exception e) {
      try {
        myDalServices.commit(false);
      } catch (Exception ex) {
        throw new BizzException(ex);
      }
      throw new BizzException(e);
    }
  }


  @Override
  public List<ItemDTO> getLastItemsOffered(boolean isConnected) {
    try {
      myDalServices.start(false);
      List<ItemDTO> list;
      if (isConnected) {
        list = myItemDAOService.getLastItemsOffered(0);
        myDalServices.commit(false);
        return list;
      }
      list = myItemDAOService.getLastItemsOffered(12);
      myDalServices.commit(false);
      return list;
    } catch (Exception e) {
      try {
        myDalServices.commit(false);
      } catch (Exception ex) {
        throw new BizzException(ex);
      }
      throw new BizzException(e);
    }
  }

  @Override
  public void ItemCollectedOrNot(int itemId, boolean itemCollected) {
    try {
      myDalServices.start(true);
      myItemDAOService.ItemCollectedOrNot(itemId, itemCollected);
      myDalServices.commit(true);
    } catch (Exception e) {
      try {
        myDalServices.rollBack();
      } catch (Exception ex) {
        throw new BizzException(ex);
      }
      throw new BizzException(e);
    }
  }
}