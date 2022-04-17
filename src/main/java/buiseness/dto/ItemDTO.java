package buiseness.dto;

import buiseness.impl.ItemImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.sql.Timestamp;

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

  String getItemCondition();

  void setItemCondition(String itemCondition);

  String getTimeSlot();

  void setTimeSlot(String timeSlot);

  String getOfferor();

  void setOfferor(String offeror);

  int getRecipient();

  void setRecipient(int recipient);

  String getItemtype();

  void setItemtype(String itemType);

  int getNumberOfPeopleInterested();

  int setNumberOfPeopleInterested(int numberOfPeopleInterested);

  Timestamp getLastDateOffered();

  void setLastDateOffered(Timestamp lastDateOffered);
}
