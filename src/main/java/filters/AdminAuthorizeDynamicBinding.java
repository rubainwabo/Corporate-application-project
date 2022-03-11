package filters;

import ihm.UserRessource;
import jakarta.ws.rs.container.DynamicFeature;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.FeatureContext;

public class AdminAuthorizeDynamicBinding implements DynamicFeature {
    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {
        if (UserRessource.class.equals(resourceInfo.getResourceClass())
                && resourceInfo.getResourceMethod()
                .getName().contains("admin")) {
            System.out.println("ADMIN LOCATED");
            context.register(AdminAuthorizeRequestFilter.class);
        }
}
}
