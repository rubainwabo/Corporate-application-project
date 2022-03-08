package be.vinci.pae.main;

import buiseness.ucc.UserUCC;
import buiseness.ucc.UserUCCImpl;
import dal.services.UserDAO;
import dal.services.UserDAOImpl;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.mockito.Mockito;
import utils.TokenService;
import utils.TokenServiceImpl;

@Provider
public class TestApplicationBinder extends AbstractBinder {

  protected void configure() {
    bind(Mockito.mock(UserDAOImpl.class)).to(UserDAO.class);
    bind(Mockito.mock(TokenServiceImpl.class)).to(TokenService.class);
    bind(UserUCCImpl.class).to(UserUCC.class).in(Singleton.class);
  }
}
