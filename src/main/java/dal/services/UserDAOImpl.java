package dal.services;

import buiseness.dto.UserDTO;
import buiseness.factory.BizFactory;
import dal.DalBackService;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.exception.FatalException;

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
    } catch (Exception e) {
      throw new FatalException(e);
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
          user.setPostCode(rs.getInt(5));
          user.setBuildingNumber(rs.getInt(6));
          user.setId(rs.getInt(7));
          user.setUserName(rs.getString(8));
          user.setState(rs.getString(9));
          user.setRole(rs.getString(11));
          userDTOList.add(user);
        }
      }
    } catch (Exception e) {
      throw new FatalException(e);
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
    } catch (Exception e) {
      throw new FatalException(e);
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
    } catch (Exception e) {
      throw new FatalException(e);
    }
  }

  @Override
  public void addPhoneNumber(int userId, String phoneNumber) {
    try (PreparedStatement psAddPhone = myDalService.getPreparedStatement(
        "update projet.members set phone_number = '" + phoneNumber + "' where user_id = "
            + userId)) {
      psAddPhone.executeUpdate();
    } catch (Exception e) {
      throw new FatalException(e);
    }
  }

  @Override
  public void changeState(int userId, String state, String refusalReason, boolean admin) {
    String role = admin ? "admin" : "member";

    String query = refusalReason.isBlank() ? "update projet.members set state = '" + state
        + "', _role = '" + role + (state.equals("valid")
        ? "',reason_for_connection_refusal = null"
        : "'")
        + " where user_id =" + userId
        : "update projet.members set state = '" + state
            + "', reason_for_connection_refusal = '"
            + refusalReason + "', _role = '" + role + (state.equals("valid")
            ? "',reason_for_connection_refusal = null" : "'")
            + " where user_id = " + userId;

    try (PreparedStatement psConfirm = myDalService.getPreparedStatement(
        query)) {
      psConfirm.executeUpdate();
    } catch (Exception e) {
      throw new FatalException(e);
    }
  }

  @Override
  public int register(UserDTO user) {

    try (PreparedStatement ps = myDalService.getPreparedStatementWithId(
        "INSERT INTO projet.members(user_id,username,last_name, first_name,"
            + " unit_number,state,password,street,postCode,"
            + " building_number,city,"
            + " url_picture,nb_of_item_not_taken) VALUES (DEFAULT,?,?,?,?,?,?,?,?,?,?,?,?) ",
        Statement.RETURN_GENERATED_KEYS)) {

      ps.setString(1, user.getUserName());
      ps.setString(2, user.getLastName());
      ps.setString(3, user.getFirstName());
      ps.setInt(4, user.getUnitNumber());
      ps.setString(5, "waiting");
      ps.setString(6, user.getPassword());
      ps.setString(7, user.getStreet());
      ps.setInt(8, user.getPostCode());
      ps.setInt(9, user.getBuildingNumber());
      ps.setString(10, user.getCity());
      ps.setString(11, user.getUrlPhoto());
      ps.setInt(12, 0);
      ps.executeUpdate();
      ResultSet rs = ps.getGeneratedKeys();

      if (!rs.next()) {
        throw new FatalException("Echec de la query");
      }
      return rs.getInt(1);
    } catch (Exception e) {
      throw new FatalException(e);
    }
  }
}
