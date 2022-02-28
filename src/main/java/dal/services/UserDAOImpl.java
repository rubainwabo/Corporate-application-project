package dal.services;

import buiseness.domain.UserDTO;
import buiseness.factory.BizFactory;
import dal.DalServices;
import jakarta.inject.Inject;

public class UserDAOImpl implements UserDAO {
    @Inject
    private BizFactory myDomainFactory;
    @Inject
    private DalServices myDalService;

    /**
     * @param pseudo
     * @return
     */
    public UserDTO getOne(String pseudo) {
        /*
        //PreparedStatement ps = myDalService.getPs("INSERT INTO projet.personnes values()");
        //ps.executeQuery();
         */
        return null;
    }
}
