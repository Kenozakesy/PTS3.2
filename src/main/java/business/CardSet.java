package business;

/**
 * Created by Gebruiker on 12-9-2017.
 */
public class CardSet {

    private int id;
    private String name;

    public void setId(int id) {this.id = id;}
    public int getId() {return this.id;}
    public void setName(String n) {this.name = n;}
    public String getName() {return this.name;}

    public CardSet(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
