package business;

/**
 * Created by Gebruiker on 12-9-2017.
 */
public class PlayCard extends Cards
{
    private boolean blank;

    public void setText(String n) {this.text = n;}

    public boolean getBlank() {return this.blank;}

    public PlayCard(int id, String text, CardSet cardSet, boolean blank) {
        super(id, text, cardSet);
        this.blank = blank;
    }

}

