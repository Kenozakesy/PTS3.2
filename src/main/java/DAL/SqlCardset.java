/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import Business.*;
import java.sql.*;
import java.util.*;

/**
 *
 * @author JelleSchrader
 */
public class SqlCardset {
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
    // Deze is static omdat we niet weten waarom het static moet zijn, maar er genoeg reden voor waren, alleen die hebben we niet meer onthouden.
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
    public ArrayList<Cardset> getAllCardsets(){
        try{
            connection = connect();
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM Deck;";
            ResultSet result = statement.executeQuery(query);
            
            ArrayList<Cardset> sets = new ArrayList<Cardset>();
            
            while(result.next()){
                int id = result.getInt(0);
                String name = result.getString(1);
                
                Cardset set = new Cardset(id, name);
                
                ArrayList<Cards> cards = sqlCard.get
                set.setCardsInCardset(cards);
                
                System.out.println(set.getId() + " " + set.getName());
                sets.add(set);
            }
            return sets;
        } catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    public ArrayList<Cards> getAllCardsFromCardSet(int cardsetId){
        return null;
    }
    public Cardset getCardsetById(int cardsetId){
        return null;
    }
}
