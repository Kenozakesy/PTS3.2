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
public class SqlCard {
    private Connection connection;
    private Properties properties;
    private String user = "dbi299244";
    private String pass = "PTS3Groep1";
            //"Server=mssql.fhict.local;Database=dbi299244;User Id=dbi299244;Password=PTS3Groep1;";

    SqlCardset sqlCardset;
    
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
    public ArrayList<Cards> getAllCards(){
        try{
            sqlCardset = new SqlCardset();
            connection = connect();
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM Card;";
            
            ResultSet result = statement.executeQuery(query);
            
            ArrayList<Cards> cards = new ArrayList<Cards>();
            while(result.next()) {
                int id = result.getInt(0);
                String name = result.getString(1);
                Boolean blanc = result.getBoolean(2);
                Cardset cs = sqlCardset.getCardsetById(result.getInt(3));
                
                Cards card = new PlayCard(id, name, cs, blanc);
                System.out.println(card.getId() + " " + card.getText() + " " + ((PlayCard)card).getBlank());
                cards.add(card);
            }
            return cards;
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public ArrayList<Cards> getAllCardsFromCardSet(Cardset cardset) {
        try{
            sqlCardset = new SqlCardset();
            connection = connect();
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM Card WHERE DeckId = ?;";
            
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, cardset.getId());
            
            ResultSet result = statement.executeQuery(query);
            
            ArrayList<Cards> cards = new ArrayList<Cards>();
            while(result.next()) {
                int id = result.getInt(0);
                String name = result.getString(1);
                Boolean blanc = result.getBoolean(2);
                
                Cards card = new PlayCard(id, name, cardset, blanc);
                System.out.println(card.getId() + " " + card.getText() + " " + ((PlayCard)card).getBlank());
                cards.add(card);
            }
            return cards;
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public Cards getCardById(int cardId){
        try{
            sqlCardset = new SqlCardset();
            connection = connect();
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM Card WHERE Id = ?;";
            
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, cardId);
            
            ResultSet result = statement.executeQuery(query);
            
            Cards card = null;
            while(result.next()) {
                int id = result.getInt(0);
                String name = result.getString(1);
                Boolean blanc = result.getBoolean(2);
                Cardset cardset = sqlCardset.getCardsetById(result.getInt(3));
                
                card = new PlayCard(id, name, cardset, blanc);
            }
            return card;
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
