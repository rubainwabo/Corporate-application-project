package dal.services;

import buiseness.domain.UserDTO;
import buiseness.factory.BizFactory;
import dal.DalServices;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//TODO Vérifier la duplication de code
public class UserDAOImpl implements UserDAO {

  @Inject
  private BizFactory myDomainFactory;

  @Inject
  private DalServices myDalService;

  @Override
  public UserDTO getOneByUsername(String username) {
    try (PreparedStatement ps = myDalService.getPreparedStatement(
        "select user_id,password,username,state, _role,"
            + "reason_for_connection_refusal from projet.members where username=?")) {

      ps.setString(1, username);
      return getUserDTO(ps);


    } catch (SQLException throwables) {
      throwables.printStackTrace();
      return null;
    }

  }

  @Override
  public UserDTO getOneById(int id) {
    try (PreparedStatement ps = myDalService.getPreparedStatement(
            "select user_id,password,username,state, _role,"
                    + "reason_for_connection_refusal from projet.members where user_id=?")) {

      ps.setInt(1, id);
      return getUserDTO(ps);

    } catch (SQLException throwables) {
      throwables.printStackTrace();
      return null;
    }
  }

  //Made to avoid duplicata code for getOneById and getOneByUsername
  private UserDTO getUserDTO(PreparedStatement ps) throws SQLException {
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
      user.setRole(rs.getString(6));
      return user;
    }
  }

}
