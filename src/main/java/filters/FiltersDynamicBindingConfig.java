package filters;

import ihm.UserRessource;
import jakarta.ws.rs.container.DynamicFeature;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.FeatureContext;


public class FiltersDynamicBindingConfig implements DynamicFeature {

  @Override
  public void configure(ResourceInfo resourceInfo, FeatureContext context) {
    if (UserRessource.class.equals(resourceInfo.getResourceClass())
    ) {
      if (resourceInfo.getResourceMethod().getName().contains("user")) {
        context.register(AuthorizationRequestFilter.class);
      }
      if (resourceInfo.getResourceMethod().getName().contains("admin")) {
        //When you're admin u want to check if the user has refreshToken to access to pages aswell
        //then check if user is admin.
        context.register(AuthorizationRequestFilter.class, 1);
        context.register(AdminAuthorizeRequestFilter.class, 2);
      }
    }
  }
}
