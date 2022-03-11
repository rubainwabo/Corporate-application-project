package filters;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

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
//TODO Changer poiur ne pas passer le role dans le heaader (ça a pas de sens de faire ça comme ça)
@Singleton
@Provider
@AdminAuthorize
public class AdminAuthorizeRequestFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        System.out.println("HERRRRE");
    }
}

