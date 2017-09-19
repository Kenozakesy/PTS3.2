package Business;

/**
 * Created by Gebruiker on 12-9-2017.
 */
public class CzarCard extends Cards {

    private int blankspaces;

    public void SetBlankSpaces(int n) {this.blankspaces = n;}

    public int GetBlankSpaces() {return this.blankspaces;}

    public CzarCard(String Text, Cardset cardset, int Blankspaces) {
        super(Text, cardset);

        this.blankspaces = Blankspaces;
    }
}
