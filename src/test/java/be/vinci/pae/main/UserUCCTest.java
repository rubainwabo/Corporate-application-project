package be.vinci.pae.main;

import be.vinci.pae.buiseness.domain.User;
import be.vinci.pae.buiseness.dto.ItemDTO;
import be.vinci.pae.buiseness.dto.UserDTO;
import be.vinci.pae.buiseness.ucc.UserUCC;
import be.vinci.pae.dal.services.ItemDAO;
import be.vinci.pae.dal.services.UserDAO;
import be.vinci.pae.utils.exception.FatalException;
import be.vinci.pae.utils.exception.UserInvalidException;
import java.util.ArrayList;
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

  /**
   * Init for tests.
   */
  @BeforeAll
  public static void init() {
    locator = ServiceLocatorUtilities.bind(new TestApplicationBinder());
    userUCC = locator.getService(UserUCC.class);
  }


  /**
   * Set up for tests.
   */

  @BeforeEach
  public void setup() {
    userDAO = locator.getService(UserDAO.class);
    Mockito.clearInvocations(userDAO);
    Mockito.reset(userDAO);
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
  public void loginFatalException() {

    Mockito.when(userDAO.getOneByUsername(USERNAME)).thenThrow(FatalException.class);

    Assertions.assertAll(
        () -> Assertions.assertThrows(FatalException.class,
            () -> userUCC.login(USERNAME, PASSWORD)),
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
  public void getUsersByStateFatalException() {
    Mockito.when(userDAO.getAllUserByState(DENIED)).thenThrow(FatalException.class);

    Assertions.assertAll(
        () -> Assertions.assertThrows(FatalException.class, () -> userUCC.getUsersByState(DENIED)),
        () -> Mockito.verify(userDAO).getAllUserByState(DENIED)
    );
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
  public void addPhoneNumberFatalException() {
    // We verify the number of times that our method has been called
    Mockito.doThrow(FatalException.class).when(userDAO).addPhoneNumber(ID, "0494555687");
    Assertions.assertAll(
        () -> Assertions.assertThrows(FatalException.class,
            () -> userUCC.addPhoneNumber(ID, "0494555687")),
        () -> Mockito.verify(userDAO, Mockito.times(1)).addPhoneNumber(ID, "0494555687"));
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
  public void getOneByIdFatalException() {
    Mockito.when(userDAO.getOneById(ID)).thenThrow(FatalException.class);
    Assertions.assertAll(
        () -> Assertions.assertThrows(FatalException.class, () -> userUCC.getOneById(ID)),
        () -> Mockito.verify(userDAO).getOneById(ID)
    );
  }

  @Test
  public void changeStateException() {
    // if the state is not denied or valid the method will throw an exception
    // with the error message "Trying to insert invalid state"
    Throwable exception = Assertions.assertThrows(Exception.class,
        () -> userUCC.changeState(ID, WAITING, EMPTY, true, ID));
    Assertions.assertEquals("Trying to insert invalid state", exception.getMessage());
  }

  @Test
  public void changeStateFailure() {
    Mockito.when(userDAO.getOneById(ID)).thenReturn(null);
    Assertions.assertAll(
        () -> Assertions.assertFalse(userUCC.changeState(ID, VALID, EMPTY, true, ID)),
        () -> Assertions.assertFalse(userUCC.changeState(ID, DENIED, EMPTY, true, ID)),
        () -> Mockito.verify(userDAO, Mockito.times(2)).getOneById(ID)
    );
  }

  @Test
  public void changeStateSuccessful() {
    user = Mockito.mock(User.class);

    Mockito.when(userDAO.getOneById(ID)).thenReturn(user);
    Assertions.assertAll(
        () -> Assertions.assertTrue(userUCC.changeState(ID, VALID, EMPTY, true, ID)),
        () -> Assertions.assertTrue(userUCC.changeState(ID, DENIED, EMPTY, true, ID)),
        () -> Mockito.verify(userDAO, Mockito.times(2)).getOneById(ID),
        () -> Mockito.verify(userDAO).changeState(ID, VALID, EMPTY, true, ID),
        () -> Mockito.verify(userDAO).changeState(ID, DENIED, EMPTY, true, ID)
    );
  }

  @Test
  public void changeStateFatalException() {
    Mockito.when(userDAO.getOneById(ID)).thenThrow(FatalException.class);
    Assertions.assertAll(
        () -> Assertions.assertThrows(FatalException.class,
            () -> userUCC.changeState(ID, VALID, EMPTY, true, ID)),
        () -> Mockito.verify(userDAO).getOneById(ID)
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

  @Test
  public void registerFatalException() {
    user = Mockito.mock(User.class);

    Mockito.when(user.getUserName()).thenReturn(USERNAME);
    Mockito.when(userDAO.getOneByUsername(USERNAME)).thenThrow(FatalException.class);
    Assertions.assertAll(
        () -> Assertions.assertThrows(FatalException.class, () -> userUCC.register(user)),
        () -> Mockito.verify(user).getUserName(),
        () -> Mockito.verify(userDAO).getOneByUsername(USERNAME)
    );
  }

  /*
    @Test
    public void updateProfileSuccessful() {
      Mockito.when(userDAO.updateProfile(ID, USERNAME, USERNAME, USERNAME, USERNAME, ID, ID, USERNAME,
          USERNAME, USERNAME)).thenReturn(true);
      Assertions.assertTrue(userUCC.updateProfile(ID, USERNAME, USERNAME, USERNAME, USERNAME, ID, ID,
          USERNAME, USERNAME, USERNAME));
      Mockito.verify(userDAO).updateProfile(ID, USERNAME, USERNAME, USERNAME, USERNAME, ID, ID,
          USERNAME, USERNAME, USERNAME);
    }

    @Test
    public void updateProfileFatalException() {
      Mockito.when(userDAO.updateProfile(ID, USERNAME, USERNAME,
          USERNAME, USERNAME, ID, ID, USERNAME, USERNAME, USERNAME)).thenThrow(FatalException.class);
      Assertions.assertThrows(FatalException.class, () -> userUCC.updateProfile(ID, USERNAME,
          USERNAME, USERNAME, USERNAME, ID, ID, USERNAME, USERNAME, USERNAME));
      Mockito.verify(userDAO).updateProfile(ID, USERNAME, USERNAME, USERNAME, USERNAME, ID, ID,
          USERNAME, USERNAME, USERNAME);
    }

    @Test
    public void updatePasswordSuccessful() {
      Mockito.when(userDAO.updatePassword(ID, PASSWORD)).thenReturn(true);
      Assertions.assertTrue(userUCC.updatePassword(ID, PASSWORD));
      Mockito.verify(userDAO).updatePassword(ID, PASSWORD);
    }

    @Test
    public void updatePasswordFatalException() {
      Mockito.when(userDAO.updatePassword(ID, PASSWORD)).thenThrow(FatalException.class);
      Assertions.assertThrows(FatalException.class, () -> userUCC.updatePassword(ID, PASSWORD));
      Mockito.verify(userDAO).updatePassword(ID, PASSWORD);
    }
  */
  @Test
  public void getAllUsersFilteredSuccessful() {
    List<UserDTO> list = Mockito.mock(List.class);

    Mockito.when(userDAO.getAllUsersFiltered(USERNAME, USERNAME, USERNAME)).thenReturn(list);
    Assertions.assertEquals(list, userUCC.getAllUsersFiltered(USERNAME, USERNAME, USERNAME));
    Mockito.verify(userDAO).getAllUsersFiltered(USERNAME, USERNAME, USERNAME);
  }

  @Test
  public void getAllUsersFilteredFatalException() {
    Mockito.when(userDAO.getAllUsersFiltered(USERNAME, USERNAME, USERNAME))
        .thenThrow(FatalException.class);
    Assertions.assertThrows(FatalException.class, () ->
        userUCC.getAllUsersFiltered(USERNAME, USERNAME, USERNAME));
    Mockito.verify(userDAO).getAllUsersFiltered(USERNAME, USERNAME, USERNAME);
  }

  @Test
  public void getAutocompleteListBlankString() {
    Assertions.assertEquals(new ArrayList(), userUCC.getAutocompleteList(EMPTY));
  }

  @Test
  public void getAutocompleteListSuccessful() {
    List<String> list = Mockito.mock(List.class);

    Mockito.when(userDAO.getAutocompleteList(USERNAME)).thenReturn(list);
    Assertions.assertEquals(list, userUCC.getAutocompleteList(USERNAME));
    Mockito.verify(userDAO).getAutocompleteList(USERNAME);
  }

  @Test
  public void getAutocompleteListFatalException() {
    Mockito.when(userDAO.getAutocompleteList(USERNAME)).thenThrow(FatalException.class);
    Assertions.assertThrows(FatalException.class, () -> userUCC.getAutocompleteList(USERNAME));
    Mockito.verify(userDAO).getAutocompleteList(USERNAME);
  }

  @Test
  public void getUsersInterestSuccessful() {
    ItemDAO itemDAO = locator.getService(ItemDAO.class);
    ItemDTO itemDTO = Mockito.mock(ItemDTO.class);
    List<UserDTO> list = Mockito.mock(List.class);

    Mockito.when(itemDAO.getOneById(ID)).thenReturn(itemDTO);
    Mockito.when(itemDTO.getOfferorId()).thenReturn(ID);
    Mockito.when(userDAO.getUserInterest(ID)).thenReturn(list);
    Assertions.assertAll(
        () -> Assertions.assertEquals(list, userUCC.getUsersInterest(ID, ID)),
        () -> Mockito.verify(itemDTO).getOfferorId(),
        () -> Mockito.verify(userDAO).getUserInterest(ID),
        () -> Mockito.verify(itemDAO, Mockito.atMost(2)).getOneById(ID)
    );
  }

  @Test
  public void getUsersInterestUserInvalidException() {
    ItemDAO itemDAO = locator.getService(ItemDAO.class);
    ItemDTO itemDTO = Mockito.mock(ItemDTO.class);

    Mockito.when(itemDAO.getOneById(ID)).thenReturn(itemDTO);
    Mockito.when(itemDTO.getOfferorId()).thenReturn(2);
    Assertions.assertAll(
        () -> Assertions.assertThrows(UserInvalidException.class,
            () -> userUCC.getUsersInterest(ID, ID)),
        () -> Mockito.verify(itemDTO).getOfferorId(),
        () -> Mockito.verify(itemDAO).getOneById(ID)
    );
  }

  @Test
  public void getUsersInterestFatalException() {
    ItemDAO itemDAO = locator.getService(ItemDAO.class);
    ItemDTO itemDTO = Mockito.mock(ItemDTO.class);

    Mockito.when(itemDAO.getOneById(ID)).thenReturn(itemDTO);
    Mockito.when(itemDTO.getOfferorId()).thenReturn(ID);
    Mockito.when(userDAO.getUserInterest(ID)).thenThrow(FatalException.class);
    Assertions.assertAll(
        () -> Assertions.assertThrows(FatalException.class,
            () -> userUCC.getUsersInterest(ID, ID)),
        () -> Mockito.verify(itemDTO).getOfferorId(),
        () -> Mockito.verify(itemDAO, Mockito.atMost(3)).getOneById(ID)
    );
  }
}

