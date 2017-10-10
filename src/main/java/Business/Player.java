package Business;

import Business.Enums.Role;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jordi on 12-9-2017.
 */
public class Player {

    private String name;
    private int points;
    private Role role;
    private List<PlayCard> cardsInHand;

    public String getName(){return name;}
    public void setName(String name){this.name = name;}
    public int getPoints(){return points;}
    public void setPoints(int n){this.points = n;}
    public Role getRole(){return role;}
    public void setRole(Role n){this.role = n;}
    public List<PlayCard> getCardsInHand() {return cardsInHand;}

    public Player(String name)
    {
        this.name = name;
        cardsInHand = new ArrayList<PlayCard>();
    }

    public void addToHand(PlayCard card)
    {
        cardsInHand.add(card);
    }
    public void removeFromHand(PlayCard card) {
        for(PlayCard cardInHand : cardsInHand) {
            if(cardInHand.getId() == card.getId()) {
                cardsInHand.remove(card);
            }
        }
    }
}
