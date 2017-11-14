package Business;

/**
 * Created by Gebruiker on 12-9-2017.
 */
public abstract class Cards {

    private int id;
    protected String text;

    private Cardset cardSet;

    public String getText() {return this.text;}
    public int getId() {return this.id;}
    
    public Cardset getCardset() {return this.cardSet;}

    public Cards(int id, String text, Cardset cardSet)
    {
        this.id = id;
        this.cardSet = cardSet;
        this.text = text;
    }
}
