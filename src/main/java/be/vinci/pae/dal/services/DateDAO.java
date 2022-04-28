package be.vinci.pae.dal.services;

import be.vinci.pae.buiseness.dto.DateDTO;
import java.util.List;

public interface DateDAO {

  /**
   * add the date to the table dates.
   *
   * @param itemId the itemId
   */
  void addDate(int itemId);

  List<DateDTO> getAllDateItem(int itemId);

}
