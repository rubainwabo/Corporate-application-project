package buiseness.ucc;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface UserUCC {

  ObjectNode login(String pseudo, String password,boolean rememberMe);
}
