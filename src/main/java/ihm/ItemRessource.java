package ihm;

import buiseness.dto.ItemDTO;
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
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.util.List;
import org.glassfish.jersey.server.ContainerRequest;

@Singleton
@Path("/items")
public class ItemRessource {

  @Inject
  private ItemUCC myItemUCC;

  @Inject
  private UserUCC myUserUCC;


  /**
   * gets list of offers of the user.
   *
   * @return items
   */
  @GET
  @Path("mesOffres")
  @Produces(MediaType.APPLICATION_JSON)
  public List<ItemDTO> userMesOffres(@Context ContainerRequest req) {
    int id = (int) req.getProperty("id");
    return myItemUCC.getAllItemsOffered(id);
  }

  /**
   * retrives to add an item to the DB.
   *
   * @param itemDTO the item transoformed into an java object by jackson
   * @return the id of the item insered
   */
  @POST
  @Path("add")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public int userAddItem(ItemDTO itemDTO, @Context ContainerRequest req) {
    if (itemDTO == null || itemDTO.getDescription().isBlank() || itemDTO.getItemtype().isBlank()
        || itemDTO.getTimeSlot().isBlank()) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("info de l'objet incorrectes").type("text/plain").build());
    }
    int id = (int) req.getProperty("id");
    return myItemUCC.addItem(itemDTO, id);
  }

  /**
   * get details of a specific item.
   *
   * @param id the id of the item we want to have details
   * @return the itemDTO find in the DB
   */
  @GET
  @Path("itemDetails/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public ItemDTO userGetItemDetails(@PathParam("id") int id) {
    if (id <= 0) {
      throw new WebApplicationException("Aucun object existant avec cet id");
    }
    return myItemUCC.getDetails(id);
  }

  /**
   * retrives to add an interest to a specific item.
   *
   * @param itemId the itemId
   * @param body   the phone number,callMe and date where the user want to meet the offeror)
   */
  @POST
  @Path("addInterest/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response userAddInterest(@PathParam("id") int itemId, ObjectNode body,
      @Context ContainerRequest req) {
    if (!body.hasNonNull("availabilities") || itemId <= 0) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("informations manquantes").type("text/plain").build());
    }
    boolean callMe = body.hasNonNull("callMe") && body.get("callMe").asBoolean();
    boolean updateNumber = body.hasNonNull("updateNumber") && body.get("updateNumber").asBoolean();
    String phoneNumber = body.hasNonNull("phoneNumber") ? body.get("phoneNumber").asText() : "";

    int userId = (int) req.getProperty("id");
    if (callMe && !phoneNumber.isBlank() && updateNumber) {
      myUserUCC.addPhoneNumber(userId, phoneNumber);
    }
    myItemUCC.addInterest(itemId, body, userId);
    return Response.ok().build();
  }

  /**
   * retrives to cancel an offer.
   *
   * @param itemId the id of the item we want to cancel
   */
  @POST
  @Path("cancelOffer/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response userCancelOffer(@PathParam("id") int itemId, @Context ContainerRequest req) {
    if (itemId <= 0) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("information manquante").type("text/plain").build());
    }
    int userId = (int) req.getProperty("id");
    myItemUCC.cancelOffer(itemId, userId);
    return Response.ok().build();
  }

  /**
   * retrives to get all the list of last object added with the itemCondition 'offered'.
   *
   * @return a list with all the last items
   */
  @GET
  @Path("lastItemsOfferedNotConnected")
  @Produces(MediaType.APPLICATION_JSON)
  public List<ItemDTO> getLastItemsOfferedNotConnected() {
    return myItemUCC.getLastItemsOffered(false);
  }

  /**
   * retrives to get all the list of last object added with the itemCondition 'offered'.
   *
   * @return a list with all the last items
   */
  @GET
  @Path("lastItemsOfferedConnected")
  @Produces(MediaType.APPLICATION_JSON)
  public List<ItemDTO> userGetLastItemsOfferedConnected() {
    return myItemUCC.getLastItemsOffered(true);
  }

  @POST
  @Path("itemCollected")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response userItemCollectedOrNot(@Context ContainerRequest req,
      @QueryParam("itemId") int itemId,
      @QueryParam("itemCollected") boolean itemCollected) {

    if (itemId <= 0) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("informations manquantes").type("text/plain").build());
    }
    int reqUserId = (int) req.getProperty("id");
    int realUserId = myItemUCC.checkUserEligibility(reqUserId, itemId);

    if (realUserId != reqUserId) {
      throw new WebApplicationException(Response.status(Status.UNAUTHORIZED)
          .entity("l'utilisateur effectuant cette requête n'est pas le receveur")
          .type("text/plain").build());
    }
    ItemDTO item = myItemUCC.getDetails(itemId);
    myItemUCC.ItemCollectedOrNot(item, itemCollected, reqUserId);
    return Response.ok().build();
  }
}