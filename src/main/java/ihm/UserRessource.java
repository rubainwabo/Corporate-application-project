package ihm;

import buiseness.dto.UserDTO;
import buiseness.ucc.UserUCC;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.apache.commons.text.StringEscapeUtils;
import org.glassfish.jersey.server.ContainerRequest;

// ! To use the AdminAuthorizeFilter the name of your path methods must contain "admin" !
// (name can be changed in FiltersDynamicBindingConfig class)
// ! To use the AuthorizeRequestFilter the name of path methods must contain "user" !


@Singleton
@Path("/auths")
public class UserRessource {

  @Inject
  private UserUCC myUserUCC;


  @POST
  @Path("updateProfile")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public boolean userUpdateProfile(@Context ContainerRequest req, JsonNode body) {
    int userId = (int) req.getProperty("id");

    if (!body.hasNonNull("phone") || !body.hasNonNull("street")
        || !body.hasNonNull("unitNumber") || !body.hasNonNull("city")
        || !body.hasNonNull("postcode") || !body.hasNonNull("box")
        || !body.hasNonNull("username") || !body.hasNonNull("firstName")
        || !body.hasNonNull("lastName")) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("id field is required").type("text/plain").build());
    } else {
      return myUserUCC.updateProfile(
          userId,
          body.get("username").asText(),
          body.get("firstName").asText(),
          body.get("lastName").asText(),
          body.get("street").asText(),
          body.get("unitNumber").asInt(),
          body.get("postcode").asInt(),
          body.get("box").asText(),
          body.get("city").asText(),
          body.get("phone").asText());
    }
  }

  /**
   * Returns the user who's connected.
   *
   * @return true or false if state successfully changed.
   */
  @POST
  @Path("updatePassword")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public boolean userUpdatePassword(@Context ContainerRequest req, JsonNode body) {

    if (!body.hasNonNull("newPassword")) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("newPassword field is required").type("text/plain").build());
    } else {
      int userId = (int) req.getProperty("id");
      if (body.get("newPassword").asText().isBlank()) {
        throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
            .entity("newPassword field is blank").type("text/plain").build());
      }
      return myUserUCC.updatePassword(userId, body.get("newPassword").asText());
    }
  }

  /**
   * Returns the user who's connected.
   *
   * @return true or false if state successfully changed.
   */
  @GET
  @Path("myProfile")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public UserDTO userMyProfile(@Context ContainerRequest req) {
    int userId = (int) req.getProperty("id");
    return myUserUCC.getOneById(userId);
  }

  /**
   * Change the state of a certain user.
   *
   * @param body the data that the user has entered put in json format
   * @return true or false if state successfully changed.
   */
  @POST
  @Path("changeState")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public boolean adminChangeState(JsonNode body) {
    System.out.println(body.get("admin"));
    if (!body.hasNonNull("change_id") || !body.hasNonNull("state")) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("id field is required").type("text/plain").build());
    }

    if (!body.get("state").asText().equals("denied") && body.hasNonNull("refusalReason")) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("You cannot put refusal reason on something else than refused state")
          .type("text/plain").build());
    }

    if (body.get("state").asText().equals("denied") && (!body.hasNonNull("refusalReason")
        || body.get("refusalReason").asText().isBlank())) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("You have to put your denial reason if you want to deny someone")
          .type("text/plain").build());
    }
    if (body.hasNonNull("refusalReason")) {
      return myUserUCC.changeState(body.get("change_id").asInt(), body.get("state").asText(),
          body.get("refusalReason").asText(), body.get("admin").asBoolean());
    } else {
      return myUserUCC.changeState(body.get("change_id").asInt(), body.get("state").asText(), "",
          body.get("admin").asBoolean());
    }
  }

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
    return myUserUCC.login(username, password, rememberMe);
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
  public ObjectNode userRefreshToken(JsonNode body) {
    if (!body.hasNonNull("refreshToken")) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("a token or refreshToken is required").type("text/plain").build());
    }
    // escape characters to avoid XSS injections and transforms the received token into text
    String refreshToken = StringEscapeUtils.escapeHtml4(body.get("refreshToken").asText());
    return myUserUCC.refreshToken(refreshToken);
  }

  /**
   * retrives to get all the user with a specific state.
   *
   * @return a list of user by a specific state
   */
  @GET
  @Path("list")
  @Produces(MediaType.APPLICATION_JSON)
  public List<UserDTO> adminListByState(@QueryParam("state") String state) {
    if (state.isBlank()) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("a state is required").type("text/plain").build());
    }
    return myUserUCC.getUsersByState(state);
  }

  /**
   * retrives to find the phone number of the user who's sending a request to the api.
   *
   * @return the user phoneNumber of "" if he don't have a phone number
   */
  @GET
  @Path("phoneNumber")
  @Produces(MediaType.APPLICATION_JSON)
  public String userGetUserPhoneNumber(@Context ContainerRequest req) {
    int userId = (int) req.getProperty("id");
    return myUserUCC.getPhoneNumber(userId);
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
