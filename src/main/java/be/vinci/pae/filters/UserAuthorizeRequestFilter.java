package be.vinci.pae.filters;

import be.vinci.pae.buiseness.domain.User;
import be.vinci.pae.buiseness.ucc.UserUCC;
import be.vinci.pae.utils.TokenService;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;

//1 set id dans le premier seulement
//2 vrai filter qui vérifie l'user

@Singleton
@Provider
public class UserAuthorizeRequestFilter implements ContainerRequestFilter {

  @Inject
  TokenService myTokenService;
  @Inject
  private UserUCC myUserUCC;

  @Override
  public void filter(ContainerRequestContext requestContext) {
    System.out.println("We're in AuthorizeRequestFilter");
    String token = requestContext.getHeaderString("token");
    String refreshToken = requestContext.getHeaderString("refreshToken");

    if (token == null && refreshToken == null) {
      requestContext.abortWith(Response.status(Status.UNAUTHORIZED)
          .entity("A token is needed to access this resource").build());
    } else {
      DecodedJWT decodedToken;
      try {
        decodedToken = (refreshToken == null) ? myTokenService.getVerifyToken(token)
            : myTokenService.getVerifyRefreshToken(refreshToken);

      } catch (Exception e) {
        throw new WebApplicationException(Response.status(Status.TEMPORARY_REDIRECT)
            .entity("Malformed token : " + e.getMessage()).type("text/plain").build());
      }
      int id = decodedToken.getClaim("user").asInt();

      User myUser = myUserUCC.getOneById(id);
      //We already set admin property for next filters if needed.
      requestContext.setProperty("admin", myUser.getRole());
      requestContext.setProperty("id", id);

      if (myUser.isWaiting() || myUser.isDenied()) {
        requestContext.abortWith(Response.status(Status.FORBIDDEN)
            .entity("You are denied or waiting").build());
      }

      if (refreshToken != null) {
        requestContext.setProperty("refresh", "");
      }
    }
  }
}

