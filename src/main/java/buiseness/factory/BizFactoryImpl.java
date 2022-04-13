package buiseness.factory;

import buiseness.dto.DateDTO;
import buiseness.dto.ItemDTO;
import buiseness.dto.ItemTypeDTO;
import buiseness.dto.UserDTO;
import buiseness.impl.DateImpl;
import buiseness.impl.ItemImpl;
import buiseness.impl.ItemTypeImpl;
import buiseness.impl.UserImpl;

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

  @Override
  public DateDTO getDate() {
    return new DateImpl();
  }
}