package module.kalj123.skisbot.config;

import com.github.xaanit.d4j.oauth.handle.IConnection;
import module.kalj123.skisbot.database.SQLitePlayers;
import module.kalj123.skisbot.league.Player;
import module.kalj123.skisbot.oauth.OAuth;
import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.summoner.dto.Summoner;
import net.rithms.riot.constant.Platform;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

/**
 * Project: skisbot
 * Created by Kale on 13/06/2017 at 3:03 AM.
 */
public class Players {

    public static String addPlayerWithCheck(IUser user, IGuild guild, RiotApi api) throws RiotApiException {
        if(OAuth.getOAuthUser(user) == null) {
            return "You have not authorised this bot!";
        }
//        for (IConnection connection : OAuth.getOAuthUser(user).getConnections())
        IConnection connection = OAuth.getLeagueConnection(user);
        if(connection != null) {
            SQLitePlayers.addSummonerIDToPlayer(user, guild, api.getSummonerByName(Platform.OCE, OAuth.getLeagueConnection(user).getName() + "").getName());
            return "Successfully linked " + connection.getName() + " to " + user.getName();
        }
        return "You have not connected League of Legends with Discord!";
    }

    public static Summoner getSummonerFromName(String name, RiotApi api) throws RiotApiException {
        return api.getSummonerByName(Platform.OCE, name);
    }

    public static String viewPlayers(IGuild guild) {
        StringBuilder lines = new StringBuilder("```\nDiscordID, Name");
        for (Player player: SQLitePlayers.getPlayerList(guild)) {
            lines.append("\n").append(player.toString());
        }
        lines.append("\n```");
        return lines.toString();
    }
}
