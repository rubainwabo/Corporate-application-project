package buiseness.ucc;

import buiseness.domain.User;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dal.services.UserDAO;
import jakarta.inject.Inject;
import utils.TokenService;

public class UserUCCImpl implements UserUCC {

  @Inject
  private UserDAO myUserDAO;

  @Inject
  private TokenService myTokenService;

  /**
   * permet de connecter un utilisateur.
   *
   * @param pseudo le pseudo de la personne essayant de se connecter
   * @param mdp    son mot de passe
   * @return un objet contenant son token,son id,son pseudo, si tout se passe bien, sinon null
   */
  public ObjectNode login(String pseudo, String mdp, boolean rememberMe) {
    User user = (User) myUserDAO.getOne(pseudo);
    if (user == null) {
      return null;
    }
    if (!user.verifMdp(mdp)) {
      return null;
    }
    if (!user.checkEtat(user.getEtat()) || !user.getEtat().equals("validé")) {
      return null;
    }
    return myTokenService.localStorageLogin(user.getId(), user.getPseudo(), rememberMe);
  }

  public String refreshToken(int id) {
    return myTokenService.createToken(id);
  }
}
