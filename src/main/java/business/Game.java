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
    private List<CzarCard> czarCards = new ArrayList<>();
    private List<PlayCard> playCards = new ArrayList<>();
    private List<PlayCard> subPilePlayCards = new ArrayList<>();
    private List<PlayCard> allCards = new ArrayList<>();
    private CzarCard currentCzar; // Is dus de zwarte kaart die op tafel ligt, waarop gespeeld wordt.
    private boolean czarTurn;
    private static final int MAX_HAND_SIZE = 8;
    // Gekozen kaarten door de spelers in de HUIDIGE ronde.

    private Map<Player, PlayCard> chosenCards = new HashMap<>();


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
        getDecks();
    }

    // Methode wordt aangeroepen nadat een speler een kaart speelt.
    // Returnt true als de Host de laatste kaart heeft gespeeld
    public boolean playerPicksCard(int cardPosition, Player player) {
        PlayCard card = player.getCardsInHand().get(cardPosition);
        boolean listIsFull;

        if (lobby.isHost()) {
            listIsFull = addToChosenCards(player, card);
            player.removeFromHand(card);

            return listIsFull;
        } else {
            try {
                lobby.messageServer(MessageType.PLAY_CARD, String.valueOf(card.getId()));
                player.removeFromHand(card);
            } catch (NotClientException e) {
                // do nothing
            }
        }

        // Check of alle spelers hun kaart gespeeld hebben.
        if (chosenCards.size() >= lobby.getPlayers().size() - 1) {
            //bericht sturen server bijhouden wie heeft gekozen (peter fix this)
            czarTurn = true;
        }
        return false;
    }

    public void czarPicksCards(String cardText) {
        //Speler met punt erbij moet 1 punt krijgen doorgestuurd
        if (lobby.isHost()) {
            String playerName = null;
            for (Map.Entry<Player, PlayCard> entry : chosenCards.entrySet()) {
                if (entry.getValue().getText().equals(cardText)) {
                    entry.getKey().increasePoints();
                    playerName = entry.getKey().getName();
                    break;
                }
            }

            try {
                lobby.messageClients(MessageType.INCREASE_POINTS, playerName + "," + cardText);
            } catch (NotHostException e) {
                e.printStackTrace();
            }
        } else {
            try {
                lobby.messageServer(MessageType.INCREASE_POINTS, cardText);
            } catch (NotClientException e) {
                e.printStackTrace();
            }
        }

        //verwijder chosencards (voor iedereen)
        chosenCards.clear();
    }

    // Een zwarte kaart wordt gekozen om te lezen en wordt meteen uit de te kiezen kaarten gehaald.
    public void pickBlackCard() {
        CzarCard card = czarCards.get(random.nextInt(czarCards.size()));
        this.currentCzar = card;
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

                        if (playCards.isEmpty()) {
                            playCards.addAll(subPilePlayCards);
                            subPilePlayCards = new ArrayList<>(playCards.size());
                        }

                        int index = random.nextInt(playCards.size());
                        player.addToHand(playCards.get(index));

                        subPilePlayCards.add(playCards.get(index));
                        playCards.remove(index);
                    }
                }
            }

            //voor het doorsturen kaarten naar bijbehorende client
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
            ex.printStackTrace();
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
        boolean playedCard = false;

        if (player.getCardsInHand().size() != MAX_HAND_SIZE) {
            playedCard = true;
        }

        return playedCard;
    }

    // Host ontvangt een kaart van zichzelf of een clients.
    // Deze methode wordt aangeroepen in de GameController bij OnClientMessage
    public boolean addToChosenCards(Player player, PlayCard playCard) {
        chosenCards.put(player, playCard);
        StringBuilder builder = new StringBuilder();

        if (chosenCards.size() >= lobby.getPlayers().size() - 1 && lobby.isHost()) {
            for (PlayCard card : chosenCards.values()) {
                builder.append(String.valueOf(card.getId()) + ",");
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




