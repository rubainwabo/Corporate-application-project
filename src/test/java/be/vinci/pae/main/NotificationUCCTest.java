package be.vinci.pae.main;

import be.vinci.pae.buiseness.dto.NotificationDTO;
import be.vinci.pae.buiseness.ucc.NotificationUCC;
import be.vinci.pae.dal.services.NotificationDAO;
import be.vinci.pae.utils.exception.FatalException;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class NotificationUCCTest {

  private static final int ID = 1;
  private static NotificationUCC notificationUCC;
  private static ServiceLocator locator;
  private static NotificationDAO notificationDAO;

  @BeforeAll
  public static void init() {
    locator = ServiceLocatorUtilities.bind(new TestApplicationBinder());
    notificationUCC = locator.getService(NotificationUCC.class);
  }

  @BeforeEach
  public void setup() {
    notificationDAO = locator.getService(NotificationDAO.class);
    Mockito.clearInvocations(notificationDAO);
    Mockito.reset(notificationDAO);
  }

  @Test
  public void getAllMyNotifSuccessful() {
    List<NotificationDTO> list = Mockito.mock(List.class);

    Mockito.when(notificationDAO.getAllMyNotif(ID, true)).thenReturn(list);
    Assertions.assertEquals(list, notificationUCC.getAllMyNotif(ID, true));
    Mockito.verify(notificationDAO).getAllMyNotif(ID, true);
  }

  @Test
  public void getAllMyNotifFatalException() {
    Mockito.when(notificationDAO.getAllMyNotif(ID, true)).thenThrow(FatalException.class);
    Assertions.assertThrows(FatalException.class, () -> notificationUCC.getAllMyNotif(ID, true));
    Mockito.verify(notificationDAO).getAllMyNotif(ID, true);
  }

  @Test
  public void updateAllNotifToViewedSuccessful() {
    notificationUCC.updateAllNotifToViewed(ID);
    Mockito.verify(notificationDAO).updateAllNotifToViewed(ID);
  }

  @Test
  public void updateAllNotifToViewedFatalException() {
    Mockito.doThrow(FatalException.class).when(notificationDAO).updateAllNotifToViewed(ID);
    Assertions.assertThrows(FatalException.class, () -> notificationUCC.updateAllNotifToViewed(ID));
    Mockito.verify(notificationDAO).updateAllNotifToViewed(ID);
  }
}
