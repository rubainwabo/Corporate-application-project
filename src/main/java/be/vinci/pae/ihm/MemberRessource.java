package be.vinci.pae.ihm;

import be.vinci.pae.buiseness.dto.UserDTO;
import be.vinci.pae.buiseness.ucc.UserUCC;
import be.vinci.pae.utils.TokenService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.glassfish.jersey.server.ContainerRequest;

@Singleton
@Path("/members")
public class MemberRessource {

  @Inject
  private UserUCC myUserUCC;

  @Inject
  private TokenService myTokenService;

  /**
   * Returns the user who's connected.
   *
   * @return true or false if state successfully changed.
   */
  @GET
  @Path("myProfile")
  @Produces(MediaType.APPLICATION_JSON)
  public UserDTO userMyProfile(@Context ContainerRequest req) {
    int userId = (int) req.getProperty("id");
    return myUserUCC.getOneById(userId);
  }

  /**
   * check the validity of a user.
   *
   * @param req the user issuing a request
   * @return 1 refresh and 1 access token, if user haven't a refresh -> return null
   */
  @GET
  @Path("me")
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode userCheckValidity(@Context ContainerRequest req) {
    int userId = (int) req.getProperty("id");
    // if return smth 200, else if userIsValid return 204 else return 401
    return req.getProperty("refresh") != null ? myTokenService.getRefreshedTokens(userId) : null;
  }

  /**
   * retrives to get all the list of users who are interest in the item.
   *
   * @param req    container request
   * @param itemId the id of the item
   * @return List of users
   */
  @GET
  @Path("interest/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public List<UserDTO> userInterest(@Context ContainerRequest req, @PathParam("id") int itemId) {
    return myUserUCC.getUsersIterest((int) req.getProperty("id"), itemId);
  }

  /**
   * tries to retrieve a user's data according to the id of the person making the request.
   *
   * @param req the user issuing a request
   * @return user's details
   */
  @GET
  @Path("details")
  @Produces(MediaType.APPLICATION_JSON)
  public UserDTO userGetOneById(@Context ContainerRequest req, @QueryParam("id") int id) {
    return id > 0 ? myUserUCC.getOneById(id) : myUserUCC.getOneById((int) req.getProperty("id"));
  }

  /**
   * Updates the profile of an user.
   *
   * @param req  request we get.
   * @param body data we want to change.
   * @return true if user updates successfully.
   */
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
}