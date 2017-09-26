package Business;

/**
 * Created by Gebruiker on 12-9-2017.
 */
public class CzarCard extends Cards {

    private int blankspaces;

    public void setBlankSpaces(int n) {this.blankspaces = n;}

    public int getBlankSpaces() {return this.blankspaces;}

    public CzarCard(String text, Cardset cardset, int blankspaces) {
        super(text, cardset);

        this.blankspaces = blankspaces;
    }
}
