package Business;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Gebruiker on 12-9-2017.
 */
public class Cardset {

    private ArrayList<Cards> cardsInSet;

    private String name;

    public void setName(String n) {this.name = n;}
    public String getName() {return this.name;}

    public Cardset(String name)
    {
        this.name = name;
    }

    public void getCardsInSet()
    {
        //if card set is chosen add all cards from the set to the list
        //to be added later to the two different decks.
    }

}
