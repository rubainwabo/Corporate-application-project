package ihm;

import buiseness.ucc.UserUCC;
import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Singleton
@Path("/auths")
public class UserRessource {

  /*
   * @Inject
   * private UserDAO myUserDataService;
   */

  @Inject
  private UserUCC myUserUCC;


  /**
   * permet de connecter l'utilisateur.
   *
   * @param body les données que l'utilisateur à entré mise sous format json
   * @return le token associé à l'utilisateur, sinon une erreur en cas d'échec
   */
  @POST
  @Path("login")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode login(JsonNode body) {

    if (!body.hasNonNull("pseudo") || !body.hasNonNull("password") || !body.hasNonNull(
        "rememberMe")) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("login or password required").type("text/plain").build());
    }

    String pseudo = body.get("pseudo").asText();
    String password = body.get("password").asText();

    boolean rememberMe = body.get("rememberMe").asBoolean();

    ObjectNode authentifiedUser = myUserUCC.login(pseudo, password, rememberMe);
    if (authentifiedUser == null) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("null token").type("text/plain").build());
    }
    return authentifiedUser;
  }

  /**
   * Refresh the user token.
   *
   * @param body the user's data retrieved via his local storage in the front-end
   * @return the created token, otherwise null in case of error
   */

  @POST
  @Path("refreshToken")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public String refreshToken(JsonNode body) {
    if (!body.hasNonNull("token")) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("a token is required").type("text/plain").build());
    }
    String token = body.get("token").asText();
    if (token.isBlank()) {
      throw new WebApplicationException(Response.status(Status.UNAUTHORIZED)
          .entity("empty token").type("text/plain").build());
    }
    var idUser = JWT.decode(token).getClaim("user").asInt();
    String refreshedToken = myUserUCC.refreshToken(idUser, token);
    if (refreshedToken == null) {
      throw new WebApplicationException(Response.status(Status.UNAUTHORIZED)
          .entity("token not valid").type("text/plain").build());
    }
    return refreshedToken;
  }
}
