package be.vinci.pae.buiseness.ucc;

import be.vinci.pae.buiseness.dto.NotificationDTO;
import java.util.List;

public interface NotificationUCC {

  List<NotificationDTO> getAllMyNotif(int userId, boolean allNotif);

  void updateAllNotifToViewed(int userId);
}
