package module.kalj123.skisbot.league;

/**
 * Project: skisbot
 * Created by Kale on 2/06/2017 at 3:04 AM.
 */
public class Player {

    private String ID;
    private String username;

    public Player(String ID, String username) {
        this.ID = ID;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getID() {
        return ID;
    }


    @Override
    public String toString() {
        return getID() + ", " + getUsername();
    }
}
