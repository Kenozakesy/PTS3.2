package Business;

/**
 * Created by Jordi on 12-9-2017.
 */
public class Player {

    String name;
    int points;

    public Player(String name){
        this.name = name;
    }

    public String getName(){return name;}
    public void setName(String name){this.name = name;}
    public int getPoints(){return points;}
}
