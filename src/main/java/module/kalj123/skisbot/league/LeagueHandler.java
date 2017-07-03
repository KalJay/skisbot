package module.kalj123.skisbot.league;

import com.github.xaanit.d4j.oauth.handle.IConnection;
import com.robrua.orianna.api.core.RiotAPI;
import com.robrua.orianna.type.core.common.Region;
import com.robrua.orianna.type.core.common.Side;
import com.robrua.orianna.type.core.currentgame.CurrentGame;
import com.robrua.orianna.type.core.currentgame.Participant;
import com.robrua.orianna.type.core.summoner.Summoner;
import module.kalj123.skisbot.SkisBot;
import module.kalj123.skisbot.config.Config;
import module.kalj123.skisbot.config.Players;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IPrivateChannel;
import sx.blah.discord.handle.obj.IUser;

import java.io.*;
import java.util.List;

/**
 * Project: skisbot
 * Created by Kale on 2/06/2017 at 2:59 AM.
 */
public class LeagueHandler {



    private Config config;
    private Players players;
    private LOLAdmin lolAdmin;
    private Boolean keyset = false;



    public LeagueHandler() throws IOException {
        players = new Players();


    }

    public void handler(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContent().split(" ");
        IGuild activeGuild = event.getGuild();
        switch(args[1]) {
            case "link":
                if (!keyset) {
                    config.getGuildBotChannel(activeGuild).sendMessage("Riot API Key is not set in config file!");
                } else {
                    if (players.addPlayer(event.getAuthor().getName(), event.getAuthor().getDiscriminator(), args[2])) {
                        config.getGuildBotChannel(activeGuild).sendMessage("Successfully linked " + event.getAuthor().getName() + " to summoner name " + args[2]);
                    } else
                        config.getGuildBotChannel(activeGuild).sendMessage("You have already registered a Summoner name");
                }
                break;
            case "view":
                if (!keyset) {
                    config.getGuildBotChannel(activeGuild).sendMessage("Riot API Key is not set in config file!");
                } else {
                    config.getGuildBotChannel(activeGuild).sendMessage(viewGame(args[2]));
                    break;
                }
            case "help":
                help(event.getMessage().getAuthor());
                break;
            case "admin":
                config.getGuildBotChannel(activeGuild).sendMessage(lolAdmin.handle(args, event.getAuthor(), event.getGuild()));
                break;
            case "refresh":
                config.getGuildBotChannel(activeGuild).sendMessage(refresh());
                break;
            default:
                config.getGuildBotChannel(activeGuild).sendMessage(unrecognised());
                break;
        }

    }

    public void startConfig(List<IGuild> guilds) {
        config = new Config(guilds);
        lolAdmin = new LOLAdmin(players, config);
        RiotAPI.setRegion(Region.OCE);
        String key = config.getRiotAPIKey();
        if (!key.equals("")) {
            RiotAPI.setAPIKey(config.getRiotAPIKey());
            keyset = true;
        } else {
            System.out.println("Riot API key not set! League interaction limited...");
            keyset = false;
        }

    }

    private String viewGame(String name) {
        Summoner focusPlayer;
        CurrentGame currentGame;
        String returnString;
        List<Participant> participants;

        if ((focusPlayer = players.getSummonerFromName(name))== null) {
            return "Invalid summoner name";
        }

        if ((currentGame = focusPlayer.getCurrentGame()) == null) {
            return name + " is not currently in a game";
        }
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

        return returnString.concat("\n```");
    }

    private void help(IUser user) {
        IPrivateChannel privateDM = SkisBot.discordClient.getOrCreatePMChannel(user);
        String helpString = "```";
        helpString += "\n!lol link <summonerName> - links your discord account with the provided summoner name";
        helpString += "\n!lol view <summonerName> - provides a short description of the player's current game";
        helpString += "\n```";
        privateDM.sendMessage(helpString);
    }

    private String unrecognised() {
        return "Unrecognised LoL command";
    }

    private String refresh() {
        players = new Players();
        lolAdmin = new LOLAdmin(players, config);
        return "Refreshed";
    }

}

//API key: RGAPI-ce2745b8-b516-4ce0-a49f-034dfab0155e
