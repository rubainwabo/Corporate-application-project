package buiseness.domain;

import org.mindrot.jbcrypt.BCrypt;

public class UserImpl implements User, UserDTO {
  /*
  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTAccess"));
  private final ObjectMapper jsonMapper = new ObjectMapper();
   */
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
    return BCrypt.checkpw(mdp, this.mdp);
  }

  @Override
  public String hashMdp(String mdp) {
    return BCrypt.hashpw(mdp, BCrypt.gensalt());
  }

  /*
  @Override
  public String createToken(int id){
    String token=null;
    // add +- 15 min token life's
    long tokenLifeTime = System.currentTimeMillis() + (1000000);
    try {
      token = JWT.create().withIssuer("auth0")
          .withClaim("user", id).withClaim("date", System.currentTimeMillis())
          .withExpiresAt(new Date(tokenLifeTime)).sign(this.jwtAlgorithm);
    }catch (Exception e){
        e.printStackTrace();
      }
    return token;
    }
    @Override
  public ObjectNode localStorageLogin (int id, String pseudo, boolean rememberMe) {
    String token = this.createToken(id);
    return jsonMapper.createObjectNode()
        .put("token", token)
        .put("id", id)
        .put("pseudo", pseudo)
        .put("rememberMe", rememberMe);
  }

   */

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
