package module.kalj123.skisbot.config;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

/**
 * Project: skisbot
 * Created by Kale on 15/06/2017 at 3:03 AM.
 */
class KGuild {

    private IGuild guild;
    private IChannel botChannel;

    KGuild(IGuild guild, IChannel botChannel) {
        this.guild = guild;
        this.botChannel = botChannel;
    }

    IGuild getGuild() {
        return guild;
    }

    IChannel getBotChannel() {
        return botChannel;
    }

    void setBotChannel(IChannel channel) {
        botChannel = channel;
    }
}
