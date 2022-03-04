package dal.services;

import buiseness.domain.UserDTO;
import buiseness.factory.BizFactory;
import dal.DalServices;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl implements UserDAO {


  @Inject
  private BizFactory myDomainFactory;

  @Inject
  private DalServices myDalService;

  @Override
  public UserDTO getOne(String username) {
    try (PreparedStatement ps = myDalService.getPreparedStatement(
        "select id,password,username,state from projet.members where username=?")) {

      ps.setString(1, username);
      return executeQuery(ps);

    } catch (SQLException throwables) {
      throwables.printStackTrace();
      return null;
    }
  }

  public PreparedStatement getPs(String query) {
    try {
      return myDalService.getPreparedStatement(query);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public UserDTO getOneById(int id) {
    try (PreparedStatement ps = this.getPs(
        "select id,password,username,state from projet.members where id=?")) {

      ps.setInt(1, id);
      return executeQuery(ps);

    } catch (SQLException throwables) {
      throwables.printStackTrace();
      return null;
    }
  }

  private UserDTO executeQuery(PreparedStatement ps) throws SQLException {
    ResultSet rs = ps.executeQuery();

    UserDTO user = myDomainFactory.getUser();
    if (!rs.next()) {
      return null;
    }
    user.setId(rs.getInt(1));
    user.setPassword(rs.getString(2));
    user.setUserName(rs.getString(3));
    user.setState(rs.getString(4));
    return user;
  }
}
