package be.vinci.pae.main;

import be.vinci.pae.buiseness.dto.ItemTypeDTO;
import be.vinci.pae.buiseness.ucc.ItemTypeUCC;
import be.vinci.pae.dal.services.ItemTypeDAO;
import be.vinci.pae.utils.exception.FatalException;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ItemTypeUCCTest {

  private static ItemTypeUCC itemTypeUCC;
  private static ServiceLocator locator;
  private final String type = "type";
  private ItemTypeDAO itemTypeDAO;

  @BeforeAll
  public static void init() {
    locator = ServiceLocatorUtilities.bind(new TestApplicationBinder());
    itemTypeUCC = locator.getService(ItemTypeUCC.class);
  }

  @BeforeEach
  public void setup() {
    itemTypeDAO = locator.getService(ItemTypeDAO.class);
    Mockito.clearInvocations(itemTypeDAO);
    Mockito.reset(itemTypeDAO);
  }

  @Test
  public void addItemType() {
    ItemTypeDTO itemType = Mockito.mock(ItemTypeDTO.class);

    Mockito.when(itemTypeDAO.addItemType(type)).thenReturn(itemType);
    Assertions.assertAll(
        () -> Assertions.assertEquals(itemType, itemTypeUCC.addItemType(type)),
        () -> Mockito.verify(itemTypeDAO).addItemType(type)
    );
  }

  @Test
  public void addItemTypeFatalException() {
    Mockito.when(itemTypeDAO.addItemType(type)).thenThrow(FatalException.class);
    Assertions.assertAll(
        () -> Assertions.assertThrows(FatalException.class, () -> itemTypeUCC.addItemType(type)),
        () -> Mockito.verify(itemTypeDAO).addItemType(type)
    );
  }


  @Test
  public void getAllItemType() {
    List<ItemTypeDTO> listItemTypes = Mockito.mock(List.class);

    Mockito.when(itemTypeDAO.getAllItemType()).thenReturn(listItemTypes);
    Assertions.assertAll(
        () -> Assertions.assertEquals(listItemTypes, itemTypeUCC.getAllItemType()),
        () -> Mockito.verify(itemTypeDAO).getAllItemType()
    );
  }

  @Test
  public void getAllItemTypeFatalException() {
    Mockito.when(itemTypeDAO.getAllItemType()).thenThrow(FatalException.class);
    Assertions.assertAll(
        () -> Assertions.assertThrows(FatalException.class, () -> itemTypeUCC.getAllItemType()),
        () -> Mockito.verify(itemTypeDAO).getAllItemType()
    );
  }


}
