package module.kalj123.skisbot.config;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

/**
 * Created by Kale on 15/06/2017.
 */
public class KGuild {

    private IGuild guild;
    private IChannel botChannel;

    public KGuild(IGuild guild, IChannel botChannel) {
        this.guild = guild;
        this.botChannel = botChannel;
    }

    public IGuild getGuild() {
        return guild;
    }

    public IChannel getBotChannel() {
        return botChannel;
    }

    public void setBotChannel(IChannel channel) {
        botChannel = channel;
    }
}
