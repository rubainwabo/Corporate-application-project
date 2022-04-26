package be.vinci.pae.buiseness.ucc;

import be.vinci.pae.buiseness.dto.NotificationDTO;
import be.vinci.pae.dal.DalServices;
import be.vinci.pae.dal.services.NotificationDAO;
import be.vinci.pae.utils.exception.UserInvalidException;
import jakarta.inject.Inject;
import java.util.List;

public class NotificationUCCImpl implements NotificationUCC {

  @Inject
  private NotificationDAO myNotifDaoService;

  @Inject
  private DalServices myDalServices;

  @Override
  public List<NotificationDTO> getAllMyNotif(int id, int userId) {
    if (id != userId) {
      throw new UserInvalidException(
          "la personne essayant de faire la requête ne peut pas accéder à cette ressource");
    }
    try {
      myDalServices.start();
      var notificationDTOList = myNotifDaoService.getAllMyNotif(userId);
      myDalServices.commit();
      return notificationDTOList;
    } catch (Exception e) {
      myDalServices.rollBack();
      throw e;
    }
  }
}
