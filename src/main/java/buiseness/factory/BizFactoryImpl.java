package buiseness.factory;

import buiseness.domain.UserDTO;
import buiseness.domain.UserImpl;

public class BizFactoryImpl implements BizFactory{
    public UserDTO getUser(){
        return new UserImpl();
    }
}