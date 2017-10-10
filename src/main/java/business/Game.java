package business;

/**
 * Created by Jordi on 26-9-2017.
 */

import DAL.SqlCard;

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

    public Game(Lobby lobby) {this.lobby = lobby;}

    public void playerPicksCard(Cards card)
    {

        if (chosenCards.size() >= lobby.getPlayers().size())
        {
            
        }
    }

    public void czarPicksCards(Cards card)
    {

    }

    public void endTurn()
    {
        // for(Player player : playerList)
        // {
        //    while(player.cardsInHand < 8)
        //  {
        //    int index = random.nextInt(playcardsList.size());
        //    player.addCard(playcardsList.get(index));
        // }
        //
        // }
    }

    public void getDecks(ArrayList<Cardset> cardSets)
    {
        SqlCard sqlCard = new SqlCard();
        for (Cardset c: cardSets )
        {
            czarCards.addAll(sqlCard.getAllCzarCardsFromCardSet(c));
            playCards.addAll(sqlCard.getAllPlayCardsFromCardSet(c));
        }
    }
}


