package buiseness.domain.impl;

import buiseness.domain.bizclass.ItemType;
import java.util.Arrays;

public class ItemTypeImpl implements ItemType {

  private final String possibleItem[] = {"Accessoires pour animaux domestiques",
      "Accessoires pour voiture", "Décoration", "Jouets", "Literie", "Matériel de cuisine",
      "Matériel de jardinage",
      "Meuble", "Plantes", "Produits cosmétiques", "Vélo, trottinette", "Vêtements"};


  private int idItemType;
  private String itemTypeName;

  public ItemTypeImpl() {
  }

  @Override
  public boolean typeExist() {
    return Arrays.asList(possibleItem).contains(this.itemTypeName);
  }

  @Override
  public boolean verfiyState() {
    //TODO
    return false;
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
