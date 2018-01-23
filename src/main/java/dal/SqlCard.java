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
public class SqlCard {
            //"Server=mssql.fhict.local;Database=dbi299244;User Id=dbi299244;Password=PTS3Groep1;"

    SqlCardSet sqlCardSet;

    public List<PlayCard> getAllPlayCardsFromCardSet(CardSet cardSet) {
        SqlConnection sqlConnection = new SqlConnection();
        List<PlayCard> cards = new ArrayList<>();

        try{
            sqlCardSet = new SqlCardSet();
            sqlConnection.setStatement(sqlConnection.getConnection().createStatement());
            String query = "SELECT * FROM Card WHERE DeckId = ? AND CardIsBlack = 0;";
            
            PreparedStatement ps = sqlConnection.getConnection().prepareStatement(query);
            ps.setInt(1, cardSet.getId());

            sqlConnection.setResult(ps.executeQuery());
            
            while(sqlConnection.getResult().next()) {
                int id = sqlConnection.getResult().getInt(1);
                String name = sqlConnection.getResult().getString(2);
                Boolean blanc = sqlConnection.getResult().getBoolean(3);

                PlayCard card = new PlayCard(id, name, cardSet, blanc);
                cards.add(card);
            }
        } catch(Exception ex) {
            //Do nothing
        }
        finally{
            sqlConnection.closeAll();
        }

        return cards;
    }

    public List<CzarCard> getAllCzarCardsFromCardSet(CardSet cardSet) {
        SqlConnection sqlConnection = new SqlConnection();
        List<CzarCard> cards = new ArrayList<>();

        try{
            sqlCardSet = new SqlCardSet();
            sqlConnection.setStatement(sqlConnection.getConnection().createStatement());
            String query = "SELECT * FROM Card WHERE DeckId = ? AND CardIsBlack = 1;";

            PreparedStatement ps = sqlConnection.getConnection().prepareStatement(query);
            ps.setInt(1, cardSet.getId());

            sqlConnection.setResult(ps.executeQuery());

            while(sqlConnection.getResult().next()) {
                int id = sqlConnection.getResult().getInt(1);
                String name = sqlConnection.getResult().getString(2);
                // Boolean blanc = sqlConnection.getResult().getBoolean(3)

                //Blank spaces nog toevoegen aan Database
                CzarCard card = new CzarCard(id, name, cardSet, 1);
                cards.add(card);
            }
        } catch(Exception ex) {
            //Do nothing
        }
        finally{
            sqlConnection.closeAll();
        }

        return cards;
    }

    public Cards getCardById(int cardId){
        SqlConnection sqlConnection = new SqlConnection();
        try{
            sqlCardSet = new SqlCardSet();
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
                CardSet cardSet = sqlCardSet.getCardSetById(sqlConnection.getResult().getInt(4));
                
                card = new PlayCard(id, name, cardSet, blanc);
            }
            return card;
        } catch(Exception ex) {
            //Do nothing
            return null;
        }
        finally{
            sqlConnection.closeAll();
        }
    }
}
