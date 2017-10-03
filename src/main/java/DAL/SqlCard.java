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
    private SqlMain sqlMain = new SqlMain();
            //"Server=mssql.fhict.local;Database=dbi299244;User Id=dbi299244;Password=PTS3Groep1;";

    SqlCardset sqlCardset;

    public ArrayList<Cards> getAllCards(){
        try{
            sqlCardset = new SqlCardset();
            sqlMain.setStatement(sqlMain.getConnection().createStatement());
            String query = "SELECT * FROM Card;";
            
            sqlMain.setResult(sqlMain.getStatement().executeQuery(query));
            
            ArrayList<Cards> cards = new ArrayList<Cards>();
            while(sqlMain.getResult().next()) {
                int id = sqlMain.getResult().getInt(1);
                String name = sqlMain.getResult().getString(2);
                Boolean blanc = sqlMain.getResult().getBoolean(3);
                Cardset cs = sqlCardset.getCardsetById(sqlMain.getResult().getInt(4));
                
                Cards card = new PlayCard(id, name, cs, blanc);
                System.out.println(card.getId() + " " + card.getText() + " " + ((PlayCard)card).getBlank());
                cards.add(card);
            }
            return cards;
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
        finally{
            sqlMain.closeAll();
        }
    }

    public ArrayList<Cards> getAllCardsFromCardSet(Cardset cardset) {
        try{
            sqlCardset = new SqlCardset();
            sqlMain.setStatement(sqlMain.getConnection().createStatement());
            String query = "SELECT * FROM Card WHERE DeckId = ?;";
            
            PreparedStatement ps = sqlMain.getConnection().prepareStatement(query);
            ps.setInt(1, cardset.getId());

            sqlMain.setResult(ps.executeQuery());
            
            ArrayList<Cards> cards = new ArrayList<Cards>();
            while(sqlMain.getResult().next()) {
                int id = sqlMain.getResult().getInt(1);
                String name = sqlMain.getResult().getString(2);
                Boolean blanc = sqlMain.getResult().getBoolean(3);
                
                Cards card = new PlayCard(id, name, cardset, blanc);
                System.out.println(card.getId() + " " + card.getText() + " " + ((PlayCard)card).getBlank());
                cards.add(card);
            }
            return cards;
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
        finally{
            sqlMain.closeAll();
        }
    }

    public Cards getCardById(int cardId){
        try{
            sqlCardset = new SqlCardset();
            sqlMain.setStatement(sqlMain.getConnection().createStatement());
            String query = "SELECT * FROM Card WHERE CardId = ?;";
            
            PreparedStatement ps = sqlMain.getConnection().prepareStatement(query);
            ps.setInt(1, cardId);

            sqlMain.setResult(ps.executeQuery());
            
            Cards card = null;
            while(sqlMain.getResult().next()) {
                int id = sqlMain.getResult().getInt(1);
                String name = sqlMain.getResult().getString(2);
                Boolean blanc = sqlMain.getResult().getBoolean(3);
                Cardset cardset = sqlCardset.getCardsetById(sqlMain.getResult().getInt(4));
                
                card = new PlayCard(id, name, cardset, blanc);
            }
            return card;
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
        finally{
            sqlMain.closeAll();
        }
    }
}
