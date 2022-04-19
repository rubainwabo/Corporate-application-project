package be.vinci.pae.main;

import buiseness.dto.ItemTypeDTO;
import buiseness.ucc.ItemTypeUCC;
import dal.services.ItemTypeDAO;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ItemTypeUCCTest {

  private final static String TYPE = "type";

  private static ItemTypeUCC itemTypeUCC;
  private static ServiceLocator locator;
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
  }

  @Test
  public void addItemType() {
    ItemTypeDTO itemType = Mockito.mock(ItemTypeDTO.class);

    Mockito.when(itemTypeDAO.addItemType(TYPE)).thenReturn(itemType);
    Assertions.assertAll(
        () -> Assertions.assertEquals(itemType, itemTypeUCC.addItemType(TYPE)),
        () -> Mockito.verify(itemTypeDAO).addItemType(TYPE)
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

}
