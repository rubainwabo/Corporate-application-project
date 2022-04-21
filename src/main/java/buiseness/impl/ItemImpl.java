package buiseness.impl;

import buiseness.domain.Item;
import java.sql.Timestamp;

public class ItemImpl implements Item {

  private int id;
  private String description;
  private String urlPicture;
  private int rating;
  private String comment;
  private String itemCondition;
  private String timeSlot;
  private String offeror;
  private int offerorId;
  private String recipient;
  private int recipientId;
  private String itemType;
  private int numberOfPeopleInterested;
  private Timestamp lastDateOffered;

  public ItemImpl() {
  }

  @Override
  public Timestamp getLastDateOffered() {
    return lastDateOffered;
  }

  @Override
  public void setLastDateOffered(Timestamp lastDateOffered) {
    this.lastDateOffered = lastDateOffered;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String getUrlPicture() {
    return urlPicture;
  }

  @Override
  public void setUrlPicture(String urlPicture) {
    this.urlPicture = urlPicture;
  }

  @Override
  public int getRating() {
    return rating;
  }

  @Override
  public void setRating(int rating) {
    this.rating = rating;
  }

  @Override
  public String getComment() {
    return comment;
  }

  @Override
  public void setComment(String comment) {
    this.comment = comment;
  }

  @Override
  public String getItemCondition() {
    return itemCondition;
  }

  @Override
  public void setItemCondition(String itemCondition) {
    this.itemCondition = itemCondition;
  }

  @Override
  public String getTimeSlot() {
    return timeSlot;
  }

  @Override
  public void setTimeSlot(String timeSlot) {
    this.timeSlot = timeSlot;
  }

  @Override
  public String getOfferor() {
    return offeror;
  }

  @Override
  public void setOfferor(String offeror) {
    this.offeror = offeror;
  }

  @Override
  public String getRecipient() {
    return recipient;
  }

  @Override
  public void setRecipient(String recipient) {
    this.recipient = recipient;
  }

  @Override
  public int getOfferorId() {
    return offerorId;
  }

  @Override
  public void setOfferorId(int offerorId) {
    this.offerorId = offerorId;
  }

  @Override
  public int getRecipientId() {
    return recipientId;
  }

  @Override
  public void setRecipientId(int recipientId) {
    this.recipientId = recipientId;
  }

  @Override
  public String getItemtype() {
    return itemType;
  }

  @Override
  public void setItemtype(String itemType) {
    this.itemType = itemType;
  }

  @Override
  public int getNumberOfPeopleInterested() {
    return numberOfPeopleInterested;
  }

  @Override
  public int setNumberOfPeopleInterested(int numberOfPeopleInterested) {
    return this.numberOfPeopleInterested = numberOfPeopleInterested;
  }
}
