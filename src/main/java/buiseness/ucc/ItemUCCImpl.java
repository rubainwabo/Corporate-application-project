package buiseness.ucc;

import buiseness.domain.bizclass.ItemType;
import buiseness.domain.dto.ItemDTO;
import buiseness.domain.dto.UserDTO;
import buiseness.factory.BizFactory;
import dal.DalServices;
import dal.services.ItemDAO;
import dal.services.ItemTypeDAO;
import dal.services.UserDAO;
import jakarta.inject.Inject;

public class ItemUCCImpl implements ItemUCC {

  @Inject
  private DalServices myDalService;

  @Inject
  private ItemDAO myItemDAOService;

  @Inject
  private BizFactory myBizFactoService;

  @Inject
  private UserDAO myUserDAOService;

  @Inject
  private ItemTypeDAO myItemTypeDAOService;

  @Override
  public int addItem(ItemDTO item, int userId) {
    UserDTO user = myUserDAOService.getOneById(userId);
    if (user.getId() <= 0) {
      throw new IllegalArgumentException("user inexistant");
    }
    int itemId = -1;
    var myItemType = (ItemType) myBizFactoService.getItemType();
    myItemType.setItemTypeName(item.getItemtype());
    try {
      myDalService.start();
      if (!myItemType.typeExist()) {
        if (myItemTypeDAOService.addItemType(item.getItemtype()) == -1) {
          throw new IllegalArgumentException("itemType n'a pas pu être ajouté");
        }
      }
      itemId = myItemDAOService.addItem(item, userId);
      if (itemId <= 0) {
        throw new IllegalArgumentException("probleme dans itemDAOImpl");
      }
    } catch (Exception e) {
      myDalService.rollBack();
      e.printStackTrace();
    }
    myDalService.commit();
    return itemId;
  }
}
