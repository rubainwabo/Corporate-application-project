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
  public ItemTypeDTO addItemType(String itemType) {
    try {
      myDalServices.start(true);
      ItemTypeDTO val = myItemTypeDAOService.addItemType(itemType);
      myDalServices.commit(true);
      return val;
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
  public List<ItemTypeDTO> getAllItemType() {
    try {
      myDalServices.start(false);
      var listeItemType = myItemTypeDAOService.getAllItemType();
      myDalServices.commit(false);
      return listeItemType;
    } catch (Exception e) {
      try {
        myDalServices.commit(false);
      } catch (Exception ex) {
        throw new BizzException(ex);
      }
      throw new BizzException(e);
    }
  }
}
