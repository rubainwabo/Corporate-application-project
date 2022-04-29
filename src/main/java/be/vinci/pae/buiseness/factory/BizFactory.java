package be.vinci.pae.buiseness.factory;

import be.vinci.pae.buiseness.dto.DateDTO;
import be.vinci.pae.buiseness.dto.ItemDTO;
import be.vinci.pae.buiseness.dto.ItemTypeDTO;
import be.vinci.pae.buiseness.dto.NotificationDTO;
import be.vinci.pae.buiseness.dto.UserDTO;


public interface BizFactory {

  /**
   * creates an empty user.
   *
   * @return an empty user
   */
  UserDTO getUser();

  /**
   * creates an empty item.
   *
   * @return an empty item
   */
  ItemDTO getItem();

  /**
   * creates an empty itemType.
   *
   * @return an empty itemType
   */
  ItemTypeDTO getItemType();

  DateDTO getDate();

  NotificationDTO getNotif();
}
