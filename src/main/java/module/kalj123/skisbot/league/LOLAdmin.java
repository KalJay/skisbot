package module.kalj123.skisbot.league;

import module.kalj123.skisbot.Permissions;
import module.kalj123.skisbot.SkisBot;
import module.kalj123.skisbot.config.Config;
import module.kalj123.skisbot.config.Players;
import module.kalj123.skisbot.database.SQLitePlayers;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IPrivateChannel;
import sx.blah.discord.handle.obj.IUser;

/**
 * Project: skisbot
 * Created by Kale on 13/06/2017 at 3:01 AM.
 */
class LOLAdmin {
    private Permissions permissions;
    private Players players;
    private Config config;

    LOLAdmin(Players players, Config config) {
        permissions = new Permissions();
        this.players = players;
        this.config = config;
    }

    private String unrecognised() {
        return "Unrecognised LoL command";
    }

    String handle(String[] args, IUser user, IGuild guild) {
        if (!permissions.isUserHighestRole(user, guild)) {
            return "You don't have permission to do this";
        }
        switch(args[2]) {
            case "unlink":
                return unLink(args[3], guild);
            case "viewplayers":
                return players.viewPlayers(guild);
            case "botchannel":
                return botChannel(guild);
            case "setbot":
                return config.setGuildBotChannel(guild, args[3]);
            case "link":
                return linkPlayer(guild, args[3], args[4]);
            case "help":
                return help(user);
            default:
                return unrecognised();
        }

    }

    private String linkPlayer(IGuild guild, String ID, String summonerID) {
        SQLitePlayers.addSummonerIDToPlayer(guild.getUserByID(Long.parseLong(ID)), guild, summonerID);
        return "Successfully linked player";
    }

    private String unLink(String discordID, IGuild guild) {
        SQLitePlayers.deleteSummonerIDFromPlayer(guild.getUserByID(Long.parseLong(discordID)), guild);
        return "Successfully unlinked player";
    }

    private String botChannel(IGuild guild) {
        return "Bot channel is " + config.getGuildBotChannel(guild).getName();
    }

    private String help(IUser user) {
        IPrivateChannel privateDM = SkisBot.discordClient.getOrCreatePMChannel(user);
        String helpString = "```";
        helpString += "\n!lol admin unlink <discordID> - unlinks via Discord ID";
        helpString += "\n!lol admin link <discordID> <summonerName> - links via Discord ID and Summoner name";
        helpString += "\n!lol admin viewplayers - shows linked Discord IDs with corresponding Summoner names";
        helpString += "\n!lol admin botchannel - reveals the channel configured to be to the bot channel";
        helpString += "\n!lol admin setbot <channelID>- sets the bot channel to the channel ID";
        helpString += "\n```";
        privateDM.sendMessage(helpString);
        return "Sent admin help to user " + user.getName();
    }

}
