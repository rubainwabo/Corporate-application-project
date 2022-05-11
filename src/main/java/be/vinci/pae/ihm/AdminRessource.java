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
import jakarta.ws.rs.PUT;
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
  @Path("filtered")
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
  @Path("members")
  @Produces(MediaType.APPLICATION_JSON)
  public List<UserDTO> adminListByState(@QueryParam("state") String state) {
    if (state.isBlank()) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("un état est obligatoire").type("text/plain").build());
    }
    return myUserUCC.getUsersByState(state);
  }

  /**
   * Change the state of a certain user.
   *
   * @param body the data that the user has entered put in json format
   * @return true or false if state successfully changed.
   */
  @PUT
  @Path("update/state/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public boolean adminChangeState(JsonNode body, @PathParam("id") int userId) {
    if (userId <= 0 || !body.hasNonNull("state")) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("le champ id est obligatoire").type("text/plain").build());
    }

    if (!body.get("state").asText().equals("denied") && body.hasNonNull("refusalReason")) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("vous ne pouvez pas mettre une raison de refus autre qu'un état refusé")
          .type("text/plain").build());
    }

    if (body.get("state").asText().equals("denied") && (!body.hasNonNull("refusalReason")
        || body.get("refusalReason").asText().isBlank())) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("vous devez mettre une raison de denie si vous voulez denier quelqu'un")
          .type("text/plain").build());
    }
    if (body.hasNonNull("refusalReason")) {
      return myUserUCC.changeState(userId, body.get("state").asText(),
          body.get("refusalReason").asText(), body.get("admin").asBoolean(),
          body.get("version").asInt());
    } else {
      return myUserUCC.changeState(userId, body.get("state").asText(), "",
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
  @Path("items/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public List<ItemDTO> adminAllMemberItemsByItemCondition(
      @QueryParam("itemCondition") String itemCondition, @PathParam("id") int userId,
      @QueryParam("isOfferor") boolean isOfferor) {
    if (userId <= 0 || itemCondition.isBlank()) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("informations sont manquantes").type("text/plain").build());
    }
    return myItemUCC.memberItemsByItemCondition(itemCondition, userId, isOfferor);
  }

  /**
   * change all item condition for a new invalid user.
   *
   * @param memberId id of the member.
   * @return if nothing gonna wrong, return status 200
   */
  @PUT
  @Path("update/invalidMember/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response adminInvalideMember(@PathParam("id") int memberId) {
    if (memberId <= 0) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("informations invalides").type("text/plain").build());
    }
    myItemUCC.updateItemOfInvalidMember(memberId);
    return Response.ok().build();
  }
}
