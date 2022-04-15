package ihm;

import buiseness.dto.UserDTO;
import buiseness.ucc.UserUCC;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
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
}