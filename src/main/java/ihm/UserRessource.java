package ihm;

import buiseness.dto.UserDTO;
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
import org.apache.commons.text.StringEscapeUtils;
import utils.TokenService;

// ! To use the AdminAuthorizeFilter the name of your path methods must contain "admin" !
// (name can be changed in FiltersDynamicBindingConfig class)
// ! To use the AuthorizeRequestFilter the name of path methods must contain "user" !


@Singleton
@Path("/auths")
public class UserRessource {

  @Inject
  private UserUCC myUserUCC;

  @Inject
  private TokenService myTokenService;

  /**
   * allows to connect the user.
   *
   * @param body the data that the user has entered put in json format
   * @return the user
   */
  @POST
  @Path("login")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode login(JsonNode body) {

    if (!body.hasNonNull("username") || !body.hasNonNull("password") || !body.hasNonNull(
        "rememberMe")) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("username or password required").type("text/plain").build());
    }

    // escape characters to avoid XSS injections
    String username = StringEscapeUtils.escapeHtml4(body.get("username").asText());
    String password = StringEscapeUtils.escapeHtml4(body.get("password").asText());
    // allows to know if the user wants to be remembered
    boolean rememberMe = body.get("rememberMe").asBoolean();

    if (username.isBlank() || password.isBlank()) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("username or password required").type("text/plain").build());
    }
    var user = myUserUCC.login(username, password, rememberMe);
    var token = myTokenService.login(user.getId(), username, rememberMe);
    token.put("role", user.getRole());
    return token;
  }

  /**
   * register a user in the DB.
   *
   * @param user the user
   * @return the user informations
   */
  @POST
  @Path("register")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public int register(UserDTO user) {
    if (user == null || user.getUserName() == null || user.getUserName().isBlank() ||
        user.getLastName() == null || user.getLastName().isBlank() ||
        user.getFirstName() == null || user.getFirstName().isBlank() ||
        user.getPassword() == null || user.getPassword().isBlank()) {
      System.out.println("helllo");
      throw new WebApplicationException(
          Response.status(Response.Status.BAD_REQUEST).entity("Lacks of mandatory info")
              .type("text/plain").build());
    }
    return myUserUCC.register(user);
  }
}
