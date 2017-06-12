package module.kalj123.skisbot.league;

import module.kalj123.skisbot.Permissions;
import module.kalj123.skisbot.SkisBot;
import module.kalj123.skisbot.config.Config;
import module.kalj123.skisbot.config.Players;
import module.kalj123.skisbot.league.Player;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IPrivateChannel;
import sx.blah.discord.handle.obj.IUser;

/**
 * Created by Kale on 13/06/2017.
 */
public class LOLAdmin {
    private Permissions permissions;
    private Players players;
    private Config config;

    public LOLAdmin(Players players, Config config) {
        permissions = new Permissions();
        this.players = players;
        this.config = config;
    }

    private String unrecognised() {
        return "Unrecognised LoL command";
    }

    public String handle(String[] args, IUser user, IGuild guild) {
        if (!permissions.isUserHighestRole(user, guild)) {
            return "You don't have permission to do this";
        }
        switch(args[2]) {
            case "unlink":
                return unLink(args[3]);
            case "viewplayers":
                return players.viewPlayers();
            case "botchannel":
                return botChannel(guild);
            case "help":
                return help(user);
            default:
                return unrecognised();
        }
    }

    private String unLink(String discordID) {
        String lines[] = discordID.split("#");
        if (players.removePlayerByDiscordID(lines[0], lines[1] )) {
            return "Successfully unlinked " + discordID;
        } else {
            return "Unlink was unsuccessful";
        }
    }

    private String botChannel(IGuild guild) {
        return "Bot channel is " + config.getGuildBotChannel(guild).getName();
    }

    private String help(IUser user) {
        IPrivateChannel privateDM = SkisBot.discordClient.getOrCreatePMChannel(user);
        String helpString = "```";
        helpString += "\n!lol admin unlink <discordID> - unlinks via Discord ID";
        helpString += "\n!lol admin viewplayers - shows linked Discord IDs with corresponding Summoner names";
        helpString += "\n!lol admin botchannel - reveals the channel configured to be to the bot channel";
        helpString += "\n```";
        privateDM.sendMessage(helpString);
        return "Sent admin help to user " + user.getName();
    }

}
