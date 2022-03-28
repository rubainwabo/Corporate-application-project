package ihm;

import buiseness.domain.dto.UserDTO;
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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.util.List;
import org.apache.commons.text.StringEscapeUtils;
import utils.exception.InvalidTokenException;
import utils.exception.PasswordOrUsernameException;
import utils.exception.ReasonForConnectionRefusalException;
import utils.exception.UserInvalidException;
import utils.exception.UserOnHoldException;


@Singleton
@Path("/auths")
public class UserRessource {

  @Inject
  private UserUCC myUserUCC;

  /**
   * allows to connect the user.
   *
   * @param body the data that the user has entered put in json format
   * @return the token associated to the user, otherwise an error in case of failure
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
    try {
      return myUserUCC.login(username, password, rememberMe);
    } catch (UserInvalidException e) {
      throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
          .entity(e.getMessage()).type("text/plain").build());
    } catch (PasswordOrUsernameException e) {
      throw new WebApplicationException(Response.status(Status.UNAUTHORIZED)
          .entity(e.getMessage()).type("text/plain").build());
    } catch (ReasonForConnectionRefusalException e) {
      throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
          .entity(e.getMessage()).type("text/plain").build());
    } catch (UserOnHoldException e) {
      throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
          .entity(e.getMessage()).type("text/plain").build());
    }
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
  public ObjectNode refreshToken(JsonNode body) {
    if (!body.hasNonNull("refreshToken")) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("a token required").type("text/plain").build());
    }
    // escape characters to avoid XSS injections and transforms the received token into text
    String refreshToken = StringEscapeUtils.escapeHtml4(body.get("refreshToken").asText());
    try {
      return myUserUCC.refreshToken(refreshToken);
    } catch (InvalidTokenException e) {
      throw new WebApplicationException(Response.status(Status.UNAUTHORIZED)
          .entity(e.getMessage()).type("text/plain").build());
    }
  }

  /**
   * retrives to get all the user with a specific state.
   *
   * @return a list of user by a specific state
   */
  @GET
  @Path("list")
  @Produces(MediaType.APPLICATION_JSON)
  public List<UserDTO> userListByState(@QueryParam("state") String state) {
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
  public String getUserPhoneNumber() {
    int userId = 1;
    return myUserUCC.getPhoneNumber(userId);
  }
}