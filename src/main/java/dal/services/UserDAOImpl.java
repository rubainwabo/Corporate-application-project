package dal.services;

import buiseness.domain.UserDTO;

public class UserDAOImpl implements UserDAO {
  /*
   * @Inject
   * private BizFactory myDomainFactory;
   * @Inject
   * private DalServices myDalService;
   */

  /**
   * permet de récuperer un utilisateur par rapport à un pseudo et le renvoie à l'user UCC pour
   * continuer les checks.
   *
   * @param pseudo le pseudo entré par l'utilisateur
   * @return User trouvé
   */
  public UserDTO getOne(String pseudo) {
    /*
     *  PreparedStatement ps = myDalService.getPs("INSERT INTO projet.personnes values()");
     *  ps.executeQuery();
     */
    return null;

  }
}
