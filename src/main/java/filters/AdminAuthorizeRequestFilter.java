package filters;
import java.io.IOException;
import buiseness.domain.User;
import buiseness.ucc.UserUCC;
import jakarta.inject.Inject;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;
import utils.TokenService;
//TODO Beaucoup de duplicata de code : trouver une solution pour éviter ça
@Singleton
@Provider
public class AdminAuthorizeRequestFilter implements ContainerRequestFilter {
    @Inject
    private UserUCC myUserUCC;
    @Inject
    TokenService myTokenService;
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        System.out.println("We're in adminAuthorize");
        String token = requestContext.getHeaderString("token");
        String refreshToken = requestContext.getHeaderString("refreshToken");
        if (token == null && refreshToken == null) {
            requestContext.abortWith(Response.status(Status.UNAUTHORIZED)
                    .entity("A token is needed to access this resource").build());
        } else {
            DecodedJWT decodedToken;
            try {
                decodedToken = (token == null) ? myTokenService.getVerifyRefreshToken(refreshToken): myTokenService.getVerifyToken(token);
            } catch (Exception e) {
                throw new WebApplicationException(Response.status(Status.UNAUTHORIZED)
                        .entity("Malformed token : " + e.getMessage()).type("text/plain").build());
            }
            User authenticatedUser = myUserUCC.getOneById(decodedToken.getClaim("user").asInt());

            if (authenticatedUser == null || authenticatedUser.isDenied() || authenticatedUser.isWaiting() || !authenticatedUser.isAdmin()) {
                requestContext.abortWith(Response.status(Status.FORBIDDEN)
                        .entity("You are forbidden to access this resource").build());
            }
            requestContext.setProperty("user",
                    myUserUCC.getOneById(decodedToken.getClaim("user").asInt()));
        }
    }
}

