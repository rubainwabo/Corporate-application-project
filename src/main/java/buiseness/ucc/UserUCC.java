package buiseness.ucc;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface UserUCC {

  ObjectNode seConnecter(String pseudo, String password);
}
