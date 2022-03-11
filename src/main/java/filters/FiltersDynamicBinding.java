package filters;

import ihm.UserRessource;
import jakarta.ws.rs.container.DynamicFeature;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.FeatureContext;


public class FiltersDynamicBinding implements DynamicFeature {
    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {
        if (UserRessource.class.equals(resourceInfo.getResourceClass())
        ) {
        if (resourceInfo.getResourceMethod().getName().contains("admin")) {
            context.register(AdminAuthorizeRequestFilter.class);
        }
        if (resourceInfo.getResourceMethod().getName().contains("user")) {
            context.register(AuthorizationRequestFilter.class);}
}}
}
