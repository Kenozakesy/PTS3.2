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
            //"Server=mssql.fhict.local;Database=dbi299244;User Id=dbi299244;Password=PTS3Groep1;"

    SqlCardset sqlCardset;

    public List<PlayCard> getAllPlayCardsFromCardSet(Cardset cardset) {
        SqlConnection sqlConnection = new SqlConnection();
        try{
            sqlCardset = new SqlCardset();
            sqlConnection.setStatement(sqlConnection.getConnection().createStatement());
            String query = "SELECT * FROM Card WHERE DeckId = ? AND CardIsBlack = 0;";
            
            PreparedStatement ps = sqlConnection.getConnection().prepareStatement(query);
            ps.setInt(1, cardset.getId());

            sqlConnection.setResult(ps.executeQuery());
            
            List<PlayCard> cards = new ArrayList<>();
            while(sqlConnection.getResult().next()) {
                int id = sqlConnection.getResult().getInt(1);
                String name = sqlConnection.getResult().getString(2);
                Boolean blanc = sqlConnection.getResult().getBoolean(3);

                PlayCard card = new PlayCard(id, name, cardset, blanc);
                cards.add(card);
            }
            return cards;
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
        finally{
            sqlConnection.closeAll();
        }
    }

    public List<CzarCard> getAllCzarCardsFromCardSet(Cardset cardset) {
        SqlConnection sqlConnection = new SqlConnection();
        try{
            sqlCardset = new SqlCardset();
            sqlConnection.setStatement(sqlConnection.getConnection().createStatement());
            String query = "SELECT * FROM Card WHERE DeckId = ? AND CardIsBlack = 1;";

            PreparedStatement ps = sqlConnection.getConnection().prepareStatement(query);
            ps.setInt(1, cardset.getId());

            sqlConnection.setResult(ps.executeQuery());

            List<CzarCard> cards = new ArrayList<>();
            while(sqlConnection.getResult().next()) {
                int id = sqlConnection.getResult().getInt(1);
                String name = sqlConnection.getResult().getString(2);
                Boolean blanc = sqlConnection.getResult().getBoolean(3);

                //Blank spaces nog toevoegen aan Database
                CzarCard card = new CzarCard(id, name, cardset, 1);
                cards.add(card);
            }
            return cards;
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
        finally{
            sqlConnection.closeAll();
        }
    }

    public Cards getCardById(int cardId){
        SqlConnection sqlConnection = new SqlConnection();
        try{
            sqlCardset = new SqlCardset();
            sqlConnection.setStatement(sqlConnection.getConnection().createStatement());
            String query = "SELECT * FROM Card WHERE CardId = ?;";
            
            PreparedStatement ps = sqlConnection.getConnection().prepareStatement(query);
            ps.setInt(1, cardId);

            sqlConnection.setResult(ps.executeQuery());
            
            Cards card = null;
            while(sqlConnection.getResult().next()) {
                int id = sqlConnection.getResult().getInt(1);
                String name = sqlConnection.getResult().getString(2);
                Boolean blanc = sqlConnection.getResult().getBoolean(3);
                Cardset cardset = sqlCardset.getCardsetById(sqlConnection.getResult().getInt(4));
                
                card = new PlayCard(id, name, cardset, blanc);
            }
            return card;
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
        finally{
            sqlConnection.closeAll();
        }
    }
}
