package buiseness.ucc;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface UserUCC {
    public ObjectNode seConnecter(String pseudo, String password);
}
