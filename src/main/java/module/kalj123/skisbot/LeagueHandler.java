package module.kalj123.skisbot;

import com.robrua.orianna.api.core.RiotAPI;
import com.robrua.orianna.type.core.common.Region;
import com.robrua.orianna.type.core.common.Side;
import com.robrua.orianna.type.core.currentgame.CurrentGame;
import com.robrua.orianna.type.core.currentgame.Participant;
import com.robrua.orianna.type.core.summoner.Summoner;
import module.kalj123.skisbot.league.Player;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kale on 2/06/2017.
 */
public class LeagueHandler {


    private final String filePath = "src/main/resources/players.txt";
    private File theFile = new File(filePath);
    Path file = Paths.get(filePath);
    private ArrayList<Player> registeredPlayers;
    Charset utf8 = StandardCharsets.UTF_8;

    public LeagueHandler() throws IOException {
        registeredPlayers = new ArrayList<Player>();
        readFile();
        RiotAPI.setRegion(Region.OCE);
        RiotAPI.setAPIKey("RGAPI-ce2745b8-b516-4ce0-a49f-034dfab0155e");
    }

    public void handler(MessageReceivedEvent event) throws IOException {
        String[] args = event.getMessage().getContent().split(" ");
        switch(args[1]) {
            case "link":
                if (addPlayer(event.getAuthor().getName(), event.getAuthor().getDiscriminator(), args[2])) {
                    event.getChannel().sendMessage("Successfully linked " + event.getAuthor().getName() + " to summoner name " + args[2]);
                } else
                    event.getChannel().sendMessage("You have already registered a Summoner name");
                break;
            case "view":
                event.getChannel().sendMessage(viewGame(args[2]));
                break;
            default:
                help(event.getChannel());
                break;
        }

    }

    private String viewGame(String name) {
        Summoner focusPlayer = null;
        CurrentGame currentGame;
        String returnString;
        List<Participant> participants = new ArrayList<Participant>();
        for (Player player : registeredPlayers) {
            if(RiotAPI.getSummonerByName(name).getID() == player.getSummonerID()) {
                focusPlayer = RiotAPI.getSummonerByName(name);
            }
        }
        if (focusPlayer == null) {
            return "Invalid summoner name";
        }
        currentGame = focusPlayer.getCurrentGame();
        returnString = "```\nMap: " + currentGame.getMap() + "\nQueue Type: " + currentGame.getQueueType() + "\nGame Time: " + currentGame.getLength();
        participants = currentGame.getParticipants();
        returnString = returnString.concat("\n\nBLUE TEAM:");
        for (Participant participant : participants) {
            if (participant.getTeam().equals(Side.BLUE)) {
                returnString = returnString.concat("\n" + participant.getChampion() + " (" + participant.getSummonerName() + ")");
            }
        }
        returnString = returnString.concat("\n\nPURPLE TEAM:");
        for (Participant participant : participants) {
            if (participant.getTeam().equals(Side.PURPLE)) {
                returnString = returnString.concat("\n" + participant.getChampion() + " (" + participant.getSummonerName() + ")");
            }
        }

        return returnString = returnString.concat("\n```");
    }

    private void help(IChannel channel) {
        channel.sendMessage("Unrecognised LoL command");
    }

    private boolean addPlayer(String name, String disc, String summonerName) throws IOException {
        if(!doesPlayerExist(name, disc)) {
            long summonerID = RiotAPI.getSummonerByName(summonerName).getID();
            registeredPlayers.add(new Player(name, disc, summonerID));
            saveFile();
            return true;
        } else {
            return false;
        }
    }

    private void readFile() throws IOException {
        if(!Files.exists(file)) {
            Files.createFile(file);
            System.out.println("File Created!");
        }

        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line = null;
        while ((line = br.readLine()) != null) {
            String[] lines = line.split(",");
            registeredPlayers.add(new Player(lines[0], lines[1], Long.parseLong(lines[2])));
        }
    }

    private void saveFile() throws IOException {

        List<String> lines = new ArrayList<String>();
        int count = 0;
        for (Player player : registeredPlayers) {
            lines.add(count, player.toString());
            count++;
        }
        Files.write(file, lines, utf8);
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
}

//API key: RGAPI-ce2745b8-b516-4ce0-a49f-034dfab0155e
