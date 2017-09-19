package Business;

import Business.Enums.Role;

/**
 * Created by Jordi on 12-9-2017.
 */
public class Player {

    String name;
    int points;
    Role role;

    public String getName(){return name;}
    public void setName(String name){this.name = name;}
    public int getPoints(){return points;}
    public void setPoints(int n){this.points = n;}
    public Role getRole(){return role;}
    public void setRole(Role n){this.role = n;}

    public Player(String name){
        this.name = name;
    }


}
