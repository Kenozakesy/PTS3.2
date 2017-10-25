package Business;

/**
 * Created by Jordi on 26-9-2017.
 */

import Business.Enums.Role;
import Business.staticClasses.StaticPlayer;
import DAL.SqlCard;

import java.net.Socket;
import java.util.*;

/**
 * In Sprint 2 wordt deze klasse volledig uitgewerkt
 */
public class Game {
    private Lobby lobby;
    private Random random = new Random();
    private List<CzarCard> czarCards;
    private List<PlayCard> playCards;
    private CzarCard currentCzar;
    private boolean czarTurn;
    // Gekozen kaarten door de spelers in de HUIDIGE ronde.

    Map<Player, PlayCard> chosenCards;

    public CzarCard getCurrentCzar() {
        return currentCzar;
    }
    public boolean getIsCzarTurn() {
        return czarTurn;
    }
    public List<CzarCard> getCzarCards() {
        return czarCards;
    }
    public List<PlayCard> getPlayCards() {
        return playCards;
    }
    public Map<Player, PlayCard> getChosenCards(){ return chosenCards; }

    public Game(Lobby lobby) {
        this.lobby = lobby;
        czarCards = new ArrayList<>();
        playCards = new ArrayList<>();
        chosenCards = new HashMap<>();
        getDecks();
        newTurn();
    }

    // Methode wordt aangeroepen nadat een speler een kaart speelt.
    public void playerPicksCard(int cardPosition, Player player) {

        //gets player
        Player pleb = null;
        for (Player p: lobby.getPlayers().values())
        {
            if(p.getName().equals(player.getName()))
            {
                 pleb = p;
            }
        }
        PlayCard card = pleb.getCardsInHand().get(cardPosition);
        //kiest kaart om in te zenden
        chosenCards.put(player, card);
        //verwijderd de kaart uit de hand van de speler
        pleb.getCardsInHand().remove(card);

        // Check of alle spelers hun kaart gespeeld hebben.
        if (chosenCards.size() >= lobby.getPlayers().size()) {

            //bericht sturen server bijhouden wie heeft gekozen (peter fix this)
            czarTurn = true;
        }
    }

    public void czarPicksCards(String cardText) {
        // Einde van de beurt, nieuwe ronde start etc.
        for (Map.Entry<Player, PlayCard> entry : chosenCards.entrySet())
        {
            if(entry.getValue().getText().equals(cardText))
            {
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
        for (Player player : lobby.getPlayers().values()) {
            while (player.getCardsInHand().size() < 8) {
                int index = random.nextInt(playCards.size());
                player.addToHand(playCards.get(index));
                playCards.remove(index);
            }
        }
        //Volgende speler wordt willekeurig gekozen. Moet nog aangepast worden.
        for (Map.Entry<Socket, Player> entry : lobby.getPlayers().entrySet())
        {
            Random ran = new Random();
            int pos = ran.nextInt(lobby.getPlayers().size() + 1);
            int tel = 1;
            for (Map.Entry<Socket, Player> p : lobby.getPlayers().entrySet()) {
                if (tel == pos){
                    p.getValue().setRole(Role.Czar);
                }
                else{
                    p.getValue().setRole(Role.Pleb);
                }
                tel++;
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

    public boolean playedCard(Player player){
        for (Map.Entry<Player, PlayCard> entry : chosenCards.entrySet())
        {
            if(entry.getKey().getName().equals(player.getName()))
            {
                return true;
            }
        }
        return false;
    }
}


