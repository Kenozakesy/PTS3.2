package Business;

import java.util.ArrayList;

/**
 * Created by Gebruiker on 12-9-2017.
 */
public class Cardset {

    private ArrayList<Cards> cardsInSet = new ArrayList<Cards>();

    private int id;
    private String name;

    public void setId(int id) {this.id = id;}
    public int getId() {return this.id;}
    public void setName(String n) {this.name = n;}
    public String getName() {return this.name;}

    public Cardset(String name)
    {
        this.name = name;
    }
    public Cardset(int id, String name){
        this(name);
        this.id = id;
    }

    public void getCardsInSet()
    {
        //if card set is chosen add all cards from the set to the list
        //to be added later to the two different decks.
    }

    public void setCardsInCardset(ArrayList<Cards> cards){
        this.cardsInSet = cards;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
