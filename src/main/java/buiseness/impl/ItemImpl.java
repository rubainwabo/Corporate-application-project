package buiseness.impl;

import buiseness.domain.Item;

public class ItemImpl implements Item {

  private int id;
  private String description;
  private String urlPicture;
  private int rating;
  private String comment;
  private String itemCondition;
  private String timeSlot;
  private String offeror;
  private int recipient;
  private String itemType;
  private int numberOfPeopleInterested;

  public ItemImpl() {
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
  public int getRecipient() {
    return recipient;
  }

  @Override
  public void setRecipient(int recipient) {
    this.recipient = recipient;
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
