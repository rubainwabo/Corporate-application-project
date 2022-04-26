package be.vinci.pae.buiseness.impl;

import be.vinci.pae.buiseness.dto.NotificationDTO;

public class NotificationImpl implements NotificationDTO {

  private int idNotification;
  private boolean is_viewed;
  private String txt;
  private int memberId;
  private int itemId;
  private String url_picture;
  private String itemType;
  private String itemDescritpion;

  @Override
  public String getUrl_picture() {
    return url_picture;
  }

  @Override
  public void setUrl_picture(String url_picture) {
    this.url_picture = url_picture;
  }

  @Override
  public String getItemType() {
    return itemType;
  }

  @Override
  public void setItemType(String itemType) {
    this.itemType = itemType;
  }

  @Override
  public String getItemDescritpion() {
    return itemDescritpion;
  }

  @Override
  public void setItemDescritpion(String itemDescritpion) {
    this.itemDescritpion = itemDescritpion;
  }

  @Override
  public int getIdNotification() {
    return idNotification;
  }

  @Override
  public void setIdNotification(int idNotification) {
    this.idNotification = idNotification;
  }

  @Override
  public boolean isIs_viewed() {
    return is_viewed;
  }

  @Override
  public void setIs_viewed(boolean is_viewed) {
    this.is_viewed = is_viewed;
  }

  @Override
  public String getTxt() {
    return txt;
  }

  @Override
  public void setTxt(String txt) {
    this.txt = txt;
  }

  @Override
  public int getMemberId() {
    return memberId;
  }

  @Override
  public void setMemberId(int memberId) {
    this.memberId = memberId;
  }

  @Override
  public int getItemId() {
    return itemId;
  }

  @Override
  public void setItemId(int itemId) {
    this.itemId = itemId;
  }
}
