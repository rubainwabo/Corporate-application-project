package be.vinci.pae.main;

import buiseness.domain.bizclass.User;
import buiseness.ucc.UserUCC;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dal.services.UserDAO;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import utils.TokenService;

public class UserUCCTest {

  private static final String USER = "user";
  private static final String DENIED = "denied";
  private static final String MESSAGE = "username or password incorrect";
  private static final String PASSWORD = "password";
  private static final String WAITING = "waiting";
  private static final String TOKEN = "token";
  private static final String INVALID_TOKEN = "invalid token";
  private static final int ID = 1;

  private static UserUCC userUCC;
  private static ServiceLocator locator;
  private TokenService tokenService;
  private UserDAO userDAO;
  private User userWrongPassword;
  private User userDenied;
  private User userWaiting;
  private User validUser;
  private ObjectNode objectNode;

  @BeforeAll
  public static void init() {
    locator = ServiceLocatorUtilities.bind(new TestApplicationBinder());
    userUCC = locator.getService(UserUCC.class);
  }

  /**
   * setup all the service before each test.
   */
  @BeforeEach
  public void setup() {
    userDAO = locator.getService(UserDAO.class);
    Mockito.clearInvocations(userDAO);

    tokenService = locator.getService(TokenService.class);
    Mockito.clearInvocations(tokenService);
  }

  /*@Test
  public void loginExceptionUserInvalid() {
    Throwable exception = Assertions.assertThrows(Exception.class,
        () -> userUCC.login(null, "test", true));
    Assertions.assertEquals(MESSAGE, exception.getMessage());
  }*/

  @Test
  public void loginExceptionWrongPassword() {

    userWrongPassword = Mockito.mock(User.class);

    Mockito.when(userWrongPassword.checkPassword(PASSWORD)).thenReturn(false);
    Mockito.when(userDAO.getOneByUsername(USER)).thenReturn(userWrongPassword);

    Throwable exception = Assertions.assertThrows(Exception.class,
        () -> userUCC.login(USER, PASSWORD, true));

    Assertions.assertAll(
        () -> Assertions.assertEquals(MESSAGE, exception.getMessage()),
        () -> Mockito.verify(userDAO).getOneByUsername(USER));
  }

  @Test
  public void loginExceptionUserDenied() {

    userDenied = Mockito.mock(User.class);

    Mockito.when(userDenied.getReasonForConnectionRefusal()).thenReturn("You are not recognized");
    Mockito.when(userDenied.checkPassword(PASSWORD)).thenReturn(true);
    Mockito.when(userDenied.getState()).thenReturn(DENIED);
    Mockito.when(userDenied.isDenied()).thenReturn(true);
    Mockito.when(userDAO.getOneByUsername(USER)).thenReturn(userDenied);

    Throwable exception = Assertions.assertThrows(Exception.class,
        () -> userUCC.login(USER, PASSWORD, true));

    Assertions.assertAll(
        () -> Assertions.assertEquals(userDenied.getReasonForConnectionRefusal(),
            exception.getMessage()),
        () -> Mockito.verify(userDenied, Mockito.atMost(2)).getReasonForConnectionRefusal(),
        () -> Mockito.verify(userDenied).checkPassword(PASSWORD),
        () -> Mockito.verify(userDenied, Mockito.atMost(2)).getState(),
        () -> Mockito.verify(userDenied).isDenied(),
        () -> Mockito.verify(userDAO).getOneByUsername(USER));
  }

