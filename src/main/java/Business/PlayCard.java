package Business;

/**
 * Created by Gebruiker on 12-9-2017.
 */
public class PlayCard extends Cards
{
    private boolean blank;

    public void setBlank(boolean n) {this.blank = n;}

    public boolean getBlank() {return this.blank;}


    public PlayCard(String text, Cardset cardset, boolean blank) {
        super(text, cardset);

        this.blank = blank;
    }

}

