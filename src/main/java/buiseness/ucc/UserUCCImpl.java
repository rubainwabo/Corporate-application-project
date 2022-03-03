package buiseness.ucc;

import buiseness.domain.User;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dal.services.UserDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

public class UserUCCImpl implements UserUCC {

  @Inject
  private UserDAO myUserDAO;
  /*
   *  @Inject
   *  private BizFactory myBizFacto;
   */

  /**
   * permet de connecter un utilisateur.
   *
   * @param pseudo le pseudo de la personne essayant de se connecter
   * @param mdp    son mot de passe
   * @return un objet contenant son token,son id,son pseudo, si tout se passe bien, sinon null
   */
  public ObjectNode login(String pseudo, String mdp, boolean rememberMe) {
    System.out.println(pseudo);

    User user = (User) myUserDAO.getOne(pseudo);

    // faut utiliser la factory pour créer le userDTO ???
    // check si les pasword correspondent
    if (user == null || !user.verifMdp(mdp) || !user.getEtat().equals("validé")) {
      throw new WebApplicationException();
    }
    return user.creeToken(user.getId(), user.getPseudo(), rememberMe);
  }
}
