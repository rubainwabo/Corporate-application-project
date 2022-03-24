package buiseness.factory;

import buiseness.domain.dto.ItemDTO;
import buiseness.domain.dto.ItemTypeDTO;
import buiseness.domain.dto.UserDTO;
import buiseness.domain.impl.ItemImpl;
import buiseness.domain.impl.ItemTypeImpl;
import buiseness.domain.impl.UserImpl;

public class BizFactoryImpl implements BizFactory {

  @Override
  public UserDTO getUser() {
    return new UserImpl();
  }

  @Override
  public ItemDTO getItem() {
    return new ItemImpl();
  }

  @Override
  public ItemTypeDTO getItemType() {
    return new ItemTypeImpl();
  }
}