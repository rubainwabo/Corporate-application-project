package be.vinci.pae.buiseness.dto;

public interface NotificationDTO {

  int getIdNotification();

  void setIdNotification(int idNotification);

  boolean isIs_viewed();

  void setIs_viewed(boolean is_viewed);

  String getTxt();

  void setTxt(String txt);

  int getMemberId();

  void setMemberId(int memberId);

  int getItemId();

  void setItemId(int itemId);

  String getUrl_picture();

  void setUrl_picture(String url_picture);

  String getItemType();

  void setItemType(String itemType);

  String getItemDescritpion();

  void setItemDescritpion(String itemDescritpion);
}
