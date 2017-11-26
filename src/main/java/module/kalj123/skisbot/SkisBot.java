package module.kalj123.skisbot;

import module.kalj123.skisbot.database.SQLite;
import module.kalj123.skisbot.database.SQLitePlayers;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.audio.AudioPlayer;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Project: skisbot
 * Created by kalj123 on 31/7/2016 at 2:54 AM.
 */
public class SkisBot {
    public static IDiscordClient discordClient;
    private static String token = "token.txt";
    private static Path tokenPath = Paths.get(token);
    private static Charset utf8 = StandardCharsets.UTF_8;

    public static void main(String[] args) throws Exception {
        if (Files.exists(tokenPath)) {
            discordClient = getClient();
            discordClient.getDispatcher().registerListener(new EventHandler());
            SQLite.connect();
            SQLitePlayers.connect();
        } else {
            createTokenFile();
        }
    }

    private static IDiscordClient getClient() throws DiscordException, IOException {
        return new ClientBuilder().withToken(retrieveToken()).login();
    }

    private static String retrieveToken() throws IOException {
        List<String> tokenLines;
        tokenLines = Files.readAllLines(tokenPath, utf8);
        //System.out.println(tokenLines);
        return tokenLines.get(0).split(":")[1];
    }

    private static void createTokenFile() {
        try {
            Files.createFile(tokenPath);
            List<String> tokenLines = new ArrayList<>();
            tokenLines.add("bot_token:");
            Files.write(tokenPath, tokenLines, utf8);
            System.out.println("Created Token File, please add the token to the file!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void playAudioFromFile(String s_file, IGuild guild) throws IOException, UnsupportedAudioFileException {
        URL url;
        if (SkisBot.class.getResource("SkisBot.class").toString().startsWith("file:")) {
            url = new File("src/main/resources/resources/" + s_file).toURI().toURL();
        } else {
            //System.out.println(SkisBot.class.getResource("/resources/" + s_file).toString());
            System.out.println("/resources/" + s_file);
            url = SkisBot.class.getResource("/resources/" + s_file);
        }
        AudioPlayer player = AudioPlayer.getAudioPlayerForGuild(guild);
        player.queue(url);
    }
}
/*
Checklist for updating:
-   update !help commands
-   change versions in .pom
*/
