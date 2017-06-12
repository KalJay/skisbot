package module.kalj123.skisbot.config;

import sx.blah.discord.handle.impl.obj.Guild;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kale on 12/06/2017.
 */
public class Config {

    private final String config = "src/main/resources/config.txt";
    private Path configPath = Paths.get(config);
    Charset utf8 = StandardCharsets.UTF_8;

    private List<IGuild> guildList;
    private ArrayList<IChannel> botChannels;

    public Config(List<IGuild> guilds) {
        guildList = guilds;
        botChannels = new ArrayList<IChannel>();

        if(!Files.exists(configPath)) {
            try {
                createConfig(guildList);
            } catch (Exception e){
                System.out.println("Error creating config file!\n" + e.getMessage());
            }
        }
        try {
            readConfig(guildList);
        } catch (Exception e) {
            System.out.println("Error reading config file!\n" + e.getMessage());
        }

    }

    private void readConfig(List<IGuild> guilds) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(config));
        String line;
        for (IGuild guild : guilds) {
            line = br.readLine();
            String[] lines = line.split(":");
            if(("guild_" + guild.getStringID() + "_botchannel").equals(lines[0])) {
                if (lines[0].equals("guild_" + guild.getStringID() + "_botchannel")) {
                    botChannels.add(guild.getChannelByID(Long.parseLong(lines[1].substring(1))));
                } else {
                    botChannels.add(guild.getGeneralChannel());
                }
            }
        }
    }


    private void createConfig(List<IGuild> guilds) throws IOException {
        System.out.println("Config file not found, creating...");
        Files.createFile(configPath);
        System.out.println("Config file created!");
        List<String> lines = new ArrayList<String>();
        for (IGuild guild : guilds) {
            lines.add("guild_" + guild.getStringID() + "_botchannel:-");
            Files.write(configPath, lines, utf8);
            System.out.println("Added botChannel entry for guild '" + guild.getName() + "', please add a botChannel ID to this for league interaction to work!");
        }
        botChannels.clear();
    }
    public IChannel getGuildBotChannel(IGuild guild) {
        int index = guildList.indexOf(guild);
        if(index == -1)
            return guild.getGeneralChannel();
        return botChannels.get(index);
    }
}
