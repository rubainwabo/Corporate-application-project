package dal;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface DalServices {

  PreparedStatement getPreparedStatement(String query) throws SQLException;
}
