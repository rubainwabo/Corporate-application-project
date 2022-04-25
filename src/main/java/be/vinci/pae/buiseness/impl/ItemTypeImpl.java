package be.vinci.pae.buiseness.impl;

import be.vinci.pae.buiseness.domain.ItemType;

public class ItemTypeImpl implements ItemType {

  private int idItemType;
  private String itemTypeName;

  public ItemTypeImpl() {
  }


  @Override
  public int getIdItemType() {
    return idItemType;
  }

  @Override
  public void setIdItemType(int idItemType) {
    this.idItemType = idItemType;
  }

  @Override
  public String getItemTypeName() {
    return itemTypeName;
  }

  @Override
  public void setItemTypeName(String itemTypeName) {
    this.itemTypeName = itemTypeName;
  }
}
