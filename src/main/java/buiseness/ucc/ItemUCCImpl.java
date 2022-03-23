package buiseness.ucc;

import buiseness.domain.bizclass.ItemType;
import buiseness.domain.dto.ItemDTO;
import buiseness.factory.BizFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dal.DalServices;
import dal.services.DateDAO;
import dal.services.ItemDAO;
import dal.services.ItemTypeDAO;
import jakarta.inject.Inject;

public class ItemUCCImpl implements ItemUCC {

  @Inject
  private DalServices myDalService;

  @Inject
  private ItemDAO myItemDAOService;

  @Inject
  private BizFactory myBizFactoService;

  @Inject
  private ItemTypeDAO myItemTypeDAOService;

  @Inject
  private DateDAO myDateDAOService;

  @Override
  public int addItem(ItemDTO item, int userId) {
    int itemId = -1;
    var myItemType = (ItemType) myBizFactoService.getItemType();
    myItemType.setItemTypeName(item.getItemtype());
    itemId = myItemDAOService.addItem(item, userId);
    if (itemId <= 0) {
      throw new IllegalArgumentException("fail insert in db : addItemItemUCC");
    }
    myDateDAOService.addDate(itemId);
    return itemId;
  }

  @Override
  public ItemDTO getDetails(int id) {
    return myItemDAOService.getOneById(id);
  }

  @Override
  public void addInterest(int itemId, ObjectNode objectNode, int userId) {
    myItemDAOService.addInterest(itemId, objectNode, userId);
  }

  @Override
  public void cancelOffer(int idItem, int userId) {
    myItemDAOService.cancelOffer(idItem, userId);
  }
}
