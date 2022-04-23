package buiseness.ucc;

import buiseness.dto.DateDTO;
import dal.DalServices;
import dal.services.DateDAO;
import jakarta.inject.Inject;
import java.util.List;

public class DateUCCImpl implements DateUCC {

  @Inject
  private DateDAO myDateDAOService;
  @Inject
  private DalServices myDalServices;

  @Override
  public List<DateDTO> getAllDateItem(int itemId) {
    try {
      myDalServices.start();
      var list = myDateDAOService.getAllDateItem(itemId);
      myDalServices.commit();
      return list;
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }
  }
}
