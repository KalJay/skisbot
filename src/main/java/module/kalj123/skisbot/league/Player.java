package module.kalj123.skisbot.league;

/**
 * Created by Kale on 2/06/2017.
 */
public class Player {

    private final String username;
    private final String discriminator;
    private final long summonerID;

    public Player(String username, String discriminator, long summonerName) {
        this.username = username;
        this.discriminator = discriminator;
        this.summonerID = summonerName;
    }

    public String getUsername() {
        return username;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public long getSummonerID() {
        return summonerID;
    }

    @Override
    public String toString() {
        return username+ "," + discriminator + "," + summonerID;
    }
}
