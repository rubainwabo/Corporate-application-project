package buiseness.ucc;

import buiseness.dto.ItemTypeDTO;
import dal.DalServices;
import dal.services.ItemTypeDAO;
import jakarta.inject.Inject;
import java.util.List;
import utils.exception.BizzException;

public class ItemTypeUCCImpl implements ItemTypeUCC {

  @Inject
  private ItemTypeDAO myItemTypeDAOService;

  @Inject
  private DalServices myDalServices;

  @Override
  public int addItemType(String itemType) {
    try {
      myDalServices.start(true);
      int val = myItemTypeDAOService.addItemType(itemType);
      myDalServices.commit(true);
      return val;
    } catch (Exception e) {
      myDalServices.rollBack();
      throw new BizzException("Echec de connexion à la db");
    }
  }

  @Override
  public List<ItemTypeDTO> getAllItemType() {
    try {
      myDalServices.start(false);
      var listeItemType = myItemTypeDAOService.getAllItemType();
      myDalServices.commit(false);
      return listeItemType;
    } catch (Exception e) {
      myDalServices.commit(false);
      throw new BizzException("Echec de connexion à la db");
    }
  }
}
