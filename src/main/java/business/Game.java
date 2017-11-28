package business;

/**
 * Created by Jordi on 26-9-2017.
 */

import business.exceptions.NotClientException;
import business.exceptions.NotHostException;
import dal.SqlCard;
import networking.MessageType;

import java.util.*;

/**
 * In Sprint 2 wordt deze klasse volledig uitgewerkt
 */
public class Game {
    private Lobby lobby;
    private Random random = new Random();
    private List<CzarCard> czarCards;
    private List<PlayCard> playCards;
    private List<PlayCard> allCards;
    private CzarCard currentCzar;
    private boolean czarTurn;
    // Gekozen kaarten door de spelers in de HUIDIGE ronde.

    private Map<Player, PlayCard> chosenCards;


    public CzarCard getCurrentCzarCard() {
        return currentCzar;
    }

    public boolean getIsCzarTurn() {
        return czarTurn;
    }

    public List<CzarCard> getCzarCards() {
        return czarCards;
    }

    public List<PlayCard> getAllCards() {
        return allCards;
    }

    public List<PlayCard> getPlayCards() {
        return playCards;
    }

    public Map<Player, PlayCard> getChosenCards() {
        return chosenCards;
    }

    public void setCurrentCzar(CzarCard currentCzar) {
        this.currentCzar = currentCzar;
    }

    public Game(Lobby lobby) {
        this.lobby = lobby;
        czarCards = new ArrayList<>();
        playCards = new ArrayList<>();
        allCards = new ArrayList<>();
        chosenCards = new HashMap<>();
        getDecks();
    }

    // Methode wordt aangeroepen nadat een speler een kaart speelt.
    public void playerPicksCard(int cardPosition, Player player) {
        //gets player
        PlayCard card = player.getCardsInHand().get(cardPosition);

        if (lobby.isHost()) {
            addToChosenCards(player, card);
            player.getCardsInHand().remove(card);
        } else {
            try {
                lobby.messageServer(MessageType.PLAY_CARD, String.valueOf(card.getId()));
            } catch (NotClientException e) {
                // do nothing
            }
        }

        // Check of alle spelers hun kaart gespeeld hebben.
        if (chosenCards.size() >= lobby.getPlayers().size() - 1) {
            //bericht sturen server bijhouden wie heeft gekozen (peter fix this)
            czarTurn = true;
        }
    }

    public void czarPicksCards(String cardText) {
        // Einde van de beurt, nieuwe ronde start etc.
        for (Map.Entry<Player, PlayCard> entry : chosenCards.entrySet()) {
            if (entry.getValue().getText().equals(cardText)) {
                entry.getKey().increasePoints();
            }
        }
        //verwijder kaarten uit geheugen
        chosenCards.clear();
        newTurn();
    }

    // Een zwarte kaart wordt gekozen om te lezen en wordt meteen uit de te kiezen kaarten gehaald.
    public void pickBlackCard() {
        CzarCard card = czarCards.get(random.nextInt(czarCards.size()));
        this.currentCzar = card;
        czarCards.remove(card);
    }

    // Opnieuw kaarten delen.
    public void newTurn() {
        czarTurn = false;

        pickBlackCard();
        cardSharing();
        lobby.setRoles();
    }


    //deelt nieuwe kaarten uit
    private void cardSharing() {
        if (!lobby.isHost()) {
            return;
        }
        try {
            for (int i = 0; i < 8; i++) {
                //voegd een kaart toe aan speler hand en verwijderd die uit de stapel
                for (Player player : lobby.getPlayers().values()) {
                    if (player.getCardsInHand().size() < 8) {

                        int index = random.nextInt(playCards.size());

                        player.addToHand(playCards.get(index));

                        playCards.remove(index);
                    }
                }
            }

            for (Player player : lobby.getPlayers().values()) {
                StringBuilder builder = new StringBuilder();

                builder.append(this.currentCzar.getId());
                builder.append(",");

                for (PlayCard C : player.getCardsInHand()) {
                    builder.append(C.getId());
                    builder.append(",");
                }

                lobby.messageClient(player, MessageType.RECEIVE_CARD, builder.toString());
            }
        } catch (Exception ex) {
            //Do nothing
        }
    }

    public void getDecks() {
        SqlCard sqlCard = new SqlCard();
        for (CardSet c : lobby.getCardSetsUsing()) {
            czarCards.addAll(sqlCard.getAllCzarCardsFromCardSet(c));
            playCards.addAll(sqlCard.getAllPlayCardsFromCardSet(c));
            allCards.addAll(sqlCard.getAllPlayCardsFromCardSet(c));
        }
    }

    public boolean playedCard(Player player) {
        for (Map.Entry<Player, PlayCard> entry : chosenCards.entrySet()) {
            if (entry.getKey().getName().equals(player.getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean addToChosenCards(Player player, PlayCard playCard) {
        chosenCards.put(player, playCard);
        StringBuilder builder = new StringBuilder();

        if (chosenCards.size() >= lobby.getPlayers().size() - 1 && lobby.isHost()) {
            for (PlayCard card : chosenCards.values()) {
                builder.append(String.valueOf(card.getId()));
            }
            try {
                lobby.messageClients(MessageType.CHOSEN_CARDS, builder.toString());
                return true;
            } catch (NotHostException e) {
                e.printStackTrace();
            }

        }
        return false;
    }
}



