package buiseness.ucc;

import buiseness.dto.ItemDTO;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dal.DalServices;
import dal.services.DateDAO;
import dal.services.ItemDAO;
import jakarta.inject.Inject;
import java.util.List;
import utils.exception.UserInvalidException;

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
  public void changeItemCondition(int idItem, int userId, String state) {
    try {
      myDalServices.start();
      myItemDAOService.changeItemCondition(idItem, userId, state);
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

  @Override
  public void offerAgain(int itemId, int userId) {
    try {
      myDalServices.start();
      myDateDAOService.addDate(itemId);
      myItemDAOService.changeItemCondition(itemId, userId, "offered");
      myDalServices.commit();
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }

  }

  @Override
  public void itemCollectedOrNot(int itemId, boolean itemCollected, int reqUserId) {
    try {
      myDalServices.start();
      ItemDTO itemDTO = myItemDAOService.getOneById(itemId);
      if (reqUserId != itemDTO.getOfferorId()) {
        throw new UserInvalidException(
            "la personne essayant de faire la requête n'est pas l'offereur de l'objet");
      }
      myItemDAOService.itemCollectedOrNot(itemDTO, itemCollected);
      myDalServices.commit();
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }
  }


  @Override
  public List<ItemDTO> memberItemsByItemCondition(String itemCondition, int userId,
      boolean isOfferor) {
    try {
      myDalServices.start();
      var myItems = myItemDAOService.memberItemsByItemCondition(itemCondition, userId, isOfferor);
      myDalServices.commit();
      return myItems;
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }
  }
  @Override
  public void updateItemUrl(int itemId,String urlImg) {
    try {
      myDalServices.start();
      var item = myItemDAOService.getOneById(itemId);
      item.setUrlPicture(urlImg);
      myItemDAOService.updateItem(item);
      myDalServices.commit();
    }catch (Exception e){
      myDalServices.rollBack();
      throw e;
    }
  }

}
