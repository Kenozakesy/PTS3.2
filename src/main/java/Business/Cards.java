package Business;

/**
 * Created by Gebruiker on 12-9-2017.
 */
public abstract class Cards {

    private String text;

    private Cardset cardset;

    public void SetText(String n) {this.text = n;}
    public String GetText() {return this.text;}

    public Cardset GetCardset() {return this.cardset;}

    public Cards(String Text, Cardset Cardset)
    {
        this.cardset = Cardset;
        this.text = Text;
    }


}
