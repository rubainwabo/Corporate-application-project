package be.vinci.pae.ihm;

import be.vinci.pae.buiseness.dto.NotificationDTO;
import be.vinci.pae.buiseness.ucc.NotificationUCC;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
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
      @PathParam("id") int id) {
    int userId = (int) req.getProperty("id");
    return myNotifService.getAllMyNotif(id, userId);
  }
}
