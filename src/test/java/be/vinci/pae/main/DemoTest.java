package be.vinci.pae.main;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import buiseness.domain.User;
import buiseness.ucc.UserUCC;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dal.services.UserDAO;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DemoTest {

  private static UserUCC userUCC;
  private static UserDAO userDAO;
  private static User user;
  private static ObjectNode fakeObject;

  @BeforeAll
  public static void setup() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinder());
    userDAO = locator.getService(UserDAO.class);
    userUCC = locator.getService(UserUCC.class);
    user = mock(User.class);
    fakeObject = mock(ObjectNode.class);
  }

  @Test
  public void userDAOTest() {
    when(userDAO.getOne("test")).thenReturn(user);
    assertNotNull(userDAO.getOne("test"));
    verify(userDAO).getOne("test");
  }

  @Test
  public void userUCCTest() {
    when(userUCC.login("test", "test", true)).thenReturn(fakeObject);
    assertNotNull(userUCC.login("test", "test", true));
    verify(userUCC).login("test", "test", true);
  }
}
