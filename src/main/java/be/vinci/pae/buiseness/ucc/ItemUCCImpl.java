package be.vinci.pae.buiseness.ucc;

import be.vinci.pae.buiseness.dto.ItemDTO;
import be.vinci.pae.dal.DalServices;
import be.vinci.pae.dal.services.DateDAO;
import be.vinci.pae.dal.services.ItemDAO;
import be.vinci.pae.dal.services.NotificationDAO;
import be.vinci.pae.utils.exception.BizzException;
import be.vinci.pae.utils.exception.UserInvalidException;
import jakarta.inject.Inject;
import java.util.List;

public class ItemUCCImpl implements ItemUCC {

  @Inject
  private ItemDAO myItemDAOService;

  @Inject
  private DateDAO myDateDAOService;

  @Inject
  private DalServices myDalServices;

  @Inject
  private NotificationDAO myNotificationDAO;

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
      myDalServices.rollBack();
      throw e;
    }
  }

  @Override
  public List<ItemDTO> getItems(String filter, String input) {
    try {
      myDalServices.start();
      List<ItemDTO> list = myItemDAOService.getFiltered(filter, input);
      myDalServices.commit();
      return list;
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }
  }


  @Override
  public void addInterest(int itemId, int userId, boolean callMe,
      String phoneNumber, String availabilities, int version) {
    try {
      myDalServices.start();
      myItemDAOService.addInterest(itemId, userId, callMe, phoneNumber,
          availabilities, version);
      myDalServices.commit();
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }
  }

  @Override
  public void changeItemCondition(int itemId, int userId, String state, int version) {
    try {
      myDalServices.start();
      ItemDTO item = myItemDAOService.getOneById(itemId);
      if (item.getOfferorId() != userId) {
        throw new BizzException("vous n'avez pas le droit de modifier l'offre");
      }
      myItemDAOService.changeItemCondition(itemId, userId, state, version);
      myDalServices.commit();
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }
  }

  @Override
  public List<ItemDTO> getMyItems(int id, String state, int type, boolean mine) {
    try {
      myDalServices.start();
      List<ItemDTO> list = myItemDAOService.getMyItems(id, state, type, mine);
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
  public int addRecipient(int idItem, int idRecipient, int version) {
    try {
      myDalServices.start();

      int itemId = myItemDAOService.addRecipient(idItem, idRecipient, version);
      myDalServices.commit();
      return itemId;
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }

  }

  @Override
  public int updateItem(ItemDTO item, int userId) {
    try {
      myDalServices.start();
      if (item.getOfferorId() != userId) {
        throw new BizzException("vous n'avez pas le droit de faire cette action");
      }
      int result = myItemDAOService.updateItem(item);
      myDalServices.commit();
      return result;
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }

  }

  @Override
  public void offerAgain(int itemId, int userId, int version) {
    try {
      myDalServices.start();
      ItemDTO item = myItemDAOService.getOneById(itemId);
      if (item.getOfferorId() != userId) {
        throw new BizzException("vous n'avez pas le droit de modifier l'offre");
      }
      myDateDAOService.addDate(itemId);
      myItemDAOService.changeItemCondition(itemId, userId, "offered", version);
      
      if (item.getRecipientId() > 0) {
        myNotificationDAO.sendNotification(item.getOfferor() + " a réoffert cet objet",
            item.getRecipientId(), item.getId());
      }

      myDalServices.commit();
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }

  }

  @Override
  public void itemCollectedOrNot(int itemId, boolean itemCollected, int reqUserId, int version) {
    try {
      myDalServices.start();
      ItemDTO itemDTO = myItemDAOService.getOneById(itemId);
      if (reqUserId != itemDTO.getOfferorId()) {
        throw new UserInvalidException(
            "la personne essayant de faire la requête n'est pas l'offereur de l'objet");
      }
      myItemDAOService.itemCollectedOrNot(itemDTO, itemCollected, version);
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
  public void updateItemUrl(int itemId, String urlImg) {
    try {
      myDalServices.start();
      var item = myItemDAOService.getOneById(itemId);
      item.setUrlPicture(urlImg);
      myItemDAOService.updateItem(item);
      myDalServices.commit();
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }
  }

  @Override
  public void rateItem(int itemId, int nbStars, String comment, int version) {
    try {
      myDalServices.start();
      myItemDAOService.rateItem(itemId, nbStars, comment, version);
      myDalServices.commit();
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }
  }

  @Override
  public void updateItemOfInvalidMember(int memberId) {
    try {
      myDalServices.start();
      myItemDAOService.updateItemOfInvalidMember(memberId);
      myDalServices.commit();
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }
  }
}
