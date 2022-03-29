package dal.services;

import buiseness.domain.dto.UserDTO;
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
        "select last_name,first_name,city,street,postCode,building_number,user_id,username, "
            + "state,phone_number,_role from projet.members where state=?")) {
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
          user.setState(rs.getString(9));
          user.setRole(rs.getString(11));
          userDTOList.add(user);
        }
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
      return null;
    }
    return userDTOList;
  }

  @Override
  public UserDTO getOneById(int id) {
    try (PreparedStatement ps = myDalService.getPreparedStatement(
        "select user_id, state, _role from projet.members where user_id=?")) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        UserDTO user = myDomainFactory.getUser();
        if (!rs.next()) {
          return null;
        }
        user.setId(rs.getInt(1));
        user.setState(rs.getString(2));
        user.setRole(rs.getString(3));
        return user;
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
      return null;
    }
  }

  @Override
  public String getPhoneNumber(int userId) {
    try (PreparedStatement psPhoneNumber = myDalService.getPreparedStatement(
        "select phone_number from projet.members where user_id = " + userId)) {
      try (ResultSet rsPhoneNumber = psPhoneNumber.executeQuery()) {
        if (!rsPhoneNumber.next()) {
          return "";
        }
        return rsPhoneNumber.getString(1);
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return "";
  }

  @Override
  public void addPhoneNumber(int userId, String phoneNumber) {
    try (PreparedStatement psAddPhone = myDalService.getPreparedStatement(
        "update projet.members set phone_number = " + phoneNumber + " where user_id = " + userId)) {
      var result = psAddPhone.executeUpdate();
      if (result <= 0) {
        throw new IllegalArgumentException("prblm dans update phone number");
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  @Override
  public void changeState(int userId, String state, String refusalReason) {
    String query = refusalReason.isBlank() ? "update projet.members set state = '" + state
        + "' where user_id =" + userId
        : "update projet.members set state = '" + state + "', reason_for_connection_refusal = '"
            + refusalReason + "' where user_id = " + userId;
    System.out.println(query);
    try (PreparedStatement psConfirm = myDalService.getPreparedStatement(
        query)) {
      var result = psConfirm.executeUpdate();
      if (result <= 0) {
        throw new IllegalArgumentException("probleme dans update confirm inscription");
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

}