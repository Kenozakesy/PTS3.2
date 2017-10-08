package Business;

/**
 * Created by Jordi on 26-9-2017.
 */

import DAL.SqlCard;

import java.util.ArrayList;
import java.util.Random;

import static Business.Enums.Role.Czar;

/**
 * In Sprint 2 wordt deze klasse volledig uitgewerkt
 */
public class Game
{
    Random random = new Random();
    ArrayList<Cards> czarCards;
    ArrayList<Cards> playCards;
    SqlCard sqlCard = new SqlCard();
    Game(){}

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
        //

    }

    public void getDecks(ArrayList<Cardset> cardSets)
    {
        for (Cardset c: cardSets )
        {
            czarCards.addAll(sqlCard.getAllCzarCardsFromCardSet(c));
            playCards.addAll(sqlCard.getAllPlayCardsFromCardSet(c));
        }
    }
}


