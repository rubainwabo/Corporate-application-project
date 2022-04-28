package be.vinci.pae.main;

import be.vinci.pae.buiseness.dto.DateDTO;
import be.vinci.pae.buiseness.ucc.DateUCC;
import be.vinci.pae.dal.services.DateDAO;
import be.vinci.pae.utils.exception.FatalException;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class DateUCCTest {

  private static final int ID = 1;
  private static DateUCC dateUCC;
  private static ServiceLocator locator;
  private static DateDAO dateDAO;

  /**
   * Init for tests.
   */
  @BeforeAll
  public static void init() {
    locator = ServiceLocatorUtilities.bind(new TestApplicationBinder());
    dateUCC = locator.getService(DateUCC.class);
  }

  /**
   * Set up for tests.
   */
  @BeforeEach
  public void setup() {
    dateDAO = locator.getService(DateDAO.class);
    Mockito.clearInvocations(dateDAO);
    Mockito.reset(dateDAO);
  }

  @Test
  public void getAllDateItemSuccessful() {
    List<DateDTO> list = Mockito.mock(List.class);

    Mockito.when(dateDAO.getAllDateItem(ID)).thenReturn(list);
    Assertions.assertEquals(list, dateUCC.getAllDateItem(ID));
    Mockito.verify(dateDAO).getAllDateItem(ID);
  }

  @Test
  public void getAllDateItemFatalException() {
    Mockito.when(dateDAO.getAllDateItem(ID)).thenThrow(FatalException.class);
    Assertions.assertThrows(FatalException.class, () -> dateUCC.getAllDateItem(ID));
    Mockito.verify(dateDAO).getAllDateItem(ID);
  }
}
