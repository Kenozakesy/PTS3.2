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
    private SqlConnection sqlConnection = new SqlConnection();

    private SqlCard sqlcard;

    public ArrayList<Cardset> getAllCardsets(){
        try{
            sqlcard = new SqlCard();
            sqlConnection.setStatement(sqlConnection.getConnection().createStatement());
            String query = "SELECT * FROM Deck;";

            sqlConnection.setResult(sqlConnection.getStatement().executeQuery(query));
            
            ArrayList<Cardset> sets = new ArrayList<Cardset>();
            
            while(sqlConnection.getResult().next()){
                int id = sqlConnection.getResult().getInt(1);
                String name = sqlConnection.getResult().getString(2);
                
                Cardset set = new Cardset(id, name);

                sets.add(set);
            }
            return sets;
        } catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
        finally{
            sqlConnection.closeAll();
        }
    }

    public Cardset getCardsetById(int cardsetId){
        try{
            sqlcard = new SqlCard();
            sqlConnection.setStatement(sqlConnection.getConnection().createStatement());
            String query = "SELECT * FROM Deck WHERE DeckId = ?;";
            
            PreparedStatement ps = sqlConnection.getConnection().prepareStatement(query);
            ps.setInt(1, cardsetId);
            
            sqlConnection.setResult(ps.executeQuery());
            
            Cardset cardset = null;
            while(sqlConnection.getResult().next()) {
                int id = sqlConnection.getResult().getInt(1);
                String name = sqlConnection.getResult().getString(2);
                
                cardset = new Cardset(id, name);
            }
            return cardset;
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
        finally{
            sqlConnection.closeAll();
        }
    }
}
