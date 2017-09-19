import Business.*;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by JelleSchrader on 12-9-2017.
 */


public class DatabaseTest {
    private Connection connection;
    private Properties properties;
    private String user = "dbi299244";
    private String pass = "PTS3Groep1";
            //"Server=mssql.fhict.local;Database=dbi299244;User Id=dbi299244;Password=PTS3Groep1;";

    private Properties getProperties() {
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

    public static Connection connect(){
        try{
            return DriverManager.getConnection("Server=mssql.fhict.local;Database=dbi299244;", "dbi299244", "PTS3Groep1");
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }
    public void disconnect(){
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    
    }
    
    public ArrayList<Cards> getCardsFromDeck(String deck){
        
    }
    

}
