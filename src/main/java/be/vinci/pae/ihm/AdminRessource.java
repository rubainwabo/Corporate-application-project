package be.vinci.pae.ihm;

import be.vinci.pae.buiseness.dto.ItemDTO;
import be.vinci.pae.buiseness.dto.UserDTO;
import be.vinci.pae.buiseness.ucc.ItemUCC;
import be.vinci.pae.buiseness.ucc.UserUCC;
import com.fasterxml.jackson.databind.JsonNode;
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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Singleton
@Path("/admins")
public class AdminRessource {

  @Inject
  private UserUCC myUserUCC;
  @Inject
  private ItemUCC myItemUCC;

  /**
   * retrieves a list of users based on received be.vinci.pae.filters.
   *
   * @param name     the name input
   * @param city     the city input
   * @param postCode the postcode input
   * @return a list of users based on received be.vinci.pae.filters.
   */
  @GET
  @Path("list/filtred")
  @Produces(MediaType.APPLICATION_JSON)
  public List<UserDTO> adminMemberListFiltred(@QueryParam("name") String name,
      @QueryParam("city") String city, @QueryParam("postCode") String postCode) {
    return myUserUCC.getAllUsersFiltered(name, city, postCode);
  }

  /**
   * retrieve city,name and postCode from the database in relation to an input.
   *
   * @param val the input val
   * @return a list containing the retrieved data.
   */
  @GET
  @Path("autocompleteList")
  @Produces(MediaType.APPLICATION_JSON)
  public List<String> adminAutoComplete(@QueryParam("value") String val) {
    return myUserUCC.getAutocompleteList(val);
  }

  /**
   * retrives to get all the user with a specific state.
   *
   * @return a list of user by a specific state
   */
  @GET
  @Path("listByState")
  @Produces(MediaType.APPLICATION_JSON)
  public List<UserDTO> adminListByState(@QueryParam("state") String state) {
    if (state.isBlank()) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("a state is required").type("text/plain").build());
    }
    return myUserUCC.getUsersByState(state);
  }

  /**
   * Change the state of a certain user.
   *
   * @param body the data that the user has entered put in json format
   * @return true or false if state successfully changed.
   */
  @POST
  @Path("changeState")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public boolean adminChangeState(JsonNode body) {
    if (!body.hasNonNull("change_id") || !body.hasNonNull("state")) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("id field is required").type("text/plain").build());
    }

    if (!body.get("state").asText().equals("denied") && body.hasNonNull("refusalReason")) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("You cannot put refusal reason on something else than refused state")
          .type("text/plain").build());
    }

    if (body.get("state").asText().equals("denied") && (!body.hasNonNull("refusalReason")
        || body.get("refusalReason").asText().isBlank())) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("You have to put your denial reason if you want to deny someone")
          .type("text/plain").build());
    }
    if (body.hasNonNull("refusalReason")) {
      return myUserUCC.changeState(body.get("change_id").asInt(), body.get("state").asText(),
          body.get("refusalReason").asText(), body.get("admin").asBoolean(),
          body.get("version").asInt());
    } else {
      return myUserUCC.changeState(body.get("change_id").asInt(), body.get("state").asText(), "",
          body.get("admin").asBoolean(), body.get("version").asInt());
    }
  }

  /**
   * Get all members item with a specific item condition.
   *
   * @param itemCondition the item condition
   * @param userId        the user we want's to get items
   * @param isOfferor     boolean to know if he is recipient or offeror
   * @return the list of his items.
   */
  @GET
  @Path("memberListItems/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public List<ItemDTO> adminAllMemberItemsByItemCondition(
      @QueryParam("itemCondition") String itemCondition, @PathParam("id") int userId,
      @QueryParam("isOfferor") boolean isOfferor) {
    if (userId <= 0 || itemCondition.isBlank()) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("Des informations sont manquantes").type("text/plain").build());
    }
    return myItemUCC.memberItemsByItemCondition(itemCondition, userId, isOfferor);
  }
}
