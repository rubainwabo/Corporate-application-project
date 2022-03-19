package ihm;

import buiseness.domain.dto.ItemDTO;
import buiseness.ucc.ItemUCC;
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
@Path("/items")
public class ItemRessource {

  @Inject
  private ItemUCC myItemUCC;

  @POST
  @Path("add")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public int addItem(ItemDTO itemDTO) {
    if (itemDTO == null || itemDTO.getDescription().isBlank() || itemDTO.getState().isBlank()
        || itemDTO.getItemtype().isBlank()) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("object informations invalid").type("text/plain").build());
    }
    // user ID
    //int userId = (int) request.getProperty("user");
    return myItemUCC.addItem(itemDTO, 1);
  }
}