package be.vinci.pae.ihm;

import be.vinci.pae.buiseness.dto.DateDTO;
import be.vinci.pae.buiseness.ucc.DateUCC;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Singleton
@Path("/dates")
public class DateRessource {

  @Inject
  private DateUCC myDateUCCSerivce;

  /**
   * retrieves all offered date about a specific item.
   *
   * @param itemId the item id
   * @return the list of date for this item.
   */
  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public List<DateDTO> userAllDateOfAnItem(@PathParam("id") int itemId) {
    if (itemId <= 0) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("object inexistant").type("text/plain").build());
    }
    return myDateUCCSerivce.getAllDateItem(itemId);
  }
}
