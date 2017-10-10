package business;

/**
 * Created by Gebruiker on 12-9-2017.
 */
public abstract class Cards {

    private int id;
    private String text;

    private Cardset cardSet;

    public void setText(String n) {this.text = n;}
    public String getText() {return this.text;}
    public int getId() {return this.id;}
    
    public Cardset getCardset() {return this.cardSet;}

    public Cards(String Text, Cardset Cardset)
    {
        this.cardSet = Cardset;
        this.text = Text;
    }
    public Cards(int id, String text, Cardset cardset) {
        this(text, cardset);
        this.id = id;
    }

}
