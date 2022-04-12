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
import org.glassfish.jersey.server.ContainerRequest;
import utils.TokenService;

@Singleton
@Path("/members")
public class MemberRessource {

  @Inject
  private UserUCC myUserUCC;

  @Inject
  private TokenService myTokenService;

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

  @GET
  @Path("me")
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode userCheckValidity(@Context ContainerRequest req) {
    int userId = (int) req.getProperty("id");
    // if return smth 200, else if userIsValid return 204 else return 401
    return req.getProperty("refresh") != null ? myTokenService.getRefreshedTokens(userId) : null;
  }

  @GET
  @Path("details")
  @Produces(MediaType.APPLICATION_JSON)
  public UserDTO userGetOneById(@Context ContainerRequest req) {
    int userId = (int) req.getProperty("id");
    return myUserUCC.getOneById(userId);
  }
}