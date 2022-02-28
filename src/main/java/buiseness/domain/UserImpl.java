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
    private int id;
    private String mdp;
    private String pseudo;
    private String prenom;
    private String etat;
    private final String[] etatPossible = {"accepté","attente","refusé"};
    private Adresse adr;
    // on pourrait faire un boolean ici
    private String role;
    private String txt_refus;
    private String num_tel;
    private String url_photo;

    public UserImpl() {
    }

    @Override
    public boolean verifMdp(String mdp) {
        return BCrypt.checkpw(mdp, this.mdp);
    }

    @Override
    public String hashMdp(String mdp) {
        return BCrypt.hashpw(mdp, BCrypt.gensalt());    }

    @Override
    public ObjectNode creeToken(int id, String pseudo) {
        String token;
        try {
            token = JWT.create().withIssuer("auth0")
                    .withClaim("utilisateur", id).sign(this.jwtAlgorithm);
            ObjectNode publicUser = jsonMapper.createObjectNode()
                    .put("token", token)
                    .put("id", id)
                    .put("pseudo", pseudo);
            return publicUser;
        } catch (Exception e) {
            System.out.println("Unable to create token");
            return null;
        }
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
        this.id=id;
    }

    @Override
    public String getMdp() {
        return mdp;
    }

    @Override
    public void setMdp(String mdp) {
        this.mdp=mdp;
    }

    @Override
    public String getEtat() {
        return etat;
    }
}