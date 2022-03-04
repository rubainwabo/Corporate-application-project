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
        "select id_personne,mot_de_passe,pseudo,etat from projet.personnes where pseudo=?")) {

      ps.setString(1, username);
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

    } catch (SQLException throwables) {

      throwables.printStackTrace();
    }
    return null;
  }
}
