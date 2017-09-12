package Business;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Gebruiker on 12-9-2017.
 */
public class Cardset {

    private ArrayList<Cards> CardsInSet;

    private String name;

    public void SetName(String n) {this.name = n;}
    public String GetName() {return this.name;}

    public Cardset(String Name)
    {
        this.name = Name;
    }

    public void GetCardsInSet()
    {
        //if card set is chosen add all cards from the set to the list
        //to be added later to the two different decks.
    }

}
