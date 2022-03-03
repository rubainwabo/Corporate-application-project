package be.vinci.pae.main;

import buiseness.ucc.UserUCC;
import buiseness.ucc.UserUCCImpl;
import dal.services.UserDAO;
import dal.services.UserDAOImpl;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.mockito.Mockito;

@Provider
public class ApplicationBinder extends AbstractBinder {

  protected void configure() {
    bind(Mockito.mock(UserDAOImpl.class)).to(UserDAO.class);
    bind(Mockito.mock(UserUCCImpl.class)).to(UserUCC.class);

  }
}
