package buiseness.ucc;

import dal.DalServices;
import dal.services.ItemTypeDAO;
import jakarta.inject.Inject;
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
      try {
        myDalServices.rollBack();
      } catch (Exception ex) {
        throw new BizzException(ex);
      }
      throw new BizzException(e);
    }
  }
}
