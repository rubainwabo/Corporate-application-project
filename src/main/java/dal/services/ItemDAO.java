package dal.services;

import buiseness.domain.dto.ItemDTO;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;

public interface ItemDAO {

  int addItem(ItemDTO item, int userId);

  ItemDTO getOneById(int id);

  void addInterest(int idItem, ObjectNode objectNode, int userId);

  void cancelOffer(int idItem, int userId);

  List<ItemDTO> getLastItemsOffered(int limit);
}
