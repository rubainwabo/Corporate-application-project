package be.vinci.pae.ihm;

import be.vinci.pae.buiseness.dto.NotificationDTO;
import be.vinci.pae.buiseness.ucc.NotificationUCC;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.glassfish.jersey.server.ContainerRequest;

@Singleton
@Path("/members/notifications")
public class NotificationRessource {

  @Inject
  private NotificationUCC myNotifService;

  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public List<NotificationDTO> userGetAllMyNotification(@Context ContainerRequest req,
      @QueryParam("all") boolean allNotif) {
    int userId = (int) req.getProperty("id");
    return myNotifService.getAllMyNotif(userId, allNotif);
  }

  @PUT
  @Path("update/notViewed/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response userUpdateAllNotifToViewed(@Context ContainerRequest req) {
    int userId = (int) req.getProperty("id");
    myNotifService.updateAllNotifToViewed(userId);
    return Response.ok().build();
  }
}
