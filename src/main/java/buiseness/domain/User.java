package buiseness.domain;

public interface User extends UserDTO {

  boolean verifMdp(String mdp);

  String hashMdp(String mdp);

  boolean checkEtat(String etat);
}
