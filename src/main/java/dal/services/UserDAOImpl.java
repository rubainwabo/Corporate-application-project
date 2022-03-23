package dal.services;

import buiseness.domain.UserDTO;
import buiseness.factory.BizFactory;
import dal.DalBackService;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {


  @Inject
  private BizFactory myDomainFactory;

  @Inject
  private DalBackService myDalService;

  @Override
  public UserDTO getOneByUsername(String username) {
    try (PreparedStatement ps = myDalService.getPreparedStatement(
        "select user_id,password,username,state,"
            + "reason_for_connection_refusal from projet.members where username=?")) {

      ps.setString(1, username);
      try (ResultSet rs = ps.executeQuery()) {
        UserDTO user = myDomainFactory.getUser();
        if (!rs.next()) {
          return null;
        }
        user.setId(rs.getInt(1));
        user.setPassword(rs.getString(2));
        user.setUserName(rs.getString(3));
        user.setState(rs.getString(4));
        user.setReasonForConnectionRefusal(rs.getString(5));
        return user;
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
      return null;
    }
  }

  @Override
  public List<UserDTO> getAllUserByState(String state) {
    List<UserDTO> userDTOList;
    try (PreparedStatement ps = myDalService.getPreparedStatement(
        "select last_name,first_name,city,street,postCode,building_number,user_id,username from projet.members where state=?")) {
      ps.setString(1, state);
      try (ResultSet rs = ps.executeQuery()) {
        userDTOList = new ArrayList<>();
        UserDTO user;
        while (rs.next()) {
          user = myDomainFactory.getUser();
          user.setLastName(rs.getString(1));
          user.setFirstName(rs.getString(2));
          user.setCity(rs.getString(3));
          user.setStreet(rs.getString(4));
          user.setPostCode(rs.getString(5));
          user.setBuildingNumber(rs.getString(6));
          user.setId(rs.getInt(7));
          user.setUserName(rs.getString(8));
          userDTOList.add(user);
        }
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
      return null;
    }
    return userDTOList;
  }
}