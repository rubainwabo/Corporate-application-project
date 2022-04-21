package be.vinci.pae.main;

import buiseness.ucc.ItemTypeUCC;
import buiseness.ucc.ItemTypeUCCImpl;
import buiseness.ucc.ItemUCC;
import buiseness.ucc.ItemUCCImpl;
import buiseness.ucc.UserUCC;
import buiseness.ucc.UserUCCImpl;
import dal.DalServices;
import dal.DalServicesImpl;
import dal.services.DateDAO;
import dal.services.DateDAOImpl;
import dal.services.ItemDAO;
import dal.services.ItemDAOImpl;
import dal.services.ItemTypeDAO;
import dal.services.ItemTypeDAOImpl;
import dal.services.UserDAO;
import dal.services.UserDAOImpl;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.mockito.Mockito;

@Provider
public class TestApplicationBinder extends AbstractBinder {

  protected void configure() {
    bind(Mockito.mock(DalServicesImpl.class)).to(DalServices.class);
    bind(Mockito.mock(UserDAOImpl.class)).to(UserDAO.class);
    bind(Mockito.mock(ItemDAOImpl.class)).to(ItemDAO.class);
    bind(Mockito.mock(DateDAOImpl.class)).to(DateDAO.class);
    bind(Mockito.mock(ItemTypeDAOImpl.class)).to(ItemTypeDAO.class);
    bind(UserUCCImpl.class).to(UserUCC.class).in(Singleton.class);
    bind(ItemUCCImpl.class).to(ItemUCC.class).in(Singleton.class);
    bind(ItemTypeUCCImpl.class).to(ItemTypeUCC.class).in(Singleton.class);
  }
}
