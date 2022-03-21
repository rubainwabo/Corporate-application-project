package buiseness.ucc;

import buiseness.domain.dto.ItemDTO;

public interface ItemUCC {

  int addItem(ItemDTO item, int user);

  ItemDTO getDetails(int id);
}
