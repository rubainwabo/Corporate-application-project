package be.vinci.pae.main;

import be.vinci.pae.buiseness.dto.ItemDTO;
import be.vinci.pae.buiseness.ucc.ItemUCC;
import be.vinci.pae.dal.services.DateDAO;
import be.vinci.pae.dal.services.ItemDAO;
import be.vinci.pae.dal.services.NotificationDAO;
import be.vinci.pae.utils.exception.BizzException;
import be.vinci.pae.utils.exception.FatalException;
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
  private static final String state = "state";

  private static ItemUCC itemUCC;
  private static ServiceLocator locator;
  private static NotificationDAO notificationDAO;
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
    notificationDAO = locator.getService(NotificationDAO.class);
    Mockito.clearInvocations(itemDAO);
    Mockito.clearInvocations(dateDAO);
    Mockito.clearInvocations(notificationDAO);
    Mockito.reset(itemDAO);
    Mockito.reset(dateDAO);
    Mockito.reset(notificationDAO);
  }

  @Test
  public void addItemSuccessful() {
    ItemDTO item = Mockito.mock(ItemDTO.class);

    Mockito.when(itemDAO.addItem(item, ID)).thenReturn(ID);
    Assertions.assertAll(
        () -> Assertions.assertEquals(ID, itemUCC.addItem(item, ID)),
        () -> Mockito.verify(itemDAO).addItem(item, ID),
        () -> Mockito.verify(dateDAO).addDate(ID)
    );
  }

  @Test
  public void addItemFatalException() {
    ItemDTO item = Mockito.mock(ItemDTO.class);

    Mockito.when(itemDAO.addItem(item, ID)).thenThrow(FatalException.class);
    Assertions.assertAll(
        () -> Assertions.assertThrows(FatalException.class, () -> itemUCC.addItem(item, ID)),
        () -> Mockito.verify(itemDAO).addItem(item, ID)
    );
  }


  @Test
  public void getDetailsSuccessful() {
    ItemDTO item = Mockito.mock(ItemDTO.class);

    Mockito.when(itemDAO.getOneById(ID)).thenReturn(item);
    Assertions.assertAll(
        () -> Assertions.assertEquals(item, itemUCC.getDetails(ID)),
        () -> Mockito.verify(itemDAO).getOneById(ID)
    );
  }

  @Test
  public void getDetailsFatalException() {
    Mockito.when(itemDAO.getOneById(ID)).thenThrow(FatalException.class);
    Assertions.assertAll(
        () -> Assertions.assertThrows(FatalException.class, () -> itemUCC.getDetails(ID)),
        () -> Mockito.verify(itemDAO).getOneById(ID)
    );
  }

  @Test
  public void getItemsSuccessful() {
    List<ItemDTO> list = Mockito.mock(List.class);

    Mockito.when(itemDAO.getFiltered(state, state)).thenReturn(list);
    Assertions.assertEquals(list, itemUCC.getItems(state, state));
  }

  @Test
  public void getItemsFatalException() {
    Mockito.when(itemDAO.getFiltered(state, state)).thenThrow(FatalException.class);
    Assertions.assertThrows(FatalException.class, () -> itemUCC.getItems(state, state));
  }


  @Test
  public void addInterestSuccessful() {
    itemUCC.addInterest(ID, ID, true, state, state, ID);
    Mockito.verify(itemDAO).addInterest(ID, ID, true, state, state, ID);

  }


  @Test
  public void addInterestFatalException() {
    Mockito.doThrow(FatalException.class).when(itemDAO).addInterest(ID, ID, true, state, state, ID);
    Assertions.assertThrows(FatalException.class,
        () -> itemUCC.addInterest(ID, ID, true, state, state, ID));
    Mockito.verify(itemDAO, Mockito.times(1)).addInterest(ID, ID, true, state, state, ID);

  }


  @Test
  public void changeItemConditionSuccessful() {
    ItemDTO item = Mockito.mock(ItemDTO.class);
    Mockito.when(item.getOfferorId()).thenReturn(ID);
    Mockito.when(itemDAO.getOneById(ID)).thenReturn(item);
    itemUCC.changeItemCondition(ID, ID, state, ID);
    Mockito.verify(itemDAO).getOneById(ID);
    Mockito.verify(item).getOfferorId();
    Mockito.verify(itemDAO).changeItemCondition(ID, ID, state, ID);
  }

  @Test
  public void changeItemConditionBizzException() {
    ItemDTO item = Mockito.mock(ItemDTO.class);
    Mockito.when(item.getOfferorId()).thenReturn(ID);
    Mockito.when(itemDAO.getOneById(ID)).thenReturn(item);
    Assertions.assertThrows(BizzException.class, () ->
        itemUCC.changeItemCondition(ID, 2, state, ID));
    Mockito.verify(itemDAO).getOneById(ID);
    Mockito.verify(item).getOfferorId();
  }


  @Test
  public void changeItemConditionFatalException() {
    ItemDTO item = Mockito.mock(ItemDTO.class);
    Mockito.when(item.getOfferorId()).thenReturn(ID);
    Mockito.when(itemDAO.getOneById(ID)).thenReturn(item);
    Mockito.doThrow(FatalException.class).when(itemDAO).changeItemCondition(ID, ID, state, ID);
    Assertions.assertThrows(FatalException.class, () ->
        itemUCC.changeItemCondition(ID, ID, state, ID));
    Mockito.verify(itemDAO).getOneById(ID);
    Mockito.verify(item).getOfferorId();
    Mockito.verify(itemDAO).changeItemCondition(ID, ID, state, ID);
  }


  @Test
  public void getMyItemsSuccessful() {
    List<ItemDTO> list = Mockito.mock(List.class);

    Mockito.when(itemDAO.getMyItems(ID, state, ID, true)).thenReturn(list);
    Assertions.assertEquals(list, itemUCC.getMyItems(ID, state, ID, true));
    Mockito.verify(itemDAO).getMyItems(ID, state, ID, true);
  }

  @Test
  public void getMyItemsFatalException() {
    Mockito.when(itemDAO.getMyItems(ID, state, ID, true)).thenThrow(FatalException.class);
    Assertions.assertThrows(FatalException.class, () -> itemUCC.getMyItems(ID, state, ID, true));
    Mockito.verify(itemDAO).getMyItems(ID, state, ID, true);
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

  @Test
  public void getAllItemsOfferedFatalException() {
    Mockito.when(itemDAO.getLastItemsOffered(LIMIT_ZERO)).thenThrow(FatalException.class);
    Assertions.assertAll(
        () -> Assertions.assertThrows(FatalException.class,
            () -> itemUCC.getLastItemsOffered(true)),
        () -> Mockito.verify(itemDAO).getLastItemsOffered(LIMIT_ZERO)
    );
  }


  @Test
  public void addRecipientSuccessful() {
    Mockito.when(itemDAO.addRecipient(ID, ID, ID)).thenReturn(ID);
    Assertions.assertEquals(ID, itemUCC.addRecipient(ID, ID, ID));
    Mockito.verify(itemDAO).addRecipient(ID, ID, ID);
  }

  @Test
  public void addRecipientFatalException() {
    Mockito.when(itemDAO.addRecipient(ID, ID, ID)).thenThrow(FatalException.class);
    Assertions.assertThrows(FatalException.class, () -> itemUCC.addRecipient(ID, ID, ID));
    Mockito.verify(itemDAO).addRecipient(ID, ID, ID);
  }

  @Test
  public void updateItemSuccessful() {
    ItemDTO itemDTO = Mockito.mock(ItemDTO.class);

    Mockito.when(itemDTO.getOfferorId()).thenReturn(ID);
    Mockito.when(itemDAO.updateItem(itemDTO)).thenReturn(ID);
    Assertions.assertAll(
        () -> Assertions.assertEquals(ID, itemUCC.updateItem(itemDTO, ID)),
        () -> Mockito.verify(itemDAO).updateItem(itemDTO),
        () -> Mockito.verify(itemDTO).getOfferorId()
    );
  }

  @Test
  public void updateItemBizzException() {
    ItemDTO itemDTO = Mockito.mock(ItemDTO.class);

    Mockito.when(itemDTO.getOfferorId()).thenReturn(2);
    Assertions.assertAll(
        () -> Assertions.assertThrows(BizzException.class, () -> itemUCC.updateItem(itemDTO, ID)),
        () -> Mockito.verify(itemDTO).getOfferorId()
    );
  }

  @Test
  public void updateItemFatalException() {
    ItemDTO itemDTO = Mockito.mock(ItemDTO.class);

    Mockito.when(itemDTO.getOfferorId()).thenReturn(ID);
    Mockito.when(itemDAO.updateItem(itemDTO)).thenThrow(FatalException.class);
    Assertions.assertAll(
        () -> Assertions.assertThrows(FatalException.class, () -> itemUCC.updateItem(itemDTO, ID)),
        () -> Mockito.verify(itemDAO).updateItem(itemDTO),
        () -> Mockito.verify(itemDTO).getOfferorId()
    );
  }


  @Test
  public void offerAgainSuccessful() {
    ItemDTO itemDTO = Mockito.mock(ItemDTO.class);

    Mockito.when(itemDAO.getOneById(ID)).thenReturn(itemDTO);
    Mockito.when(itemDTO.getOfferorId()).thenReturn(ID);
    Mockito.when(itemDTO.getRecipientId()).thenReturn(ID);
    Mockito.when(itemDTO.getId()).thenReturn(ID);
    Mockito.when(itemDTO.getOfferor()).thenReturn("user");
    itemUCC.offerAgain(ID, ID, ID);
    Assertions.assertAll(
        () -> Mockito.verify(dateDAO).addDate(ID),
        () -> Mockito.verify(itemDAO).changeItemCondition(ID, ID, "offered", ID),
        () -> Mockito.verify(itemDAO).getOneById(ID),
        () -> Mockito.verify(itemDTO).getOfferorId(),
        () -> Mockito.verify(itemDTO, Mockito.atMost(2)).getRecipientId(),
        () -> Mockito.verify(itemDTO).getOfferor(),
        () -> Mockito.verify(itemDTO).getId(),
        () -> Mockito.verify(notificationDAO).sendNotification("user a réoffert cet objet", ID, ID)
    );
  }

  @Test
  public void offerAgainSuccessful1() {
    ItemDTO itemDTO = Mockito.mock(ItemDTO.class);

    Mockito.when(itemDAO.getOneById(ID)).thenReturn(itemDTO);
    Mockito.when(itemDTO.getOfferorId()).thenReturn(ID);
    Mockito.when(itemDTO.getRecipientId()).thenReturn(0);
    itemUCC.offerAgain(ID, ID, ID);
    Assertions.assertAll(
        () -> Mockito.verify(dateDAO).addDate(ID),
        () -> Mockito.verify(itemDAO).changeItemCondition(ID, ID, "offered", ID),
        () -> Mockito.verify(itemDAO).getOneById(ID),
        () -> Mockito.verify(itemDTO).getOfferorId(),
        () -> Mockito.verify(itemDTO, Mockito.atMost(2)).getRecipientId()
    );
  }

  @Test
  public void offerAgainBizzException() {
    ItemDTO itemDTO = Mockito.mock(ItemDTO.class);

    Mockito.when(itemDAO.getOneById(ID)).thenReturn(itemDTO);
    Mockito.when(itemDTO.getOfferorId()).thenReturn(2);
    Assertions.assertAll(
        () -> Assertions.assertThrows(BizzException.class, () -> itemUCC.offerAgain(ID, ID, ID)),
        () -> Mockito.verify(itemDAO).getOneById(ID),
        () -> Mockito.verify(itemDTO).getOfferorId()
    );
  }

  @Test
  public void offerAgainFatalException() {
    Mockito.when(itemDAO.getOneById(ID)).thenThrow(FatalException.class);
    Assertions.assertAll(
        () -> Assertions.assertThrows(FatalException.class, () -> itemUCC.offerAgain(ID, ID, ID)),
        () -> Mockito.verify(itemDAO).getOneById(ID)
    );
  }

  @Test
  public void itemCollectedOrNotSuccessful() {
    ItemDTO itemDTO = Mockito.mock(ItemDTO.class);

    Mockito.when(itemDAO.getOneById(ID)).thenReturn(itemDTO);
    Mockito.when(itemDTO.getOfferorId()).thenReturn(ID);
    itemUCC.itemCollectedOrNot(ID, true, ID, ID);
    Assertions.assertAll(
        () -> Mockito.verify(itemDAO).itemCollectedOrNot(itemDTO, true, ID),
        () -> Mockito.verify(itemDAO).getOneById(ID),
        () -> Mockito.verify(itemDTO).getOfferorId()
    );
  }

  @Test
  public void itemCollectedOrNotBizzException() {
    ItemDTO itemDTO = Mockito.mock(ItemDTO.class);

    Mockito.when(itemDAO.getOneById(ID)).thenReturn(itemDTO);
    Mockito.when(itemDTO.getOfferorId()).thenReturn(2);
    Assertions.assertAll(
        () -> Assertions.assertThrows(BizzException.class,
            () -> itemUCC.itemCollectedOrNot(ID, true, ID, ID)),
        () -> Mockito.verify(itemDAO).getOneById(ID),
        () -> Mockito.verify(itemDTO).getOfferorId()
    );
  }

  @Test
  public void itemCollectedOrNotFatalException() {
    Mockito.when(itemDAO.getOneById(ID)).thenThrow(FatalException.class);
    Assertions.assertAll(
        () -> Assertions.assertThrows(FatalException.class,
            () -> itemUCC.itemCollectedOrNot(ID, true, ID, ID)),
        () -> Mockito.verify(itemDAO).getOneById(ID)
    );
  }

  @Test
  public void memberItemsByItemConditionSuccessful() {
    List<ItemDTO> list = Mockito.mock(List.class);

    Mockito.when(itemDAO.memberItemsByItemCondition(state, ID, true)).thenReturn(list);
    Assertions.assertEquals(list, itemUCC.memberItemsByItemCondition(state, ID, true));
    Mockito.verify(itemDAO).memberItemsByItemCondition(state, ID, true);
  }


  @Test
  public void memberItemsByItemConditionFatalException() {
    Mockito.when(itemDAO.memberItemsByItemCondition(state, ID, true))
        .thenThrow(FatalException.class);
    Assertions.assertThrows(FatalException.class,
        () -> itemUCC.memberItemsByItemCondition(state, ID, true));
    Mockito.verify(itemDAO).memberItemsByItemCondition(state, ID, true);
  }

  @Test
  public void updateItemUrlSuccessful() {
    ItemDTO itemDTO = Mockito.mock(ItemDTO.class);
    Mockito.when(itemDAO.getOneById(ID)).thenReturn(itemDTO);
    itemUCC.updateItemUrl(ID, state);
    Assertions.assertAll(
        () -> Mockito.verify(itemDAO).getOneById(ID),
        () -> Mockito.verify(itemDAO).updateItem(itemDTO)
    );
  }


  @Test
  public void updateItemUrlFatalException() {
    Mockito.when(itemDAO.getOneById(ID)).thenThrow(FatalException.class);
    Assertions.assertAll(
        () -> Assertions.assertThrows(FatalException.class, () -> itemUCC.updateItemUrl(ID, state)),
        () -> Mockito.verify(itemDAO).getOneById(ID)
    );
  }

  @Test
  public void rateItemSuccessful() {
    itemUCC.rateItem(ID, ID, state, ID);
    Mockito.verify(itemDAO).rateItem(ID, ID, state, ID);
  }


  @Test
  public void rateItemFatalException() {
    Mockito.doThrow(FatalException.class).when(itemDAO).rateItem(ID, ID, state, ID);
    Assertions.assertThrows(FatalException.class, () -> itemUCC.rateItem(ID, ID, state, ID));
    Mockito.verify(itemDAO).rateItem(ID, ID, state, ID);
  }

  @Test
  public void updateItemOfInvalidMemberSuccessful() {
    itemUCC.updateItemOfInvalidMember(ID);
    Mockito.verify(itemDAO).updateItemOfInvalidMember(ID);
  }


  @Test
  public void updateItemOfInvalidMemberFatalException() {
    Mockito.doThrow(FatalException.class).when(itemDAO).updateItemOfInvalidMember(ID);
    Assertions.assertThrows(FatalException.class, () -> itemUCC.updateItemOfInvalidMember(ID));
    Mockito.verify(itemDAO).updateItemOfInvalidMember(ID);
  }

}
