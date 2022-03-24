package filters;
import buiseness.ucc.UserUCC;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;


@Singleton
@Provider
public class AdminAuthorizeRequestFilter implements ContainerRequestFilter {
    @Inject
    private UserUCC myUserUCC;
    @Override
    public void filter(ContainerRequestContext requestContext) {
        System.out.println("We're in AdminAuthorizeRequestFilter");
        int id = (int) requestContext.getProperty("id");
        if (!myUserUCC.checkAdmin(id)){
            requestContext.abortWith(Response.status(Status.FORBIDDEN).entity("You are forbidden to access this resource").build());
            }

        }
    }


