package business;

/**
 * Created by Gebruiker on 12-9-2017.
 */
public abstract class Cards {

    private int id;
    protected String text;

    private CardSet cardSet;

    public String getText() {return this.text;}
    public int getId() {return this.id;}
    
    public CardSet getCardset() {return this.cardSet;}

    public Cards(int id, String text, CardSet cardSet)
    {
        this.id = id;
        this.cardSet = cardSet;
        this.text = text;
    }
}
