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
      int itemId = -1;
      itemId = myItemDAOService.addItem(item, userId);
      if (itemId <= 0) {
        myDalServices.rollBack();
        throw new BizzException("Echec lors de l'insertion : addItemItemUCC");
      }
      myDateDAOService.addDate(itemId);
      myDalServices.commit(true);
      return itemId;
    } catch (Exception e) {
      myDalServices.rollBack();
      throw new BizzException("Erreur lors de la connexion à la db");
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
      myDalServices.commit(false);
      throw new BizzException("Erreur lors de la connexion à la db");
    }
  }

  @Override
  public void addInterest(int itemId, ObjectNode objectNode, int userId) {
    try {
      myDalServices.start(true);
      myItemDAOService.addInterest(itemId, objectNode, userId);
      myDalServices.commit(true);
    } catch (Exception e) {
      myDalServices.rollBack();
      throw new BizzException("Erreur lors de la connexion à la db");
    }
  }

  @Override
  public void cancelOffer(int idItem, int userId) {
    try {
      myDalServices.start(true);
      myItemDAOService.cancelOffer(idItem, userId);
      myDalServices.commit(true);
    } catch (Exception e) {
      myDalServices.rollBack();
      throw new BizzException("Erreur lors de la connexion à la db");
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
      myDalServices.commit(false);
      throw new BizzException("Erreur lors de la connexion à la db");
    }
  }
}
