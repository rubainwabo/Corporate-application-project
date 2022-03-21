package dal.services;

import buiseness.domain.dto.ItemDTO;

public interface ItemDAO {

  int addItem(ItemDTO item, int userId);

  ItemDTO getOneById(int id);
}
