package utils;

import buiseness.domain.bizclass.ItemType;
import buiseness.domain.impl.ItemTypeImpl;
import buiseness.factory.BizFactory;
import buiseness.factory.BizFactoryImpl;
import buiseness.ucc.ItemTypeUCC;
import buiseness.ucc.ItemTypeUCCImpl;
import buiseness.ucc.ItemUCC;
import buiseness.ucc.ItemUCCImpl;
import buiseness.ucc.UserUCC;
import buiseness.ucc.UserUCCImpl;
import dal.DalBackService;
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

  }
}
