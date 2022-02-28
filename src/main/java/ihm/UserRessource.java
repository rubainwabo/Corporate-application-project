package ihm;

import buiseness.ucc.UserUCC;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dal.services.UserDAO;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Singleton
@Path("/auths")
public class UserRessource {
    @Inject
    private UserDAO myUserDataService;
    @Inject
    private UserUCC myUserUCC;

    /**
     * @param user
     * @return
     */
    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ObjectNode login(JsonNode user) {
        if (!user.hasNonNull("pseudo") || !user.hasNonNull("mdp")) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
                    .entity("login or password required").type("text/plain").build());
        }
        String pseudo = user.get("pseudo").asText();
        String mdp = user.get("mdp").asText();
        ObjectNode token = myUserUCC.seConnecter(pseudo,mdp);
        if (token == null) throw new WebApplicationException();
        return token;
    }
}
