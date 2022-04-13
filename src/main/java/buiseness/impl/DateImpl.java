package buiseness.impl;

import buiseness.domain.Date;
import buiseness.dto.DateDTO;

public class DateImpl implements Date {

  private int idDate;
  private DateDTO date;
  private int item;

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
  public int getItem() {
    return item;
  }

  @Override
  public void setItem(int item) {
    this.item = item;
  }
}