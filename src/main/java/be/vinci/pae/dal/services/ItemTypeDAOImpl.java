package be.vinci.pae.dal.services;

import be.vinci.pae.buiseness.dto.ItemTypeDTO;
import be.vinci.pae.buiseness.factory.BizFactory;
import be.vinci.pae.dal.DalBackService;
import be.vinci.pae.utils.exception.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ItemTypeDAOImpl implements ItemTypeDAO {

  @Inject
  private DalBackService myBackService;

  @Inject
  private BizFactory myDomainFactory;


  @Override
  public ItemTypeDTO addItemType(String itemTypeName) {
    try (PreparedStatement psItemType = myBackService.getPreparedStatementWithId(
        "INSERT INTO projet.item_type VALUES (DEFAULT,?)", Statement.RETURN_GENERATED_KEYS)) {
      psItemType.setString(1, itemTypeName);
      psItemType.executeUpdate();
      ResultSet rs = psItemType.getGeneratedKeys();
      int generatedKey = 0;
      if (rs.next()) {
        generatedKey = rs.getInt(1);
      }
      ItemTypeDTO itemTypeDAO = myDomainFactory.getItemType();
      itemTypeDAO.setIdItemType(generatedKey);
      itemTypeDAO.setItemTypeName(itemTypeName);
      return itemTypeDAO;

    } catch (Exception e) {
      throw new FatalException(e);
    }
  }

  @Override
  public List<ItemTypeDTO> getAllItemType() {
    List<ItemTypeDTO> itemTypeList;
    try (PreparedStatement ps = myBackService.getPreparedStatement(
        "select * from projet.item_type")) {
      try (ResultSet rs = ps.executeQuery()) {
        itemTypeList = new ArrayList<>();
        while (rs.next()) {
          var itemType = myDomainFactory.getItemType();
          itemType.setIdItemType(rs.getInt(1));
          itemType.setItemTypeName(rs.getString(2));
          itemTypeList.add(itemType);
        }
      }
      return itemTypeList;
    } catch (Exception e) {
      throw new FatalException("Echec lors de l'ajout du type de l'item");
    }
  }
}
