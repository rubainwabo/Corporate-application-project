package be.vinci.pae.buiseness.ucc;

import be.vinci.pae.buiseness.dto.ItemTypeDTO;
import be.vinci.pae.dal.DalServices;
import be.vinci.pae.dal.services.ItemTypeDAO;
import jakarta.inject.Inject;
import java.util.List;

public class ItemTypeUCCImpl implements ItemTypeUCC {

  @Inject
  private ItemTypeDAO myItemTypeDAOService;

  @Inject
  private DalServices myDalServices;

  @Override
  public ItemTypeDTO addItemType(String itemType) {
    try {
      myDalServices.start();
      ItemTypeDTO val = myItemTypeDAOService.addItemType(itemType);
      myDalServices.commit();
      return val;
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }
  }

  @Override
  public List<ItemTypeDTO> getAllItemType() {
    try {
      myDalServices.start();
      var listeItemType = myItemTypeDAOService.getAllItemType();
      myDalServices.commit();
      return listeItemType;
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }
  }
}
