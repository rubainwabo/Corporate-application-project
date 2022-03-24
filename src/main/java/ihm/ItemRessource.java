package ihm;

import buiseness.domain.dto.ItemDTO;
import buiseness.ucc.ItemUCC;
import buiseness.ucc.UserUCC;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Singleton
@Path("/items")
public class ItemRessource {

  @Inject
  private ItemUCC myItemUCC;

  @Inject
  private UserUCC myUserUCC;

  @POST
  @Path("add")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public int addItem(ItemDTO itemDTO) {
    if (itemDTO == null || itemDTO.getDescription().isBlank() || itemDTO.getItemtype().isBlank()
        || itemDTO.getTimeSlot().isBlank()) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("object informations invalid").type("text/plain").build());
    }
    // user ID
    //int userId = (int) request.getProperty("user");
    return myItemUCC.addItem(itemDTO, 1);
  }

  @GET
  @Path("itemDetails/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public ItemDTO getItemDetails(@PathParam("id") int id) {
    if (id <= 0) {
      throw new WebApplicationException("bad request no id found in pathParams");
    }
    return myItemUCC.getDetails(id);
  }

  @POST
  @Path("addInterest/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public void addInterest(@PathParam("id") int itemId, ObjectNode body) {
    // ajouter le conextManageur pour savoir qui a fait la demande et pouvoir l'utiliser dans les autrres méthode
    if (!body.hasNonNull("availabilities") || itemId <= 0) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("information is missing").type("text/plain").build());
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    LocalDateTime dateTime = LocalDateTime.parse(body.get("availabilities").asText(), formatter);
    var dateAvailable = dateTime.format(formatter);
    body.put("dateFormatted", dateAvailable);
    var callMe = body.get("callMe").asBoolean();
    var phoneNumber = body.get("phoneNumber").asText();
    int userId = 1;
    if (callMe && !phoneNumber.isBlank()) {
      myUserUCC.addPhoneNumber(userId, phoneNumber);
    }
    myItemUCC.addInterest(itemId, body, userId);
  }

  @POST
  @Path("cancelOffer/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public void cancelOffer(@PathParam("id") int itemId) {
    // ajouter le conextManageur pour savoir qui a fait la demande et pouvoir l'utiliser dans les autrres méthode
    if (itemId <= 0) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("information is missing").type("text/plain").build());
    }
    //context jsp quoi vérifier que l'id est bien le même que celui recu dans le context manageur
    int userId = 1;
    myItemUCC.cancelOffer(itemId, userId);
  }

  @GET
  @Path("lastItemsOfferedNotConnected")
  @Produces(MediaType.APPLICATION_JSON)
  public List<ItemDTO> getLastItemsOfferedNotConnected() {
    return myItemUCC.getLastItemsOfferedNotConnected(false);
  }

  @GET
  @Path("lastItemsOfferedConnected")
  @Produces(MediaType.APPLICATION_JSON)
  public List<ItemDTO> getLastItemsOfferedConnected() {
    return myItemUCC.getLastItemsOfferedNotConnected(true);
  }
}