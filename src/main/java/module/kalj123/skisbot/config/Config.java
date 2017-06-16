package module.kalj123.skisbot.config;

import ch.qos.logback.classic.spi.STEUtil;
import sx.blah.discord.handle.impl.obj.Guild;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
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

    private final String config = "config.txt"; ///module/kalj123/skisbot/resources/
    private Path configPath = Paths.get(config);
    private Charset utf8 = StandardCharsets.UTF_8;

    private ArrayList<KGuild> guildList;
    private List<String>configLines;

    public Config(List<IGuild> guilds) {
        guildList = new ArrayList<KGuild>();

        for (IGuild guild : guilds) {
            guildList.add(new KGuild(guild, null));
        }

        if(!Files.exists(configPath)) {
                createConfig(guilds);
        }

        readConfig();
    }

    private void readConfig() {
        try {
            configLines = Files.readAllLines(configPath, utf8);
            for (String line : configLines) {
                String[] lines = line.split(":");
                for (KGuild kGuild : guildList) {
                    if(("guild_" + kGuild.getGuild().getStringID() + "_botchannel").equals(lines[0])) {
                        if (lines[0].equals("guild_" + kGuild.getGuild().getStringID() + "_botchannel") && !lines[1].substring(1).equals("")) {
                            kGuild.setBotChannel(kGuild.getGuild().getChannelByID(Long.parseLong(lines[1].substring(1))));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeConfig() {
        try {
            Files.write(configPath, configLines, utf8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void createConfig(List<IGuild> guilds) {
        System.out.println("Config file not found, creating...");
        try {
            Files.createFile(configPath);
            System.out.println("Config file created!");
            List<String> lines = new ArrayList<String>();
            lines.add("riot_api_key:-");
            System.out.println("Please add a RIOT API key to config.txt for league interaction to function!");
            for (IGuild guild : guilds) {
                lines.add("guild_" + guild.getStringID() + "_botchannel:-");
                Files.write(configPath, lines, utf8);
                System.out.println("Added entry for guild '" + guild.getName() + "', please add a botChannel ID to this guild, unless general channel is fine");
                guildList.add(new KGuild(guild, null));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public IChannel getGuildBotChannel(IGuild guild) {
        for (KGuild kGuild : guildList) {
            if(kGuild.getGuild() == guild ) {
                if (kGuild.getBotChannel() == null) {
                    return kGuild.getGuild().getGeneralChannel();
                }
                return kGuild.getBotChannel();
            }
        }
        return null;
    }

    public String setGuildBotChannel(IGuild guild, String channelID) {
        if (guild.getChannelByID(Long.parseLong(channelID)) != null) {
            for (KGuild kGuild : guildList) {
                if (kGuild.getGuild() == guild) {
                    kGuild.setBotChannel(guild.getChannelByID(Long.parseLong(channelID)));
                    writeBotChannel(guild, channelID);
                    return "Set the Bot Channel for this Guild to " + kGuild.getBotChannel().getName();
                }
            }
        }
        return "Invalid ChannelID";
    }

    private void writeBotChannel(IGuild guild, String channelID) {
        int count = 0;
        for (String line : configLines) {
            String[] lines = line.split(":");
            if (lines[0].equals("guild_" + guild.getStringID() + "_botchannel")) {
                configLines.set(count, "guild_" + guild.getStringID() + "_botchannel:-" + channelID);
            }
            count++;
        }
        writeConfig();
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