  @Test
  public void loginExceptionUserWaiting() {

    userWaiting = Mockito.mock(User.class);

    Mockito.when(userWaiting.checkPassword(PASSWORD)).thenReturn(true);
    Mockito.when(userWaiting.getState()).thenReturn(WAITING);
    Mockito.when(userWaiting.isWaiting()).thenReturn(true);
    Mockito.when(userDAO.getOneByUsername(USER)).thenReturn(userWaiting);

    Throwable exception = Assertions.assertThrows(Exception.class,
        () -> userUCC.login(USER, PASSWORD, true));

    Assertions.assertAll(
        () -> Assertions.assertEquals("user on hold", exception.getMessage()),
        () -> Mockito.verify(userWaiting).checkPassword(PASSWORD),
        () -> Mockito.verify(userWaiting, Mockito.atMost(3)).getState(),
        () -> Mockito.verify(userWaiting).isWaiting(),
        () -> Mockito.verify(userDAO).getOneByUsername(USER));
  }

  @Test
  public void loginSucceed() {

    objectNode = Mockito.mock(ObjectNode.class);
    validUser = Mockito.mock(User.class);

    Mockito.when(validUser.getUserName()).thenReturn(USER);
    Mockito.when(validUser.getState()).thenReturn("valid");
    Mockito.when(validUser.getPassword()).thenReturn(PASSWORD);
    Mockito.when(validUser.checkPassword(validUser.getPassword())).thenReturn(true);
    Mockito.when(userDAO.getOneByUsername(validUser.getUserName())).thenReturn(validUser);
    Mockito.when(tokenService.login(validUser.getId(), validUser.getUserName(), true))
        .thenReturn(objectNode);

    Assertions.assertAll(
        () -> Assertions.assertEquals(objectNode,
            userUCC.login(validUser.getUserName(), validUser.getPassword(), true)),
        () -> Mockito.verify(validUser, Mockito.atMost(3)).getUserName(),
        () -> Mockito.verify(validUser, Mockito.atMost(4)).getState(),
        () -> Mockito.verify(validUser, Mockito.atMost(3)).getPassword(),
        () -> Mockito.verify(validUser, Mockito.atMost(2)).checkPassword(PASSWORD),
        () -> Mockito.verify(userDAO).getOneByUsername(USER),
        () -> Mockito.verify(tokenService).login(validUser.getId(), validUser.getUserName(), true));
  }

  @Test
  public void tokenRefreshExceptionOne() {

    Mockito.when(tokenService.isJWT(TOKEN)).thenReturn(false);

    Throwable exception = Assertions.assertThrows(Exception.class,
        () -> userUCC.refreshToken(TOKEN));

    Assertions.assertAll(
        () -> Assertions.assertEquals(INVALID_TOKEN, exception.getMessage()),
        () -> Mockito.verify(tokenService).isJWT(TOKEN)
    );
  }

  @Test
  public void tokenRefreshExceptionSec() {

    Mockito.when(tokenService.isJWT(TOKEN)).thenReturn(true);
    Mockito.when(tokenService.verifyRefreshToken(TOKEN)).thenReturn(false);

    Throwable exception = Assertions.assertThrows(Exception.class,
        () -> userUCC.refreshToken(TOKEN));

    Assertions.assertAll(
        () -> Assertions.assertEquals(INVALID_TOKEN, exception.getMessage()),
        () -> Mockito.verify(tokenService).isJWT(TOKEN),
        () -> Mockito.verify(tokenService).verifyRefreshToken(TOKEN)
    );
  }

  @Test
  public void tokenRefreshSucceed() {

    objectNode = Mockito.mock(ObjectNode.class);

    Mockito.when(tokenService.isJWT(TOKEN)).thenReturn(true);
    Mockito.when(tokenService.verifyRefreshToken(TOKEN)).thenReturn(true);
    Mockito.when(tokenService.getUserId(TOKEN)).thenReturn(ID);
    Mockito.when(tokenService.getRefreshedTokens(ID))
        .thenReturn(objectNode);

    Assertions.assertAll(
        () -> Assertions.assertEquals(objectNode, userUCC.refreshToken(TOKEN)),
        () -> Mockito.verify(tokenService).isJWT(TOKEN),
        () -> Mockito.verify(tokenService).verifyRefreshToken(TOKEN),
        () -> Mockito.verify(tokenService).getRefreshedTokens(ID)
    );
  }

}
