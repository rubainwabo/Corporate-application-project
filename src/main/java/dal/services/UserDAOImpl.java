package dal.services;

import buiseness.domain.UserDTO;
import buiseness.factory.BizFactory;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dal.DalServices;
import jakarta.inject.Inject;
import utils.Config;

import java.sql.PreparedStatement;

public class UserDAOImpl implements UserDAO {
    @Inject
    private BizFactory myDomainFactory;
    @Inject
    private DalServices myDalService;
    private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
    private final ObjectMapper jsonMapper = new ObjectMapper();

    public UserDTO login(UserDTO user) {
        PreparedStatement ps = myDalService.getPs("INSERT INTO projet.personnes values ");
        ps.executeQuery();
        if (user == null)return null;
        String token;
        try {
            token = JWT.create().withIssuer("auth0")
                    .withClaim("user", user.getId()).sign(this.jwtAlgorithm);
            ObjectNode publicUser = jsonMapper.createObjectNode()
                    .put("token", token)
                    .put("id", user.getId())
                    .put("login", user.getLogin());
            return publicUser;
        } catch (Exception e) {
            System.out.println("Unable to create token");
            return null;
        }
    }

}
