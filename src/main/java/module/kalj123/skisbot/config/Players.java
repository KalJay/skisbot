package module.kalj123.skisbot.config;

import com.robrua.orianna.api.core.RiotAPI;
import com.robrua.orianna.type.core.summoner.Summoner;
import module.kalj123.skisbot.league.Player;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kale on 13/06/2017.
 */
public class Players {

    private final String players = "src/main/resources/players.txt";
    private Path playersPath = Paths.get(players);

    private ArrayList<Player> registeredPlayers;
    private Charset utf8 = StandardCharsets.UTF_8;

    public Players() {
        registeredPlayers = new ArrayList<Player>();
        try {
            readFile();
        } catch (IOException e) {
            e.getMessage();
        }
    }

    private void readFile() throws IOException {
        if(!Files.exists(playersPath)) {
            Files.createFile(playersPath);
            System.out.println("File Created!");
        }

        BufferedReader br = new BufferedReader(new FileReader(players));
        String line = null;
        while ((line = br.readLine()) != null) {
            String[] lines = line.split(",");
            registeredPlayers.add(new Player(lines[0], lines[1], Long.parseLong(lines[2])));
        }
    }

    private void saveFile()  {

        List<String> lines = new ArrayList<String>();
        int count = 0;
        for (Player player : registeredPlayers) {
            lines.add(count, player.toString());
            count++;
        }
        try {
            Files.write(playersPath, lines, utf8);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public boolean addPlayer(String name, String disc, String summonerName) {
        if(!doesPlayerExist(name, disc)) {
            long summonerID = RiotAPI.getSummonerByName(summonerName).getID();
            registeredPlayers.add(new Player(name, disc, summonerID));
            saveFile();
            return true;
        } else {
            return false;
        }
    }

    public boolean removePlayerByDiscordID(String name, String disc) {
        for (Player player: registeredPlayers) {
            if (player.getUsername().equals(name) && player.getDiscriminator().equals(disc)) {
                registeredPlayers.remove(player);
                saveFile();
                return true;
            }
        }
        return false;
    }

    private boolean doesPlayerExist(String name, String disc) {
        String entryId = name + disc;
        for (Player player: registeredPlayers) {
            String playerId = player.getUsername() + player.getDiscriminator();
            if (entryId.equals(playerId)) {
                return true;
            }
        }
        return false;
    }

    public Summoner getSummonerFromName(String name) {
        for (Player player : registeredPlayers) {
            if(RiotAPI.getSummonerByName(name).getID() == player.getSummonerID()) {
                 return RiotAPI.getSummonerByName(name);
            }
        }
        return null;
    }

    public String viewPlayers() {
        String lines = "```\nDiscordID, Summoner Name";
        for (Player player: registeredPlayers) {
            lines += "\n" + player.getUsername() + "#" + player.getDiscriminator() + ", " +RiotAPI.getSummonerName(player.getSummonerID());
        }
        lines += "\n```";
        return lines;
    }
}
