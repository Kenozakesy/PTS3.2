package Business;

/**
 * Created by Jordi on 26-9-2017.
 */

import DAL.SqlCard;
import DAL.SqlCardset;

import java.util.ArrayList;
import java.util.Random;

/**
 * In Sprint 2 wordt deze klasse volledig uitgewerkt
 */
public class Game
{
    Lobby lobby;
    Random random = new Random();
    ArrayList<Cards> czarCards;
    ArrayList<Cards> playCards;
    ArrayList<PlayCard> chosenCards;
    int playerIsCzar;


    public ArrayList<Cards> getCzarCards() {
        return czarCards;
    }

    public ArrayList<Cards> getPlayCards() {
        return playCards;
    }

    public Game(Lobby lobby) {
        this.lobby = lobby;
        czarCards = new ArrayList<>();
        playCards = new ArrayList<>();
        playerIsCzar = 0;
        endTurn();


    }

    public void playerPicksCard(Cards card)
    {

        if (chosenCards.size() >= lobby.getPlayers().size())
        {
            
        }
    }

    public void czarPicksCards(Cards card)
    {

    }

    public CzarCard pickBlackCard(){
        random = new Random();

        return (CzarCard)czarCards.get(random.nextInt(czarCards.size()));
    }

    public void endTurn()
   {
        for(Player player:lobby.getPlayers().values())
        {
            while(player.getCardsInHand().size() < 8)
            {
                int index = random.nextInt(playCards.size());
                player.addToHand((PlayCard)playCards.get(index));
               playCards.remove(index);
            }
        }
    }

    public void getDecks(ArrayList<Cardset> cardSets)
    {

        SqlCardset sqlCardset = new SqlCardset();
        cardSets = sqlCardset.getAllCardsets();
        SqlCard sqlCard = new SqlCard();
        for (Cardset c: cardSets )
        {
            czarCards.addAll(sqlCard.getAllCzarCardsFromCardSet(c));
            playCards.addAll(sqlCard.getAllPlayCardsFromCardSet(c));
        }
    }
}


