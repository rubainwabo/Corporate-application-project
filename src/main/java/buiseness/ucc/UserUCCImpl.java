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
   * allows you to connect a user.
   *
   * @param pseudo the username of the person trying to connect
   * @param mdp    his password
   * @return an objectNode which will be composed of his token(s), his id, his nickname and if he
   * wants to be remembered
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

  /**
   * verify the refresh token.
   *
   * @param id    userId in the token
   * @param token token of the request
   * @return an acessToken
   */
  public String refreshToken(int id, String token) {
    if (!myTokenService.verifyRefreshToken(token)) {
      return null;
    }
    return myTokenService.getAccessToken(id);
  }
}