package buiseness.impl;

import buiseness.domain.Date;
import java.sql.Timestamp;

public class DateImpl implements Date {

  private int idDate;
  private Timestamp date;
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
  public Timestamp getDate() {
    return date;
  }

  @Override
  public void setDate(Timestamp date) {
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