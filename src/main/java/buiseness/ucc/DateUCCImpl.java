package buiseness.ucc;

import buiseness.dto.DateDTO;
import dal.DalServices;
import dal.services.DateDAO;
import jakarta.inject.Inject;
import java.util.List;
import utils.exception.BizzException;

public class DateUCCImpl implements DateUCC {

  @Inject
  private DateDAO myDateDAOService;
  @Inject
  private DalServices myDalServices;

  @Override
  public List<DateDTO> getAllDateItem(int itemId) {
    try {
      myDalServices.start(false);
      var list = myDateDAOService.getAllDateItem(itemId);
      myDalServices.commit(false);
      return list;
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
