package module.kalj123.skisbot.config;

import module.kalj123.skisbot.database.SQLite;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

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
 * Created by Kale on 12/06/2017 at 3:01 AM.
 */
public class Config {

    private final String config = "config.txt"; ///module/kalj123/skisbot/resources/
    private Path configPath = Paths.get(config);
    private Charset utf8 = StandardCharsets.UTF_8;
    private List<String>configLines;

    public Config(List<IGuild> guilds) {
        if(!Files.exists(configPath)) {
                createConfig(guilds);
        }
        readConfig();
    }

    private void readConfig() {
        try {
            configLines = Files.readAllLines(configPath, utf8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createConfig(List<IGuild> guilds) {
        System.out.println("Config file not found, creating...");
        try {
            Files.createFile(configPath);
            System.out.println("Config file created!");
            List<String> lines = new ArrayList<>();
            lines.add("riot_api_key:-");
            System.out.println("Please add a RIOT API key to config.txt for league interaction to function!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public IChannel getGuildBotChannel(IGuild guild) {
        String ID = SQLite.getBotChannelID(guild);
        if (ID == null) {
            return guild.getGeneralChannel();
        }
        return guild.getChannelByID(Long.parseLong(ID));
    }

    public String setGuildBotChannel(IGuild guild, String channelID) {
        if (guild.getChannelByID(Long.parseLong(channelID) ) == null) {
            return "Invalid Channel ID";
        }
        SQLite.updateBotChannel(guild, channelID);
        return "Bot Channel updated to " + guild.getChannelByID(Long.parseLong(channelID)).getName();
    }

    public String getRiotAPIKey() {
        for (String line : configLines) {
            String[] lines = line.split(":") ;
            if (lines[0].equals("riot_api_key") && !lines[1].substring(1).equals("")) {
                return lines[1].substring(1);
            }
        }
        return "";
    }
}
