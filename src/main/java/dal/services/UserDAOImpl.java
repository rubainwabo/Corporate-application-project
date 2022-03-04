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

  /**
   * permet de récuperer un utilisateur par rapport à un pseudo et le renvoie à l'user UCC pour
   * continuer les checks.
   *
   * @param pseudo le pseudo entré par l'utilisateur
   * @return User trouvé
   */
  public UserDTO getOne(String pseudo) {
    try (PreparedStatement ps = myDalService.getPreparedStatement(
        "select id_personne,mot_de_passe,pseudo,etat from projet.personnes where pseudo=?")) {

      ps.setString(1, pseudo);
      ResultSet rs = ps.executeQuery();

      UserDTO user = myDomainFactory.getUser();
      if (!rs.next()) {
        return null;
      }
      user.setId(rs.getInt(1));
      user.setMdp(rs.getString(2));
      user.setPseudo(rs.getString(3));
      user.setEtat(rs.getString(4));
      return user;

    } catch (SQLException throwables) {

      throwables.printStackTrace();
    }

    return null;
  }
}
