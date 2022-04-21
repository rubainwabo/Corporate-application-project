package be.vinci.pae.main;

import buiseness.domain.User;
import buiseness.dto.UserDTO;
import buiseness.ucc.UserUCC;
import dal.services.UserDAO;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class UserUCCTest {

  // Constants
  private static final String USERNAME = "user";
  private static final String DENIED = "denied";
  private static final String MESSAGE = "username or password incorrect";
  private static final String PASSWORD = "password";
  private static final String WAITING = "waiting";
  private static final String VALID = "valid";
  private static final String EMPTY = "";
  private static final int ID = 1;

  private static UserUCC userUCC;
  private static ServiceLocator locator;
  private UserDAO userDAO;
  private User userWrongPassword;
  private User userDenied;
  private User userWaiting;
  private User user;

  @BeforeAll
  public static void init() {
    locator = ServiceLocatorUtilities.bind(new TestApplicationBinder());
    userUCC = locator.getService(UserUCC.class);
  }


  @BeforeEach
  public void setup() {
    userDAO = locator.getService(UserDAO.class);
    Mockito.clearInvocations(userDAO);
  }

  @Test
  public void loginExceptionUserNull() {

    Mockito.when(userDAO.getOneByUsername(USERNAME)).thenReturn(null);

    Throwable exception = Assertions.assertThrows(Exception.class,
        () -> userUCC.login(USERNAME, PASSWORD));

    Assertions.assertAll(
        () -> Assertions.assertEquals(MESSAGE, exception.getMessage()),
        () -> Mockito.verify(userDAO).getOneByUsername(USERNAME)
    );
  }

  @Test
  public void loginExceptionWrongPassword() {

    userWrongPassword = Mockito.mock(User.class);

    Mockito.when(userWrongPassword.checkPassword(PASSWORD)).thenReturn(false);
    Mockito.when(userDAO.getOneByUsername(USERNAME)).thenReturn(userWrongPassword);

    Throwable exception = Assertions.assertThrows(Exception.class,
        () -> userUCC.login(USERNAME, PASSWORD));

    Assertions.assertAll(
        () -> Assertions.assertEquals(MESSAGE, exception.getMessage()),
        () -> Mockito.verify(userDAO).getOneByUsername(USERNAME));
  }

  @Test
  public void loginExceptionUserDenied() {

    userDenied = Mockito.mock(User.class);

    Mockito.when(userDenied.getReasonForConnectionRefusal()).thenReturn("You are not recognized");
    Mockito.when(userDenied.checkPassword(PASSWORD)).thenReturn(true);
    Mockito.when(userDenied.getState()).thenReturn(DENIED);
    Mockito.when(userDenied.isDenied()).thenReturn(true);
    Mockito.when(userDAO.getOneByUsername(USERNAME)).thenReturn(userDenied);

    Throwable exception = Assertions.assertThrows(Exception.class,
        () -> userUCC.login(USERNAME, PASSWORD));

    Assertions.assertAll(
        () -> Assertions.assertEquals(userDenied.getReasonForConnectionRefusal(),
            exception.getMessage()),
        () -> Mockito.verify(userDenied, Mockito.atMost(2)).getReasonForConnectionRefusal(),
        () -> Mockito.verify(userDenied).checkPassword(PASSWORD),
        () -> Mockito.verify(userDenied, Mockito.atMost(2)).getState(),
        () -> Mockito.verify(userDenied).isDenied(),
        () -> Mockito.verify(userDAO).getOneByUsername(USERNAME));
  }

  @Test
  public void loginExceptionUserWaiting() {

    userWaiting = Mockito.mock(User.class);

    Mockito.when(userWaiting.checkPassword(PASSWORD)).thenReturn(true);
    Mockito.when(userWaiting.getState()).thenReturn(WAITING);
    Mockito.when(userWaiting.isWaiting()).thenReturn(true);
    Mockito.when(userDAO.getOneByUsername(USERNAME)).thenReturn(userWaiting);

    Throwable exception = Assertions.assertThrows(Exception.class,
        () -> userUCC.login(USERNAME, PASSWORD));

    Assertions.assertAll(
        () -> Assertions.assertEquals("user on hold", exception.getMessage()),
        () -> Mockito.verify(userWaiting).checkPassword(PASSWORD),
        () -> Mockito.verify(userWaiting, Mockito.atMost(3)).getState(),
        () -> Mockito.verify(userWaiting).isWaiting(),
        () -> Mockito.verify(userDAO).getOneByUsername(USERNAME));
  }

  @Test
  public void loginSuccessful() {

    user = Mockito.mock(User.class);

    Mockito.when(user.getUserName()).thenReturn(USERNAME);
    Mockito.when(user.getState()).thenReturn(VALID);
    Mockito.when(user.getPassword()).thenReturn(PASSWORD);
    Mockito.when(user.checkPassword(user.getPassword())).thenReturn(true);
    Mockito.when(userDAO.getOneByUsername(user.getUserName())).thenReturn(user);

    Assertions.assertAll(
        () -> Assertions.assertEquals(user,
            userUCC.login(user.getUserName(), user.getPassword())),
        () -> Mockito.verify(user, Mockito.atMost(3)).getUserName(),
        () -> Mockito.verify(user, Mockito.atMost(4)).getState(),
        () -> Mockito.verify(user, Mockito.atMost(3)).getPassword(),
        () -> Mockito.verify(user, Mockito.atMost(2)).checkPassword(PASSWORD),
        () -> Mockito.verify(userDAO).getOneByUsername(USERNAME));
  }

  @Test
  public void getUsersByStateException() {
    // if the state is not denied, valid or waiting the method will throw an exception
    // with the error message "invalid state"
    Throwable exception = Assertions.assertThrows(Exception.class,
        () -> userUCC.getUsersByState("wrong state"));
    Assertions.assertEquals("invalid state", exception.getMessage());
  }

  @Test
  public void getUsersByStateSuccessful() {
    List<UserDTO> listUsers = Mockito.mock(List.class);

    Mockito.when(userDAO.getAllUserByState(DENIED)).thenReturn(listUsers);
    Mockito.when(userDAO.getAllUserByState(VALID)).thenReturn(listUsers);
    Mockito.when(userDAO.getAllUserByState(WAITING)).thenReturn(listUsers);

    Assertions.assertAll(
        () -> Assertions.assertEquals(listUsers, userUCC.getUsersByState(DENIED)),
        () -> Assertions.assertEquals(listUsers, userUCC.getUsersByState(VALID)),
        () -> Assertions.assertEquals(listUsers, userUCC.getUsersByState(WAITING)),
        () -> Mockito.verify(userDAO).getAllUserByState(DENIED),
        () -> Mockito.verify(userDAO).getAllUserByState(VALID),
        () -> Mockito.verify(userDAO).getAllUserByState(WAITING)
    );
  }

  @Test
  public void addPhoneNumber() {
    // We verify the number of times that our method has been called
    userUCC.addPhoneNumber(ID, "0494555687");
    Mockito.verify(userDAO, Mockito.times(1)).addPhoneNumber(ID, "0494555687");
  }

  @Test
  public void getOneById() {
    user = Mockito.mock(User.class);

    Mockito.when(userDAO.getOneById(ID)).thenReturn(user);
    Assertions.assertAll(
        () -> Assertions.assertEquals(user, userUCC.getOneById(ID)),
        () -> Mockito.verify(userDAO).getOneById(ID)
    );
  }

  @Test
  public void checkAdmin() {
    user = Mockito.mock(User.class);

    Mockito.when(userDAO.getOneById(ID)).thenReturn(user);
    Mockito.when(user.isAdmin()).thenReturn(true);
    Assertions.assertAll(
        () -> Assertions.assertTrue(userUCC.checkAdmin(ID)),
        () -> Mockito.verify(userDAO).getOneById(ID),
        () -> Mockito.verify(user).isAdmin()
    );
  }

  @Test
  public void checkWaitingOrDenied() {
    user = Mockito.mock(User.class);

    Mockito.when(userDAO.getOneById(ID)).thenReturn(user);
    Mockito.when(user.isWaiting()).thenReturn(false);
    Mockito.when(user.isDenied()).thenReturn(false);
    Assertions.assertAll(
        () -> Assertions.assertTrue(userUCC.checkWaitingOrDenied(ID)),
        () -> Mockito.verify(userDAO).getOneById(ID),
        () -> Mockito.verify(user).isWaiting(),
        () -> Mockito.verify(user).isDenied()
    );
  }

  @Test
  public void changeStateException() {
    // if the state is not denied or valid the method will throw an exception
    // with the error message "Trying to insert invalid state"
    Throwable exception = Assertions.assertThrows(Exception.class,
        () -> userUCC.changeState(ID, WAITING, EMPTY, true));
    Assertions.assertEquals("Trying to insert invalid state", exception.getMessage());
  }

  @Test
  public void changeStateFailure() {
    Mockito.when(userDAO.getOneById(ID)).thenReturn(null);
    Assertions.assertAll(
        () -> Assertions.assertFalse(userUCC.changeState(ID, VALID, EMPTY, true)),
        () -> Assertions.assertFalse(userUCC.changeState(ID, DENIED, EMPTY, true)),
        () -> Mockito.verify(userDAO, Mockito.times(2)).getOneById(ID)
    );
  }

  @Test
  public void changeStateSuccessful() {
    user = Mockito.mock(User.class);

    Mockito.when(userDAO.getOneById(ID)).thenReturn(user);
    Assertions.assertAll(
        () -> Assertions.assertTrue(userUCC.changeState(ID, VALID, EMPTY, true)),
        () -> Assertions.assertTrue(userUCC.changeState(ID, DENIED, EMPTY, true)),
        () -> Mockito.verify(userDAO, Mockito.times(2)).getOneById(ID),
        () -> Mockito.verify(userDAO).changeState(ID, VALID, EMPTY, true),
        () -> Mockito.verify(userDAO).changeState(ID, DENIED, EMPTY, true)
    );
  }

  @Test
  public void registerException() {
    user = Mockito.mock(User.class);

    Mockito.when(user.getUserName()).thenReturn(USERNAME);
    Mockito.when(userDAO.getOneByUsername(USERNAME)).thenReturn(user);
    Throwable exception = Assertions.assertThrows(Exception.class, () -> userUCC.register(user));
    Assertions.assertAll(
        () -> Assertions.assertEquals("username already exists", exception.getMessage()),
        () -> Mockito.verify(user).getUserName(),
        () -> Mockito.verify(userDAO).getOneByUsername(USERNAME)
    );
  }

  @Test
  public void registerSuccessful() {
    user = Mockito.mock(User.class);

    Mockito.when(user.getUserName()).thenReturn(USERNAME);
    Mockito.when(userDAO.getOneByUsername(USERNAME)).thenReturn(null);
    Mockito.when(userDAO.register(user)).thenReturn(ID);
    Assertions.assertAll(
        () -> Assertions.assertEquals(ID, userUCC.register(user)),
        () -> Mockito.verify(user).getUserName(),
        () -> Mockito.verify(userDAO).getOneByUsername(USERNAME),
        () -> Mockito.verify(userDAO).register(user)
    );
  }

}
