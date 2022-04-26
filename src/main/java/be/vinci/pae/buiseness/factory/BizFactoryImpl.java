package be.vinci.pae.buiseness.factory;

import be.vinci.pae.buiseness.dto.DateDTO;
import be.vinci.pae.buiseness.dto.ItemDTO;
import be.vinci.pae.buiseness.dto.ItemTypeDTO;
import be.vinci.pae.buiseness.dto.NotificationDTO;
import be.vinci.pae.buiseness.dto.UserDTO;
import be.vinci.pae.buiseness.impl.DateImpl;
import be.vinci.pae.buiseness.impl.ItemImpl;
import be.vinci.pae.buiseness.impl.ItemTypeImpl;
import be.vinci.pae.buiseness.impl.NotificationImpl;
import be.vinci.pae.buiseness.impl.UserImpl;

public class BizFactoryImpl implements BizFactory {

  @Override
  public UserDTO getUser() {
    return new UserImpl();
  }

  @Override
  public ItemDTO getItem() {
    return new ItemImpl();
  }

  @Override
  public ItemTypeDTO getItemType() {
    return new ItemTypeImpl();
  }

  @Override
  public DateDTO getDate() {
    return new DateImpl();
  }

  @Override
  public NotificationDTO getNotif() {
    return new NotificationImpl();
  }

}