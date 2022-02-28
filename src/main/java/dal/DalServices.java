package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DalServices {
    private Connection con=null;
    private String url = ""; // file properties later
    private String username = "";
    private String password = "";

    public DalServices() throws SQLException{
        con = DriverManager.getConnection(url, username, password);
    }

    public PreparedStatement getPreparedStatement(String query) throws SQLException {
       return con.prepareStatement(query);
    }
}
