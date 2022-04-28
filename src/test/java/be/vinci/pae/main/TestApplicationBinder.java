package be.vinci.pae.main;

import be.vinci.pae.buiseness.ucc.ItemTypeUCC;
import be.vinci.pae.buiseness.ucc.ItemTypeUCCImpl;
import be.vinci.pae.buiseness.ucc.ItemUCC;
import be.vinci.pae.buiseness.ucc.ItemUCCImpl;
import be.vinci.pae.buiseness.ucc.UserUCC;
import be.vinci.pae.buiseness.ucc.UserUCCImpl;
import be.vinci.pae.dal.DalServices;
import be.vinci.pae.dal.DalServicesImpl;
import be.vinci.pae.dal.services.DateDAO;
import be.vinci.pae.dal.services.DateDAOImpl;
import be.vinci.pae.dal.services.ItemDAO;
import be.vinci.pae.dal.services.ItemDAOImpl;
import be.vinci.pae.dal.services.ItemTypeDAO;
import be.vinci.pae.dal.services.ItemTypeDAOImpl;
import be.vinci.pae.dal.services.UserDAO;
import be.vinci.pae.dal.services.UserDAOImpl;
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
