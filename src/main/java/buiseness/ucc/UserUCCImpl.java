package buiseness.ucc;

import buiseness.domain.User;
import buiseness.factory.BizFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dal.services.UserDAO;
import jakarta.inject.Inject;

public class UserUCCImpl implements UserUCC {

  @Inject
  private UserDAO myUserDAO;
  @Inject
  private BizFactory myBizFacto;

  /**
   * permet de connecter un utilisateur
   *
   * @param pseudo le pseudo de la personne essayant de se connecter
   * @param mdp    son mot de passe
   * @return un objet contenant son token,son id,son pseudo, si tout se passe bien, sinon null
   */
  public ObjectNode seConnecter(String pseudo, String mdp) {
    User user = (User) myUserDAO.getOne(pseudo);
    // faut utiliser la factory pour créer le userDTO ???
    if (user == null) {
      return null;
    }
    // check si les pasword correspondent
    if (!user.verifMdp(mdp)) {
      return null;
    }
    if (!user.getEtat().equals("accépté")) {
      return null;
    }
    return user.creeToken(user.getId(), user.getPseudo());
  }
}