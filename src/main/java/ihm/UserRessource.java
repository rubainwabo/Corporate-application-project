package ihm;

import buiseness.ucc.UserUCC;
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

    ObjectNode token = myUserUCC.login(pseudo, password, rememberMe);
    System.out.println(token);
    if (token == null) {
      throw new WebApplicationException();
    }
    return token;
  }
}
