package Business;

/**
 * Created by Gebruiker on 12-9-2017.
 */
public class CzarCard extends Cards {

    private int blankspaces;

    public int getBlankSpaces() {return this.blankspaces;}

    public CzarCard(int id, String text, Cardset cardset, int blankspaces) {
        super(id, text, cardset);

        this.blankspaces = blankspaces;
    }
}
