package Business;

/**
 * Created by Gebruiker on 12-9-2017.
 */
public abstract class Cards {

    private String text;

    private Cardset cardSet;

    public void setText(String n) {this.text = n;}
    public String getText() {return this.text;}

    public Cardset getCardset() {return this.cardSet;}

    public Cards(String Text, Cardset Cardset)
    {
        this.cardSet = Cardset;
        this.text = Text;
    }


}
