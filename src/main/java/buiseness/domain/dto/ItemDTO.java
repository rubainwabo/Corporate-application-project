package buiseness.domain.dto;

import buiseness.domain.impl.ItemImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = ItemImpl.class)
public interface ItemDTO {

  int getId();

  void setId(int id);

  String getDescription();

  void setDescription(String description);

  String getUrlPicture();

  void setUrlPicture(String urlPicture);

  int getRating();

  void setRating(int rating);

  String getComment();

  void setComment(String comment);

  String getState();

  void setState(String state);

  DateDTO getTimeSlot();

  void setTimeSlot(DateDTO timeSlot);

  int getOfferor();

  void setOfferor(int offeror);

  int getRecipient();

  void setRecipient(int recipient);

  String getItemtype();

  void setItemtype(String itemType);
}
