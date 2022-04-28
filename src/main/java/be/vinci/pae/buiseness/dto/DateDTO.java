package be.vinci.pae.buiseness.dto;

import java.sql.Timestamp;

public interface DateDTO {

  int getIdDate();

  void setIdDate(int idDate);

  Timestamp getDate();

  void setDate(Timestamp date);

  int getItem();

  void setItem(int item);
}
