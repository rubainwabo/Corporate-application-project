package buiseness.ucc;

import dal.DalServices;
import dal.services.ItemTypeDAO;
import jakarta.inject.Inject;

public class ItemTypeUCCImpl implements ItemTypeUCC {

  @Inject
  private ItemTypeDAO myItemTypeDAOService;

  @Inject
  private DalServices dal;

  @Override
  public int addItemType(String itemType) {
    return myItemTypeDAOService.addItemType(itemType);
  }
}
