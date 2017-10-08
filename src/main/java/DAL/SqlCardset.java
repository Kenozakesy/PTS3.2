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

    private SqlCard sqlcard;

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

    public Cardset getCardsetById(int cardsetId){
        try{
            sqlcard = new SqlCard();
            sqlMain.setStatement(sqlMain.getConnection().createStatement());
            String query = "SELECT * FROM Deck WHERE DeckId = ?;";
            
            PreparedStatement ps = sqlMain.getConnection().prepareStatement(query);
            ps.setInt(1, cardsetId);
            
            sqlMain.setResult(ps.executeQuery());
            
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
