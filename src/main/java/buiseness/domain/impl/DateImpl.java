package buiseness.domain.impl;

import buiseness.domain.bizclass.Date;
import buiseness.domain.dto.DateDTO;
import buiseness.domain.dto.ItemDTO;

public class DateImpl implements Date {

  private int idDate;
  private DateDTO date;
  private ItemDTO item;

  public DateImpl() {
  }

  @Override
  public int getIdDate() {
    return idDate;
  }

  @Override
  public void setIdDate(int idDate) {
    this.idDate = idDate;
  }

  @Override
  public DateDTO getDate() {
    return date;
  }

  @Override
  public void setDate(DateDTO date) {
    this.date = date;
  }

  @Override
  public ItemDTO getItem() {
    return item;
  }

  @Override
  public void setItem(ItemDTO item) {
    this.item = item;
  }
}