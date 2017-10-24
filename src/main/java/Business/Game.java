package Business;

/**
 * Created by Jordi on 26-9-2017.
 */

import DAL.SqlCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * In Sprint 2 wordt deze klasse volledig uitgewerkt
 */
public class Game {
    Lobby lobby;
    Random random = new Random();
    List<CzarCard> czarCards;
    List<PlayCard> playCards;
    // Gekozen kaarten door de spelers in de HUIDIGE ronde.
    List<PlayCard> chosenCards;

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
        getDecks();
        newTurn();
    }

    // Methode wordt aangeroepen nadat een speler een kaart speelt.
    public void playerPicksCard(Cards card) {
        // Check of alle spelers hun kaart gespeeld hebben.
        if (chosenCards.size() >= lobby.getPlayers().size()) {
            // Czar moet hier nog kiezen.
        }
        // Wachten tot alle spelers gespeeld hebben. (niets dus)
    }

    public void czarPicksCards(Cards card) {
        // Einde van de beurt, nieuwe ronde start etc.
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


