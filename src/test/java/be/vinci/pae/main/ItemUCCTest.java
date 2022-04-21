package be.vinci.pae.main;

import buiseness.dto.ItemDTO;
import buiseness.ucc.ItemUCC;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dal.services.DateDAO;
import dal.services.ItemDAO;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ItemUCCTest {

  private static final int ID = 1;
  private static final int LIMIT_ZERO = 0;
  private static final int LIMIT_TWELVE = 12;

  private static ItemUCC itemUCC;
  private static ServiceLocator locator;
  private ItemDAO itemDAO;
  private DateDAO dateDAO;


  /**
   * Init for tests.
   */
  @BeforeAll
  public static void init() {
    locator = ServiceLocatorUtilities.bind(new TestApplicationBinder());
    itemUCC = locator.getService(ItemUCC.class);
  }

  /**
   * Set up for tests.
   */
  @BeforeEach
  public void setup() {
    itemDAO = locator.getService(ItemDAO.class);
    dateDAO = locator.getService(DateDAO.class);
    Mockito.clearInvocations(itemDAO);
    Mockito.clearInvocations(dateDAO);
  }

  @Test
  public void addItem() {
    ItemDTO item = Mockito.mock(ItemDTO.class);

    Mockito.when(itemDAO.addItem(item, ID)).thenReturn(ID);
    Assertions.assertAll(
        () -> Assertions.assertEquals(ID, itemUCC.addItem(item, ID)),
        () -> Mockito.verify(itemDAO).addItem(item, ID),
        () -> Mockito.verify(dateDAO).addDate(ID)
    );
  }

  @Test
  public void getDetails() {
    ItemDTO item = Mockito.mock(ItemDTO.class);

    Mockito.when(itemDAO.getOneById(ID)).thenReturn(item);
    Assertions.assertAll(
        () -> Assertions.assertEquals(item, itemUCC.getDetails(ID)),
        () -> Mockito.verify(itemDAO).getOneById(ID)
    );
  }

  @Test
  public void addInterest() {
    ObjectNode objectNode = Mockito.mock(ObjectNode.class);

    itemUCC.addInterest(ID, objectNode, ID);
    Mockito.verify(itemDAO, Mockito.times(1)).addInterest(ID, objectNode, ID);

  }

  @Test
  public void cancelOffer() {
    itemUCC.cancelOffer(ID, ID);
    Mockito.verify(itemDAO, Mockito.times(1)).cancelOffer(ID, ID);
  }

  @Test
  public void getAllItemsOffered() {
    List<ItemDTO> listItems = Mockito.mock(List.class);

    Mockito.when(itemDAO.getAllOffered(ID)).thenReturn(listItems);
    Assertions.assertAll(
        () -> Assertions.assertEquals(listItems, itemUCC.getAllItemsOffered(ID)),
        () -> Mockito.verify(itemDAO).getAllOffered(ID)
    );
  }

  @Test
  public void getLastItemsOfferedLimitZero() {
    List<ItemDTO> listItems = Mockito.mock(List.class);

    Mockito.when(itemDAO.getLastItemsOffered(LIMIT_ZERO)).thenReturn(listItems);
    Assertions.assertAll(
        () -> Assertions.assertEquals(listItems, itemUCC.getLastItemsOffered(true)),
        () -> Mockito.verify(itemDAO).getLastItemsOffered(LIMIT_ZERO)
    );
  }

  @Test
  public void getLastItemsOfferedLimitTwelve() {
    List<ItemDTO> listItems = Mockito.mock(List.class);

    Mockito.when(itemDAO.getLastItemsOffered(LIMIT_TWELVE)).thenReturn(listItems);
    Assertions.assertAll(
        () -> Assertions.assertEquals(listItems, itemUCC.getLastItemsOffered(false)),
        () -> Mockito.verify(itemDAO).getLastItemsOffered(LIMIT_TWELVE)
    );
  }

}
