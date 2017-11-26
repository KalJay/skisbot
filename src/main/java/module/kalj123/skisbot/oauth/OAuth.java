package module.kalj123.skisbot.oauth;

import com.github.xaanit.d4j.oauth.Scope;
import com.github.xaanit.d4j.oauth.handle.IConnection;
import com.github.xaanit.d4j.oauth.handle.IDiscordOAuth;
import com.github.xaanit.d4j.oauth.handle.IOAuthUser;
import com.github.xaanit.d4j.oauth.util.DiscordOAuthBuilder;
import com.robrua.orianna.api.core.RiotAPI;
import io.vertx.core.http.HttpServerOptions;
import module.kalj123.skisbot.SkisBot;
import module.kalj123.skisbot.config.Players;
import module.kalj123.skisbot.database.SQLitePlayers;
import module.kalj123.skisbot.league.LeagueHandler;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.constant.Platform;
import sx.blah.discord.handle.impl.events.module.ModuleEvent;
import sx.blah.discord.handle.obj.IUser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Project: skisbot
 * Created by Kale on 3/07/2017 at 9:49 PM.
 */
public class OAuth {

    private String oauth = "oauth.txt";
    private Path oauthPath = Paths.get(oauth);
    private  Charset utf8 = StandardCharsets.UTF_8;

    private static IDiscordOAuth iDiscordOAuth;
    private static String clientID;
    private static String clientSecret;

    public OAuth() {
        if (Files.exists(oauthPath)) {
            readOAuthFile();
            iDiscordOAuth = new DiscordOAuthBuilder(SkisBot.discordClient)
                    .withClientID(clientID)
                    .withClientSecret(clientSecret)
                    .withRedirectUrl("http://101.164.144.156:" + getPortNumber() +"/callback")
                    .withHttpServerOptions(new HttpServerOptions().setPort(Integer.parseInt(getPortNumber())))
                    .withScopes(Scope.IDENTIFY, Scope.CONNECTIONS)
                    .build();
        } else {
            createOAuthFile();
        }
    }

    private void readOAuthFile() {
        try {
            List<String> oauthLines = Files.readAllLines(oauthPath, utf8);
            clientID = oauthLines.get(0).split(":")[1];
            clientSecret = oauthLines.get(1).split(":")[1];
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createOAuthFile() {
        try {
            Files.createFile(oauthPath);
            List<String> oauthLines = new ArrayList<>();
            oauthLines.add("client_token:");
            oauthLines.add("client_secret:");
            Files.write(oauthPath, oauthLines, utf8);
            System.out.println("Created OAuth File, please add the required information!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static IOAuthUser getOAuthUser(IUser user) {
        return iDiscordOAuth.getOAuthUser(user);
    }

    public static String getAuthLink() {
        return iDiscordOAuth.buildAuthUrl();
    }

    private static String getPortNumber() {
        if(SkisBot.discordClient.getOurUser().getName().equals("KaljTestBot")) {
            return "8787";
        } else {return "8788";}
    }

    public static IConnection getLeagueConnection(IUser user) {
        for(IConnection connection : getOAuthUser(user).getConnections()) {
            if (connection.getType().equals("leagueoflegends")) {
                return connection;
            }
        }
        return null;
    }

    public static void handleAuthorizedEvent(IOAuthUser user) throws RiotApiException {
        SQLitePlayers.addSummonerIDToPlayerWithoutGuild(user, (LeagueHandler.getApi().getSummonerByName(Platform.OCE, getLeagueConnection(user).getID())).getId());
    }
}
