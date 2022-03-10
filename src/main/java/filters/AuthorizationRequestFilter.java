package filters;
import java.io.IOException;
import java.util.Arrays;

import buiseness.domain.User;
import buiseness.ucc.UserUCC;
import dal.services.UserDAO;
import dal.services.UserDAOImpl;
import jakarta.inject.Inject;
import utils.Config;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;
import utils.TokenService;

@Singleton
@Provider
@Authorize
public class AuthorizationRequestFilter implements ContainerRequestFilter {
    @Inject
    private UserUCC myUserUCC;
    @Inject
    TokenService myTokenService;
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String token = requestContext.getHeaderString("token");
        String refreshToken = requestContext.getHeaderString("refreshToken");
        String role = requestContext.getHeaderString("role");

        if (token == null && refreshToken == null) {
            requestContext.abortWith(Response.status(Status.UNAUTHORIZED)
                    .entity("A token is needed to access this resource").build());
        } else {
            DecodedJWT decodedToken;
            try {
                decodedToken = (token == null) ? myTokenService.getVerifyToken(refreshToken, false) : myTokenService.getVerifyToken(token, true);
            } catch (Exception e) {
                throw new WebApplicationException(Response.status(Status.UNAUTHORIZED)
                        .entity("Malformed token : " + e.getMessage()).type("text/plain").build());
            }
            User authenticatedUser = myUserUCC.getOneById(decodedToken.getClaim("user").asInt());

            if (authenticatedUser == null || authenticatedUser.isDenied() || authenticatedUser.isWaiting()) {
                requestContext.abortWith(Response.status(Status.FORBIDDEN)
                        .entity("You are forbidden to access this resource").build());
            }
            if (authenticatedUser.getState().equals("admin") && role.equals("admin")) {
                requestContext.abortWith(Response.status(Status.FORBIDDEN)
                        .entity("You are forbidden to access this resource must be admin").build());
            }
            requestContext.setProperty("user",
                    myUserUCC.getOneById(decodedToken.getClaim("user").asInt()));
        }
    }

}

