package be.vinci.pae.utils;

import be.vinci.pae.buiseness.domain.ItemType;
import be.vinci.pae.buiseness.factory.BizFactory;
import be.vinci.pae.buiseness.factory.BizFactoryImpl;
import be.vinci.pae.buiseness.impl.ItemTypeImpl;
import be.vinci.pae.buiseness.ucc.DateUCC;
import be.vinci.pae.buiseness.ucc.DateUCCImpl;
import be.vinci.pae.buiseness.ucc.ItemTypeUCC;
import be.vinci.pae.buiseness.ucc.ItemTypeUCCImpl;
import be.vinci.pae.buiseness.ucc.ItemUCC;
import be.vinci.pae.buiseness.ucc.ItemUCCImpl;
import be.vinci.pae.buiseness.ucc.NotificationUCC;
import be.vinci.pae.buiseness.ucc.NotificationUCCImpl;
import be.vinci.pae.buiseness.ucc.UserUCC;
import be.vinci.pae.buiseness.ucc.UserUCCImpl;
import be.vinci.pae.dal.DalBackService;
import be.vinci.pae.dal.DalServices;
import be.vinci.pae.dal.DalServicesImpl;
import be.vinci.pae.dal.services.DateDAO;
import be.vinci.pae.dal.services.DateDAOImpl;
import be.vinci.pae.dal.services.ItemDAO;
import be.vinci.pae.dal.services.ItemDAOImpl;
import be.vinci.pae.dal.services.ItemTypeDAO;
import be.vinci.pae.dal.services.ItemTypeDAOImpl;
import be.vinci.pae.dal.services.NotificationDAO;
import be.vinci.pae.dal.services.NotificationDAOImpl;
import be.vinci.pae.dal.services.UserDAO;
import be.vinci.pae.dal.services.UserDAOImpl;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

@Provider
public class ApplicationBinder extends AbstractBinder {

  protected void configure() {
    bind(BizFactoryImpl.class).to(BizFactory.class).in(Singleton.class);
    bind(UserDAOImpl.class).to(UserDAO.class).in(Singleton.class);
    bind(UserUCCImpl.class).to(UserUCC.class).in(Singleton.class);
    bind(ItemUCCImpl.class).to(ItemUCC.class).in(Singleton.class);
    bind(ItemDAOImpl.class).to(ItemDAO.class).in(Singleton.class);
    bind(DalServicesImpl.class).to(DalServices.class).to(DalBackService.class).in(Singleton.class);
    bind(TokenServiceImpl.class).to(TokenService.class).in(Singleton.class);
    bind(ItemTypeDAOImpl.class).to(ItemTypeDAO.class).in(Singleton.class);
    bind(DateDAOImpl.class).to(DateDAO.class).in(Singleton.class);
    bind(ItemTypeImpl.class).to(ItemType.class).in(Singleton.class);
    bind(ItemTypeUCCImpl.class).to(ItemTypeUCC.class).in(Singleton.class);
    bind(DateDAOImpl.class).to(DateDAO.class).in(Singleton.class);
    bind(DateUCCImpl.class).to(DateUCC.class).in(Singleton.class);
    bind(NotificationUCCImpl.class).to(NotificationUCC.class).in(Singleton.class);
    bind(NotificationDAOImpl.class).to(NotificationDAO.class).in(Singleton.class);
  }
}
