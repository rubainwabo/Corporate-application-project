package buiseness.ucc;

import buiseness.domain.dto.ItemDTO;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface ItemUCC {

  int addItem(ItemDTO item, int user);

  ItemDTO getDetails(int id);

  void addInterest(int itemId, ObjectNode objectNode, int userId);

  void cancelOffer(int idItem, int userId);
}
