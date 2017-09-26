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
    private SqlMain sqlMain = new SqlMain();
    private Properties properties;
    private String user = "dbi299244";
    private String pass = "PTS3Groep1";
            //"Server=mssql.fhict.local;Database=dbi299244;User Id=dbi299244;Password=PTS3Groep1;";

    private SqlCard sqlcard;
    
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

    public ArrayList<Cardset> getAllCardsets(){
        try{
            sqlcard = new SqlCard();
            sqlMain.setStatement(sqlMain.getConnection().createStatement());
            String query = "SELECT * FROM Deck;";
            sqlMain.setResult(sqlMain.getStatement().executeQuery(query));
            
            ArrayList<Cardset> sets = new ArrayList<Cardset>();
            
            while(sqlMain.getResult().next()){
                int id = sqlMain.getResult().getInt(1);
                String name = sqlMain.getResult().getString(2);
                
                Cardset set = new Cardset(id, name);
                
                ArrayList<Cards> cards = sqlcard.getAllCardsFromCardSet(set);
                set.setCardsInCardset(cards);
                
                System.out.println(set.getId() + " " + set.getName());
                sets.add(set);
            }
            return sets;
        } catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
        finally{
            sqlMain.closeAll();
        }
    }
    public ArrayList<Cards> getAllCardsFromCardSet(int cardsetId){
        return null;
    }
    public Cardset getCardsetById(int cardsetId){
        try{
            sqlcard = new SqlCard();
            sqlMain.setStatement(sqlMain.getConnection().createStatement());
            String query = "SELECT * FROM Deck WHERE Id = ?;";
            
            PreparedStatement ps = sqlMain.getConnection().prepareStatement(query);
            ps.setInt(1, cardsetId);
            
            sqlMain.setResult(sqlMain.getStatement().executeQuery(query));
            
            Cardset cardset = null;
            while(sqlMain.getResult().next()) {
                int id = sqlMain.getResult().getInt(1);
                String name = sqlMain.getResult().getString(2);
                
                cardset = new Cardset(id, name);
            }
            return cardset;
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
        finally{
            sqlMain.closeAll();
        }
    }
}
