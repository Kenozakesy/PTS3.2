package business;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jordi on 12-9-2017.
 */
public class Player {

    private String name;
    private int points;
    private Role role;
    private List<PlayCard> cardsInHand = new ArrayList<>();
    private List<CardSet> cardSetList = null;

    public String getName(){return name;}
    public void setName(String name){this.name = name;}
    public int getPoints(){return points;}
    public void setPoints(int n){this.points = n;}
    public Role getRole(){return role;}

    public List<CardSet> getCardSetList() {
        return cardSetList;
    }

    public void setCardSetList(List<CardSet> cardSetList) {
        this.cardSetList = cardSetList;
    }

    public void setRole(Role n){this.role = n;}
    public List<PlayCard> getCardsInHand() {return cardsInHand;}

    public Player()
    {

    }

    public Player(String name){
        this.name = name;
    }

    public void addToHand(PlayCard card)
    {
        cardsInHand.add(card);
    }

    public void increasePoints()
    {
        this.points++;
    }

    public void resetPoints()
    {
        this.points = 0;
    }

    public void removeFromHand(PlayCard card) {
        PlayCard cardpar = null;
        for(PlayCard cardInHand : cardsInHand) {
            if(cardInHand.getId() == card.getId()) {
                cardpar = cardInHand;
                break;
            }
        }
        cardsInHand.remove(cardpar);
    }
}
