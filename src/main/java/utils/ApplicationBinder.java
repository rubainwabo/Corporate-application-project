package utils;

import buiseness.factory.BizFactory;
import buiseness.factory.BizFactoryImpl;
import buiseness.ucc.UserUCC;
import buiseness.ucc.UserUCCImpl;
import dal.DalBackService;
import dal.DalServices;
import dal.DalServicesImpl;
import dal.services.UserDAO;
import dal.services.UserDAOImpl;
import filters.AdminAuthorize;
import filters.AdminAuthorizeRequestFilter;
import filters.AuthorizationRequestFilter;
import filters.Authorize;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

@Provider
public class ApplicationBinder extends AbstractBinder {

  protected void configure() {
    bind(BizFactoryImpl.class).to(BizFactory.class).in(Singleton.class);
    bind(UserDAOImpl.class).to(UserDAO.class).in(Singleton.class);
    bind(UserUCCImpl.class).to(UserUCC.class).in(Singleton.class);
    bind(DalServicesImpl.class).to(DalServices.class).to(DalBackService.class).in(Singleton.class);
    bind(TokenServiceImpl.class).to(TokenService.class).in(Singleton.class);
    bind(AuthorizationRequestFilter.class).to(Authorize.class).in(Singleton.class);
    bind(AdminAuthorizeRequestFilter.class).to(AdminAuthorize.class).in(Singleton.class);

  }
}
