import org.junit.Test;

import java.sql.*;
import java.util.Properties;

/**
 * Created by Gebruiker on 12-9-2017.
 */


public class DatabaseTest {
    private Connection connection;
    private Properties properties;
    private String user = "dbi299244";
    private String pass = "PTS3Groep1";
            //"Server=mssql.fhict.local;Database=dbi299244;User Id=dbi299244;Password=PTS3Groep1;";

    private Properties GetProperties() {
        try {
            if (properties == null) {
                properties = new Properties();
                properties.setProperty("user", user);
                properties.setProperty("password", pass);
            }
            return properties;
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    public static Connection Connect(){
        try{
            return DriverManager.getConnection("Server=mssql.fhict.local;Database=dbi299244;", "dbi299244", "PTS3Groep1");
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }
    public void Disconnect(){
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public

}
