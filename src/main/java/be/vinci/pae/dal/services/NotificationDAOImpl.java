package be.vinci.pae.dal.services;

import be.vinci.pae.buiseness.dto.NotificationDTO;
import be.vinci.pae.buiseness.factory.BizFactory;
import be.vinci.pae.dal.DalBackService;
import be.vinci.pae.utils.exception.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAOImpl implements NotificationDAO {

  @Inject
  private BizFactory myDomainFactory;

  @Inject
  private DalBackService myDalService;

  @Override
  public List<NotificationDTO> getAllMyNotif(int userId) {
    try (PreparedStatement ps = myDalService.getPreparedStatement(
        "select n.id_notification,n.text,n.item,i.description,i.url_picture,it.item_type_name"
            + " from projet.notifications n,"
            + "projet.items i,projet.item_type it where person=" + userId +
            " and n.is_viewed=false and n.item=i.id_item and i.item_type="
            + "it.id_item_type")) {
      try (ResultSet rs = ps.executeQuery()) {
        ArrayList<NotificationDTO> notificationDTOS = new ArrayList<>();
        while (rs.next()) {
          var myNotif = myDomainFactory.getNotif();
          myNotif.setIdNotification(rs.getInt(1));
          myNotif.setTxt(rs.getString(2));
          myNotif.setItemId(rs.getInt(3));
          myNotif.setItemDescritpion(rs.getString(4));
          myNotif.setUrl_picture(rs.getString(5));
          myNotif.setItemType(rs.getString(6));
          myNotif.setMemberId(userId);
          notificationDTOS.add(myNotif);
        }
        return notificationDTOS;
      }
    } catch (Exception e) {
      throw new FatalException(e);
    }
  }
}
