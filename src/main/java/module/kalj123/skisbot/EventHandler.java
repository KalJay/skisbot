package module.kalj123.skisbot;

import module.kalj123.skisbot.league.LeagueHandler;
import module.kalj123.skisbot.meme.MemeHandler;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import sx.blah.discord.util.audio.events.TrackFinishEvent;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

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
        meme = new MemeHandler();
        System.out.println("Meme Handler Online!");
    }


    @EventSubscriber
    public void onReadyEvent(ReadyEvent event) {
        System.out.println("The bot is now ready");
        meme.updateStatus();
        league.startConfig(SkisBot.discordClient.getGuilds());
    }

    @EventSubscriber
    public void onTrackFinishEvent(TrackFinishEvent event) {
        meme.onTrackFinishEvent();
    }

    @EventSubscriber
    public void onMessageEvent(MessageReceivedEvent event) {

        if (event.getMessage().getContent().startsWith("!lol")) {
            league.handler(event);
            event.getMessage().delete();
        } else {
            meme.handler(event);
        }

    }





}
//
