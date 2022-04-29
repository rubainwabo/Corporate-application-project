package filters;

import jakarta.inject.Singleton;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;


@Singleton
@Provider
public class AdminAuthorizeRequestFilter implements ContainerRequestFilter {

  @Override
  public void filter(ContainerRequestContext requestContext) {
    System.out.println("We're in AdminAuthorizeRequestFilter");

    //We already set the admin property
    // in last filter we went through so we just need to check.

    if (!requestContext.getProperty("admin").equals("admin")) {
      requestContext.abortWith(
          Response.status(Status.FORBIDDEN).entity("You need to be admin to access to this ressource")
              .build());
    }
  }
}


