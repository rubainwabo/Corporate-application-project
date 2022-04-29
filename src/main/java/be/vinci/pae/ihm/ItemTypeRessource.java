package be.vinci.pae.ihm;

import be.vinci.pae.buiseness.dto.ItemTypeDTO;
import be.vinci.pae.buiseness.ucc.ItemTypeUCC;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Singleton
@Path("/itemsType")

public class ItemTypeRessource {

  @Inject
  private ItemTypeUCC myItemTypeUCC;

  /**
   * retrives to add an itemType to the database.
   *
   * @param itemType the itemType
   * @return the id returned by the insert
   */
  @POST
  @Path("addItemType")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ItemTypeDTO userAddItemType(JsonNode itemType) {
    if (!itemType.hasNonNull("itemType") || itemType.get("itemType").asText().isBlank()) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("itemType informations invalid").type("text/plain").build());
    }
    return myItemTypeUCC.addItemType(itemType.get("itemType").asText());
  }

  /**
   * retrives to get all itemType as a list.
   *
   * @return a list of all the itemType
   */
  @GET
  @Path("getAll")
  @Produces(MediaType.APPLICATION_JSON)
  public List<ItemTypeDTO> userGetAllItemType() {
    return myItemTypeUCC.getAllItemType();
  }
}
