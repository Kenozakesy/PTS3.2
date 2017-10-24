package Business;

/**
 * Created by Jordi on 26-9-2017.
 */

import DAL.SqlCard;

import java.util.*;

/**
 * In Sprint 2 wordt deze klasse volledig uitgewerkt
 */
public class Game {
    private Lobby lobby;
    private Random random = new Random();
    private List<CzarCard> czarCards;
    private List<PlayCard> playCards;
    // Gekozen kaarten door de spelers in de HUIDIGE ronde.

    Map<Player, PlayCard> chosenCards;
    
    public List<CzarCard> getCzarCards() {
        return czarCards;
    }

    public List<PlayCard> getPlayCards() {
        return playCards;
    }

    public Game(Lobby lobby) {
        this.lobby = lobby;
        czarCards = new ArrayList<>();
        playCards = new ArrayList<>();
        chosenCards = new HashMap<>();
        getDecks();
        newTurn();
    }

    // Methode wordt aangeroepen nadat een speler een kaart speelt.
    public boolean playerPicksCard(PlayCard card, Player player) {

        chosenCards.put(player, card);

        // Check of alle spelers hun kaart gespeeld hebben.
        if (chosenCards.size() >= lobby.getPlayers().size()) {
            // Wanneer alle kaarten gekozen zijn moeten te zien zijn voor iedereen
            return true;
        }
        // Wachten tot alle spelers gespeeld hebben.
        return false;
    }

    public void czarPicksCards(PlayCard card) {
        // Einde van de beurt, nieuwe ronde start etc.
        for (Map.Entry<Player, PlayCard> entry : chosenCards.entrySet())
        {
            if(entry.getValue().equals(card))
            {
                entry.getKey().increasePoints();
            }
        }
        newTurn();
    }

    // Een zwarte kaart wordt gekozen om te lezen en wordt meteen uit de te kiezen kaarten gehaald.
    public CzarCard pickBlackCard() {
        CzarCard card = czarCards.get(random.nextInt(czarCards.size()));
        czarCards.remove(card);
        return card;
    }

    // Opnieuw kaarten delen.
    public void newTurn() {
        for (Player player : lobby.getPlayers().values()) {
            while (player.getCardsInHand().size() < 8) {
                int index = random.nextInt(playCards.size());
                player.addToHand(playCards.get(index));
                playCards.remove(index);
            }
        }
    }

    public void getDecks() {
        SqlCard sqlCard = new SqlCard();
        for (Cardset c : lobby.getCardSetsUsing()) {
            czarCards.addAll(sqlCard.getAllCzarCardsFromCardSet(c));
            playCards.addAll(sqlCard.getAllPlayCardsFromCardSet(c));
        }
    }
}


