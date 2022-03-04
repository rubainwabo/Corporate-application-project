package buiseness.ucc;

import buiseness.domain.User;
import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dal.services.UserDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import utils.TokenService;

public class UserUCCImpl implements UserUCC {

  @Inject
  private UserDAO myUserDAO;

  @Inject
  private TokenService myTokenService;

  @Override
  public ObjectNode login(String username, String password, boolean rememberMe) {
    User user = (User) myUserDAO.getOneByUsername(username);
    if (user == null) {
      throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
          .entity("user or password incorrect").type("text/plain").build());
    }
    if (!user.checkPassword(password)) {
      throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
          .entity("username or password incorrect").type("text/plain").build());
    }
    if (user.isDenied(user.getState())) {
      throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
          .entity("denied user").type("text/plain").build());
    }
    if (user.isWaiting(user.getState())) {
      throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
          .entity("user on hold").type("text/plain").build());
    }
    return myTokenService.login(user.getId(), username, rememberMe);
  }

  @Override
  public ObjectNode refreshToken(String token) {

    if (!myTokenService.isJWT(token) || !myTokenService.verifyRefreshToken(token)) {
      throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN)
          .entity("invalid token").type("text/plain").build());
    }
    var userId = JWT.decode(token).getClaim("user").asInt();
    User user = (User) myUserDAO.getOneById(userId);
    if (!user.isValid(user.getState())) {
      throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
          .entity("unvalid user").type("text/plain").build());
    }
    return myTokenService.getRefreshedTokens(userId);
  }
}