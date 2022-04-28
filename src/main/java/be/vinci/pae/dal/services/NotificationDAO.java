package be.vinci.pae.dal.services;

import be.vinci.pae.buiseness.dto.NotificationDTO;
import java.util.List;

public interface NotificationDAO {

  List<NotificationDTO> getAllMyNotif(int userId, boolean allNotif);

  void updateAllNotifToViewed(int userId);
}
