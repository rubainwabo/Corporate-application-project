package ihm;

import buiseness.dto.ItemDTO;
import buiseness.ucc.ItemUCC;
import buiseness.ucc.UserUCC;
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
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import javax.imageio.ImageIO;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.server.ContainerRequest;
import utils.Config;
import utils.exception.FatalException;

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
  @Path("myItems/{state}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public List<ItemDTO> userMyItems(@Context ContainerRequest req,
      @PathParam("state") String state) {
    int id = (int) req.getProperty("id");
    return myItemUCC.getMyItems(id, state);
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
  @Path("changeCondition/{id}/{condition}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response userChangeItemCondition(@PathParam("id") int itemId,
      @PathParam("condition") String condition, @Context ContainerRequest req) {
    if (itemId <= 0) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("information manquante").type("text/plain").build());
    }
    int userId = (int) req.getProperty("id");
    myItemUCC.changeItemCondition(itemId, userId, condition);
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

  /**
   * is used to indicate whether a person to whom an object has been offered has come to collect it
   * or not.
   *
   * @param req  the user making the request
   * @param node containing information like the id of the object or if the person came or not.
   * @return 200 if everything went well.
   */
  @POST
  @Path("itemCollected")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response userItemCollectedOrNot(@Context ContainerRequest req, JsonNode node) {

    if (!node.hasNonNull("itemId") || !node.hasNonNull("itemCollected")
        || node.get("itemId").asInt() <= 0) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("informations manquantes").type("text/plain").build());
    }
    int reqUserId = (int) req.getProperty("id");
    int itemId = node.get("itemId").asInt();
    boolean isCollected = node.get("itemCollected").asBoolean();
    myItemUCC.itemCollectedOrNot(itemId, isCollected, reqUserId);
    return Response.ok().build();
  }

  /**
   * retrives to add an item it's recipient.
   *
   * @return a list with all the users
   */
  @POST
  @Path("addRecipient/{idItem}/{idRecipient}")
  @Produces(MediaType.APPLICATION_JSON)
  public int userAddRecipient(@PathParam("idItem") int idItem,
      @PathParam("idRecipient") int idRecipient) {
    if (idItem <= 0 || idRecipient <= 0) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("information is missing").type("text/plain").build());
    }
    return myItemUCC.addRecipient(idItem, idRecipient);

  }

  /**
   * retrives to update an item.
   *
   * @return 1 if everything is correctly done
   */
  @POST
  @Path("update")
  @Produces(MediaType.APPLICATION_JSON)
  public int userUpdateItem(ItemDTO item) {
    return myItemUCC.updateItem(item);
  }

  /**
   * retrives to offer again an item.
   */
  @POST
  @Path("offer/again/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public void userOfferItemAgain(@PathParam("id") int idItem, @Context ContainerRequest req) {
    int userId = (int) req.getProperty("id");
    myItemUCC.offerAgain(idItem, userId);
  }

  @POST
  @Path("upload")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response uploadFile(@FormDataParam("file") InputStream file,
      @FormDataParam("file") FormDataContentDisposition fileDisposition,@FormDataParam("itemId") int itemId) {

    System.out.println(itemId);
    String fileName = fileDisposition.getFileName();
    System.out.println(fileName);
    String path = Config.getProperty("ImgPath");
    try {
      Files.copy(file, Paths.get(path, fileName));
      myItemUCC.updateItemUrl(itemId,fileName);
    } catch (IOException e) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("no image uploaded").type("text/plain").build());
    }
    return Response.ok(fileName).header("Access-Control-Allow-Origin", "*").build();
  }

  @GET
  @Path("picture/{id}")
  @Produces({"image/png","image/jpg","image/jpeg"})
  public Response getPicture(@PathParam("id") int itemId){
    ItemDTO item = myItemUCC.getDetails(itemId);

    try {
      String drivePath = Config.getProperty("ImgPath");
      String imagePath;
      if(item.getUrlPicture()==null || item.getUrlPicture()=="none"){
        imagePath = drivePath+"/"+"image_not_available.png";
      }else{
        imagePath = drivePath+"/"+item.getUrlPicture();
      }
      Image picture = ImageIO.read(new File(imagePath));
      return Response.ok(picture).build();
    } catch (IOException e) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("impossible de recuperer les images").type("text/plain").build());
    }



    /*
    ItemDTO item = myItemUCC.getDetails(itemId);
    if(item.getUrlPicture()!=null || item.getUrlPicture()!="none"){
        System.out.println("hello");
    }
    String path = Config.getProperty("ImgPath");
   */
  }



}
