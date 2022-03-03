package buiseness.domain;

import org.mindrot.jbcrypt.BCrypt;

public class UserImpl implements User, UserDTO {

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
