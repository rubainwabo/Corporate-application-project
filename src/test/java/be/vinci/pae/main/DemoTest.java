package be.vinci.pae.main;

import buiseness.domain.User;
import buiseness.ucc.UserUCC;
import dal.services.UserDAO;
import jakarta.ws.rs.WebApplicationException;
import java.lang.reflect.Field;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import utils.TokenService;

@ExtendWith(MockitoExtension.class)
public class DemoTest {

  private static UserUCC userUCC;
  private static UserDAO userDAO;
  private static TokenService tokenService;
  private User userWrongPassword;
  private User userDenied;


  @BeforeAll
  public static void init() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinder());
    userDAO = locator.getService(UserDAO.class);
    tokenService = locator.getService(TokenService.class);
    userUCC = locator.getService(UserUCC.class);

    try {
      Field myUserDAO = userUCC.getClass().getDeclaredField("myUserDAO");
      myUserDAO.setAccessible(true);
      myUserDAO.set(userUCC, userDAO);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @BeforeEach
  public void setup() {

  }

  @Test
  public void loginExceptionUserNull() {
    WebApplicationException exception = Assertions.assertThrows(WebApplicationException.class,
        () -> userUCC.login(null, "test", true));
    Assertions.assertEquals("username or password incorrect",
        exception.getResponse().getEntity().toString());
  }

  @Test
  public void loginExceptionWrongPassword() {
    userWrongPassword = Mockito.mock(User.class);
    Mockito.when(userWrongPassword.checkPassword("password")).thenReturn(false);
    Mockito.when(userDAO.getOneByUsername("user")).thenReturn(userWrongPassword);
    WebApplicationException exception = Assertions.assertThrows(WebApplicationException.class,
        () -> userUCC.login("user", "password", true));
    Assertions.assertEquals("username or password incorrect",
        exception.getResponse().getEntity().toString());
  }

  @Test
  public void loginExceptionUserDenied() {
    userDenied = Mockito.mock(User.class);
    Mockito.when(userDenied.getReasonForConnectionRefusal()).thenReturn("You are not recognized");
    Mockito.when(userDenied.checkPassword("password")).thenReturn(true);
    Mockito.when(userDenied.getState()).thenReturn("denied");
    Mockito.when(userDenied.isDenied(userDenied.getState())).thenReturn(true);
    Mockito.when(userDAO.getOneByUsername("user")).thenReturn(userDenied);
    WebApplicationException exception = Assertions.assertThrows(WebApplicationException.class,
        () -> userUCC.login("user", "password", true));
    Assertions.assertEquals(userDenied.getReasonForConnectionRefusal(),
        exception.getResponse().getEntity().toString());
  }

  @Test
  public void loginExceptionUserWaiting() {
    userDenied = Mockito.mock(User.class);
    Mockito.when(userDenied.checkPassword("password")).thenReturn(true);
    Mockito.when(userDenied.getState()).thenReturn("waiting");
    Mockito.when(userDenied.isWaiting(userDenied.getState())).thenReturn(true);
    Mockito.when(userDAO.getOneByUsername("user")).thenReturn(userDenied);
    WebApplicationException exception = Assertions.assertThrows(WebApplicationException.class,
        () -> userUCC.login("user", "password", true));
    Assertions.assertEquals("user on hold", exception.getResponse().getEntity().toString());
  }

}
