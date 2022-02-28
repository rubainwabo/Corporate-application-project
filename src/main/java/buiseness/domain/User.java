package buiseness.domain;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface User extends UserDTO {

  boolean verifMdp(String mdp);

  String hashMdp(String mdp);

  ObjectNode creeToken(int id, String pseudo);

  boolean checkEtat(String etat);
}
