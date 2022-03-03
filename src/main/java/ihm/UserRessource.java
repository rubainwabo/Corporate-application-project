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
    return myUserUCC.refreshToken(JWT.decode(body.get("token").asText()).getClaim("user").asInt());
    /*
        DecodedJWT decodedToken = null;
    try {
      System.out.println(JWT.decode(body.get("token").asText()).getClaim("user") + " voici son id");
      System.out.println(JWT.decode(body.get("token").asText()).getExpiresAt().toString());
      decodedToken = JWT.require(Algorithm.HMAC256(Config.getProperty("JWTAccess"))).withIssuer("auth0").build().verify(body.get("token").asText());
    } catch (Exception e) {
      throw new WebApplicationException(Response.status(Status.UNAUTHORIZED)
          .entity("Malformed token : " + e.getMessage()).type("text/plain").build());
    }
    return null;

     */
    /*
     if (!body.hasNonNull("token")) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("login or password required").type("text/plain").build());
    }
     JWT.create().withExpiresAt(new Date(1))
     String token = body.get("token").asText();
    DecodedJWT decodedToken = null;
    try {
      decodedToken = JWT.require(Algorithm.HMAC256(Config.getProperty("JWTAccess"))).withIssuer("auth0").build().verify(token);
    } catch (Exception e) {
      throw new WebApplicationException(Response.status(Status.UNAUTHORIZED)
          .entity("Malformed token : " + e.getMessage()).type("text/plain").build());
    }
    int idUser = decodedToken.getClaim("user").asInt();

    if (token == null) {
      throw new WebApplicationException();
    }
    return (ObjectNode) body;
  }
     */
  }
}
