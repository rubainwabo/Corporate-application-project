package filters;

import buiseness.ucc.UserUCC;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;
import utils.TokenService;

//1 set id dans le premier seulement
//2 vrai filter qui vérifie l'user

@Singleton
@Provider
public class AuthorizationRequestFilter implements ContainerRequestFilter {

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
        throw new WebApplicationException(Response.status(Status.UNAUTHORIZED)
            .entity("Malformed token : " + e.getMessage()).type("text/plain").build());
      }
      int id = decodedToken.getClaim("user").asInt();
      System.out.println(myUserUCC.checkWaitingOrDenied(id));
      if (!myUserUCC.checkWaitingOrDenied(id)) {
        requestContext.abortWith(Response.status(Status.FORBIDDEN)
            .entity("You are forbidden to access this resource").build());
      }
      requestContext.setProperty("id", id);
    }
  }
}

