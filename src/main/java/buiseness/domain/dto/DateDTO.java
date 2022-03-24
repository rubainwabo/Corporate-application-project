package buiseness.domain.dto;

public interface DateDTO {

  /**
   * retrieve the id date.
   *
   * @return date id
   */
  int getIdDate();

  /**
   * changes the id date in parameter.
   *
   * @param idDate
   */
  void setIdDate(int idDate);

  /**
   * retrieve the date.
   *
   * @return the date
   */
  DateDTO getDate();

  /**
   * changes the date in parameter.
   *
   * @param date
   */
  void setDate(DateDTO date);

  /**
   * retrieve the id item of the date.
   *
   * @return id item
   */
  ItemDTO getItem();

  /**
   * changes the item into the item in parameter.
   *
   * @param item
   */
  void setItem(ItemDTO item);
}
