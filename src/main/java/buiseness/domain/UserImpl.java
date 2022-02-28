package buiseness.domain;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.mindrot.jbcrypt.BCrypt;
import utils.Config;

public class UserImpl implements User, UserDTO {

  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final ObjectMapper jsonMapper = new ObjectMapper();
  private final String[] etatPossible = {"validé", "attente", "refusé"};
  private int id;
  private String mdp;
  private String pseudo;
  private String etat;
  /*
   * private String prenom;
   * private Adresse adr;
   * private boolean role;
   * private String txtRefus;
   * private String numTel;
   * private String urlPhoto;
   */


  public UserImpl() {
  }

  @Override
  public boolean verifMdp(String mdp) {
    System.out.println(BCrypt.checkpw(mdp, this.mdp));
    return BCrypt.checkpw(mdp, this.mdp);
  }

  @Override
  public String hashMdp(String mdp) {
    return BCrypt.hashpw(mdp, BCrypt.gensalt());
  }

  @Override
  public ObjectNode creeToken(int id, String pseudo, boolean rememberMe) {
    String token;
    try {
      token = JWT.create().withIssuer("auth0")
          .withClaim("utilisateur", id).sign(this.jwtAlgorithm);
      ObjectNode publicUser = jsonMapper.createObjectNode()
          .put("token", token)
          .put("id", id)
          .put("pseudo", pseudo)
          .put("rememberMe", rememberMe);

      return publicUser;
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public boolean checkEtat(String etat) {
    for (String e : this.etatPossible) {
      if (e.equals(etat)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String getPseudo() {
    return pseudo;
  }

  @Override
  public void setPseudo(String pseudo) {
    this.pseudo = pseudo;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String getMdp() {
    return mdp;
  }

  @Override
  public void setMdp(String mdp) {
    this.mdp = mdp;
  }

  @Override
  public String getEtat() {
    return this.etat;
  }

  public void setEtat(String etat) {
    this.etat = etat;
  }
}
