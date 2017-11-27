/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dal;

import business.*;
import java.sql.*;
import java.util.*;

/**
 *
 * @author JelleSchrader
 */
public class SqlCardSet {
    private SqlConnection sqlConnection = new SqlConnection();

    public List<CardSet> getAllCardSets(){
        List<CardSet> sets = new ArrayList<>();

        try{
            sqlConnection.setStatement(sqlConnection.getConnection().createStatement());
            String query = "SELECT * FROM Deck;";

            sqlConnection.setResult(sqlConnection.getStatement().executeQuery(query));
            
            while(sqlConnection.getResult().next()){
                int id = sqlConnection.getResult().getInt(1);
                String name = sqlConnection.getResult().getString(2);
                
                CardSet set = new CardSet(id, name);

                sets.add(set);
            }
        } catch(Exception ex){
            ex.printStackTrace();
        }
        finally{
            sqlConnection.closeAll();
        }

        return sets;
    }

    public CardSet getCardSetById(int cardsetId){
        try{
            sqlConnection.setStatement(sqlConnection.getConnection().createStatement());
            String query = "SELECT * FROM Deck WHERE DeckId = ?;";
            
            PreparedStatement ps = sqlConnection.getConnection().prepareStatement(query);
            ps.setInt(1, cardsetId);
            
            sqlConnection.setResult(ps.executeQuery());
            
            CardSet cardSet = null;
            while(sqlConnection.getResult().next()) {
                int id = sqlConnection.getResult().getInt(1);
                String name = sqlConnection.getResult().getString(2);
                
                cardSet = new CardSet(id, name);
            }
            return cardSet;
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
        finally{
            sqlConnection.closeAll();
        }
    }
}
