package Business;

/**
 * Created by Gebruiker on 12-9-2017.
 */
public class PlayCard extends Cards
{
    private boolean blank;

    public void SetBlank(boolean n) {this.blank = n;}

    public boolean GetBlank() {return this.blank;}


    public PlayCard(String Text, Cardset cardset, boolean Blank) {
        super(Text, cardset);

        this.blank = Blank;
    }

}

