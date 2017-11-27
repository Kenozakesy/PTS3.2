package business;

/**
 * Created by Gebruiker on 12-9-2017.
 */
public class CzarCard extends Cards {

    private int blankspaces;

    public int getBlankSpaces() {return this.blankspaces;}

    public CzarCard(int id, String text, CardSet cardSet, int blankspaces) {
        super(id, text, cardSet);

        this.blankspaces = blankspaces;
    }
}
