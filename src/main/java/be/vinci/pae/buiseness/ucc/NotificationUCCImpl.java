package be.vinci.pae.buiseness.ucc;

import be.vinci.pae.buiseness.dto.NotificationDTO;
import be.vinci.pae.dal.DalServices;
import be.vinci.pae.dal.services.NotificationDAO;
import jakarta.inject.Inject;
import java.util.List;

public class NotificationUCCImpl implements NotificationUCC {

  @Inject
  private NotificationDAO myNotifDaoService;

  @Inject
  private DalServices myDalServices;

  @Override
  public List<NotificationDTO> getAllMyNotif(int userId, boolean allNotif) {
    try {
      myDalServices.start();
      var notificationDTOList = myNotifDaoService.getAllMyNotif(userId, allNotif);
      myDalServices.commit();
      return notificationDTOList;
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }
  }

  @Override
  public void updateAllNotifToViewed(int userId) {
    try {
      myDalServices.start();
      myNotifDaoService.updateAllNotifToViewed(userId);
      myDalServices.commit();
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }
  }
}
