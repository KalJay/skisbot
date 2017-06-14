package module.kalj123.skisbot;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.audio.AudioPlayer;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by kalj123 on 31/7/2016.
 */
public class SkisBot {
    public static IDiscordClient discordClient;

    public void disable() {}

    public boolean enable(IDiscordClient dclient) {
        discordClient = dclient;
        EventDispatcher dispatcher = discordClient.getDispatcher();
        dispatcher.registerListener(new EventHandler());
        return true;
    }

    public String getAuthor() {
        return "kalj123";
    }
    public String getMinimumDiscord4JVersion() {
        return "2.8.0";
    }
    public String getName() {
        return "SKISBot";
    }
    public String getVersion() {
        return "1.1";
    }

    public static void main(String[] args) throws Exception {
        discordClient = getClient();
        discordClient.getDispatcher().registerListener(new EventHandler());
    }

    private static IDiscordClient getClient() throws DiscordException {
        return new ClientBuilder().withToken("MjA4OTAxMjEyMzE2MTA2NzUz.CoCNlg.X-FC-9PKvxBQcuzpXUWD2g3M1QU").login();
    }

    //credit to oopsjpeg's tutorial at https://github.com/oopsjpeg/d4j-audioplayer
    // Queue audio from specified file for guild
    public static void playAudioFromFile(String s_file, IGuild guild) throws IOException, UnsupportedAudioFileException {
        //File file = new File(s_file); // Get file
        URL url;
        if (SkisBot.class.getResource("SkisBot.class").toString().startsWith("file:")) {
            url = new File("src/main/resources/resources/" + s_file).toURI().toURL();
        } else {
            //System.out.println(SkisBot.class.getResource("/resources/" + s_file).toString());
            System.out.println("/resources/" + s_file);
            url = SkisBot.class.getResource("/resources/" + s_file);
        }
        AudioPlayer player = AudioPlayer.getAudioPlayerForGuild(guild); // Get AudioPlayer for guild
        player.queue(url); // Queue file
    }
}
/*
SKIS BOT: MjA4OTAxMjEyMzE2MTA2NzUz.CoCNlg.X-FC-9PKvxBQcuzpXUWD2g3M1QU
KaljTestBot: MjA4ODg5NzA5MzYxODg5Mjgx.CoEgxA.ZV1wxXT3ORQx6JcILResXg0WUlw
Checklist for updating:
-   update !help
-   change version in .pom
-   change version in getVersion()
-   update version in getMinimumDiscord4JVersion() if necessary
-   use compile.bat
-   update discord4j-shaded if necessary
*/
