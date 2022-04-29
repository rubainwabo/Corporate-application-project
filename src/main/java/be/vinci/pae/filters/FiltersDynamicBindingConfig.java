package be.vinci.pae.filters;

import be.vinci.pae.ihm.AdminRessource;
import be.vinci.pae.ihm.DateRessource;
import be.vinci.pae.ihm.ItemRessource;
import be.vinci.pae.ihm.ItemTypeRessource;
import be.vinci.pae.ihm.MemberRessource;
import be.vinci.pae.ihm.NotificationRessource;
import be.vinci.pae.ihm.UserRessource;
import jakarta.ws.rs.container.DynamicFeature;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.FeatureContext;


public class FiltersDynamicBindingConfig implements DynamicFeature {

  @Override
  public void configure(ResourceInfo resourceInfo, FeatureContext context) {
    if (UserRessource.class.equals(resourceInfo.getResourceClass())
        || ItemRessource.class.equals(resourceInfo.getResourceClass())
        || ItemTypeRessource.class.equals(resourceInfo.getResourceClass())
        || MemberRessource.class.equals(resourceInfo.getResourceClass())
        || DateRessource.class.equals(resourceInfo.getResourceClass())
        || AdminRessource.class.equals(resourceInfo.getResourceClass())
        || NotificationRessource.class.equals(resourceInfo.getResourceClass())) {
      if (resourceInfo.getResourceMethod().getName().contains("user")) {
        context.register(UserAuthorizeRequestFilter.class);
      }
      if (resourceInfo.getResourceMethod().getName().contains("admin")) {
        //When you're admin u want to check if the user has refreshToken to access to pages aswell
        //then check if user is admin.
        context.register(UserAuthorizeRequestFilter.class, 1);
        context.register(filters.AdminAuthorizeRequestFilter.class, 2);
      }
    }
  }
}
