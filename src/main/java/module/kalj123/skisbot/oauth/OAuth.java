package module.kalj123.skisbot.oauth;

import com.github.xaanit.d4j.oauth.Scope;
import com.github.xaanit.d4j.oauth.handle.IConnection;
import com.github.xaanit.d4j.oauth.handle.IDiscordOAuth;
import com.github.xaanit.d4j.oauth.handle.IOAuthUser;
import com.github.xaanit.d4j.oauth.util.DiscordOAuthBuilder;
import io.vertx.core.http.HttpServerOptions;
import module.kalj123.skisbot.SkisBot;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;
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

    public boolean active = true;

    public OAuth() {
        if (Files.exists(oauthPath)) {
            readOAuthFile();
            iDiscordOAuth = new DiscordOAuthBuilder(SkisBot.discordClient)
                    .withClientID(clientID)
                    .withClientSecret(clientSecret)
                    .withRedirectUrl("http://101.164.144.156:8787/callback")
                    .withHttpServerOptions(new HttpServerOptions().setPort(8787))
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
            active = false;
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
        active = false;
    }

    public IOAuthUser getOAuthUser(IUser user) {
        return iDiscordOAuth.getOAuthUser(user);
    }

    public boolean isLeagueUser(IUser user) {
        if (iDiscordOAuth.getOAuthUser(user) == null) {
            return false;
        }
        for (IConnection connection : iDiscordOAuth.getOAuthUser(user).getConnections()) {
            if (connection.getName().equals("League of Legends")) {
                return true;
            }
        }
        return false;
    }

    public void postAuthLink() {
        for(IGuild guild : SkisBot.discordClient.getGuilds()) {
            guild.getGeneralChannel().sendMessage("Authorise me! <" + iDiscordOAuth.buildAuthUrl() + ">");
        }
    }
}
