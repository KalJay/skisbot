package module.kalj123.skisbot;

import com.github.xaanit.d4j.oauth.handle.impl.events.OAuthUserAuthorized;
import module.kalj123.skisbot.database.SQLite;
import module.kalj123.skisbot.database.SQLitePlayers;
import module.kalj123.skisbot.league.LeagueHandler;
import module.kalj123.skisbot.meme.MemeHandler;
import module.kalj123.skisbot.oauth.OAuth;
import net.rithms.riot.api.RiotApiException;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;
import sx.blah.discord.handle.impl.obj.Guild;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.audio.events.TrackFinishEvent;

import java.io.IOException;

/**
 * Project: skisbot
 * Created by kalj123 on 31/7/2016 at 2:54 AM.
 */
public class EventHandler {


    private LeagueHandler league;
    private MemeHandler meme;

    EventHandler() {
        try {
            league = new LeagueHandler();
            System.out.println("League Handler Online!");
        } catch (IOException e) {
            System.out.println("Unable to create League Handler! Error message: " + e.getMessage());
        }
        meme = new MemeHandler(this);
        System.out.println("Meme Handler Online!");
    }


    @EventSubscriber
    public void onReadyEvent(ReadyEvent event) {
        System.out.println("The bot is now ready");
        meme.setStatus();
        league.startConfig(SkisBot.discordClient.getGuilds());
        new OAuth();
        SQLite.updateGuilds(SkisBot.discordClient.getGuilds());
        SQLitePlayers.updateGuilds(SkisBot.discordClient.getGuilds());
    }

    @EventSubscriber
    public void onOAuthUserAuthorizedEvent(OAuthUserAuthorized event) throws RiotApiException {
        OAuth.handleAuthorizedEvent(event.getUser());
        System.out.println("User Authorized via OAuth 2.0 : " + event.getUser().getAccessToken());
    }

    @EventSubscriber
    public void onTrackFinishEvent(TrackFinishEvent event) {
        meme.onTrackFinishEvent();
    }

    @EventSubscriber
    public void onUserJoinEvent(UserJoinEvent event) {
        SQLitePlayers.updateGuilds(SkisBot.discordClient.getGuilds());
    }

    @EventSubscriber
    public void onGuildCreateEvent(GuildCreateEvent event) {
        SQLite.updateGuilds(SkisBot.discordClient.getGuilds());
    }

    @EventSubscriber
    public void onMessageEvent(MessageReceivedEvent event) throws RiotApiException {

        if (event.getMessage().getContent().startsWith("!lol ")) {
            league.handler(event);
            deleteMessage(event.getMessage());
        } else {
            meme.handler(event);
        }

    }

    public void deleteMessage(IMessage message) {
        if(!message.getChannel().isPrivate()) {
            message.delete();

        }
    }
}
//
