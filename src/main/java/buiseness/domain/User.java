package buiseness.domain;

public interface User extends UserDTO {

  boolean verifMdp(String mdp);

  String hashMdp(String mdp);

  // String createToken(int id);

  //ObjectNode localStorageLogin (int id, String pseudo, boolean rememberMe);


    boolean checkEtat(String etat);
}
