package module.kalj123.skisbot.league;

import javafx.geometry.Side;
import module.kalj123.skisbot.SkisBot;
import module.kalj123.skisbot.config.Config;
import module.kalj123.skisbot.config.Players;
import module.kalj123.skisbot.oauth.OAuth;
import net.rithms.riot.api.ApiConfig;
import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.spectator.dto.CurrentGameInfo;
import net.rithms.riot.api.endpoints.spectator.dto.CurrentGameParticipant;
import net.rithms.riot.api.endpoints.spectator.dto.Participant;
import net.rithms.riot.api.endpoints.summoner.dto.Summoner;
import net.rithms.riot.constant.Platform;
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

    public static RiotApi api;



    public LeagueHandler() throws IOException {
        players = new Players();


    }

    public void handler(MessageReceivedEvent event) throws RiotApiException {
        String[] args = event.getMessage().getContent().split(" ");
        IGuild activeGuild = event.getGuild();
        switch(args[1]) {
            case "link":
                if (!keyset) {
                    config.getGuildBotChannel(activeGuild).sendMessage("Riot API Key is not set in config file!");
                } else {
                    config.getGuildBotChannel(activeGuild).sendMessage(Players.addPlayerWithCheck(event.getAuthor(), activeGuild, api));
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
            case "auth":
                sendAuthLinkToUser(event.getAuthor());
                break;
            default:
                config.getGuildBotChannel(activeGuild).sendMessage(unrecognised());
                break;
        }

    }

    private void sendAuthLinkToUser(IUser user) {
        IPrivateChannel privateDM = SkisBot.discordClient.getOrCreatePMChannel(user);
        privateDM.sendMessage("<"+ OAuth.getAuthLink() + ">");
    }

    public void startConfig(List<IGuild> guilds) {
        config = new Config(guilds);
        lolAdmin = new LOLAdmin(players, config);
        String key = config.getRiotAPIKey();
        if (!key.equals("")) {
            ApiConfig config = new ApiConfig().setKey(key);
            api = new RiotApi(config);
            keyset = true;
        } else {
            System.out.println("Riot API key not set! League interaction limited...");
            keyset = false;
        }
    }

    private String viewGame(String name) throws RiotApiException {
        Summoner focusPlayer;
        CurrentGameInfo currentGame;
        String returnString;
        List<CurrentGameParticipant> participants;

        if ((focusPlayer = Players.getSummonerFromName(name, api))== null) {
            return "Invalid summoner name";
        }

        if ((currentGame = api.getActiveGameBySummoner(Platform.OCE, focusPlayer.getId())) == null) {
            return name + " is not currently in a game";
        }
        returnString = "```\nMap: " +   getMapName(currentGame.getMapId()) + "\nGame Mode: " + currentGame.getGameMode() + "\nGame Time: " + currentGame.getGameLength();
        participants = currentGame.getParticipants();
        returnString = returnString.concat("\n\nBLUE TEAM:");
        for (CurrentGameParticipant participant : participants) {
            if (participant.getTeamId() == 0) {
                returnString = returnString.concat("\n" + api.getChampion(Platform.OCE, participant.getChampionId()) + " (" + participant.getSummonerName() + ")");
            }
        }
        returnString = returnString.concat("\n\nPURPLE TEAM:");
        for (CurrentGameParticipant participant : participants) {
            if (participant.getTeamId() == 1) {
                returnString = returnString.concat("\n" + participant.getChampionId() + " (" + participant.getSummonerName() + ")");
            }
        }

        return returnString.concat("\n```");
    }

    private void help(IUser user) {
        IPrivateChannel privateDM = SkisBot.discordClient.getOrCreatePMChannel(user);
        String helpString = "```";
        helpString += "\n!lol link - links your discord account with your League of Legends Discord integration";
        helpString += "\n!lol view <summonerName> - provides a short description of the player's current game";
        helpString += "\n!lol auth - privately sends an authorization link for this bot";
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

    public static RiotApi getApi() {return api;}

    public static String getMapName(int id) {
        String[] maps = {"0", "Summoner's Rift", "Summoner's Rift", "The Proving Grounds", "The Twisted Treeline", "5", "6", "7", "The Crystal Scar", "9", "The Twisted Treeline", "Summoner's Rift", "The Howling Abyss", "13", "The Butcher's Bridge", "15", "The Cosmic Ruins"};
        return maps[id];
    }

}
