package module.kalj123.skisbot;
/**
 * Created by kalj123 on 31/7/2016.
 */

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
import java.lang.String.*;

public class EventHandler {

    private boolean busy; //flag used to determine if playing sound in voice channel or not
    private IVoiceChannel channel; //voice channel needs to be set as global so it can be opened in one place and closed in another
    private LeagueHandler league; //object to handle League integration
    private final String botChannelId = "319095075994992641"; //Channel to be used for bot only chatter (i.e. League integration output)

    public EventHandler() {
        try {
            league = new LeagueHandler();
            System.out.println("League Handler Online!");
        } catch (IOException e) {
            System.out.println("Unable to create League Handler! Error message: " + e.getMessage());
        }


    }


    @EventSubscriber
    public void onReadyEvent(ReadyEvent event) {
        System.out.println("The bot is now ready");
        String currentStatus = randomStatus();
        SkisBot.discordClient.streaming(currentStatus,"");
    }

    @EventSubscriber
    public void onTrackFinishEvent(TrackFinishEvent event) {
        busy = false;
        channel.leave();
    }

    @EventSubscriber
    public void onMessageEvent(MessageReceivedEvent event) throws IOException, DiscordException, MissingPermissionsException, RateLimitException, UnsupportedAudioFileException, InterruptedException {

        if (event.getMessage().getContent().startsWith("!lol")) {
            league.handler(event);
        }

        if (!busy) {
            if (event.getMessage().getContent().equals("!skis")) {
                playVoice(event, "src/main/resources/skis.wav");
            }

            if (event.getMessage().getContent().equals("!skislongthisisalljusttodiscouragespamandtopreventoverusekthanksbailolrektming")) {
                playVoice(event, "src/main/resources/skislong.wav");
            }

            if (event.getMessage().getContent().equals("!jesus")) {
                playVoice(event, "src/main/resources/jesus.wav");
            }

            if (event.getMessage().getContent().equals("!shame")) {
                playVoice(event, "src/main/resources/shame.mp3");
            }

            if (event.getMessage().getContent().equals("!hiitline")) {
                playVoice(event, "src/main/resources/hiitline.mp3");
            }

            if (event.getMessage().getContent().equals("!god")) {
                double number = Math.random();
                if (number < 0.25) {
                    playVoice(event, "src/main/resources/god1.wav");
                } else if (number < 0.5) {
                    playVoice(event, "src/main/resources/god2.wav");
                } else if (number < 0.75) {
                    playVoice(event, "src/main/resources/god3.wav");
                } else {
                    playVoice(event, "src/main/resources/god4.wav");
                }
            }
        } else event.getMessage().delete();

        if (event.getMessage().getContent().equals("!timeforleague")) {
            event.getMessage().getChannel().sendFile(new File("src/main/resources/timeforleague.jpg"));
            String authName = event.getMessage().getAuthor().getName();
            event.getMessage().getChannel().sendMessage("@here by " + authName);
            event.getMessage().delete();
        }

        if (event.getMessage().getContent().equals("!timeforskis")) {
            event.getMessage().getChannel().sendFile(new File("src/main/resources/timeforskis.jpg"));
            String authName = event.getMessage().getAuthor().getName();
            event.getMessage().getChannel().sendMessage("by " + authName);
            event.getMessage().delete();
        }

        if (event.getMessage().getContent().equals("!slashingprices")) {
            event.getMessage().getChannel().sendFile(new File("src/main/resources/slashingprices.png"));
            String authName = event.getMessage().getAuthor().getName();
            event.getMessage().getChannel().sendMessage("by " + authName);
            event.getMessage().delete();
        }

        if (event.getMessage().getContent().equals("!rosetta")) {
            event.getMessage().getChannel().sendMessage("http://www.rosettastone.com");
            String authName = event.getMessage().getAuthor().getName();
            event.getMessage().getChannel().sendMessage("by " + authName);
            event.getMessage().delete();
        }

        if (event.getMessage().getContent().equals("!backfromthedead")) {
            event.getMessage().getChannel().sendFile(new File("src/main/resources/skisisback.jpg"));
            String authName = event.getMessage().getAuthor().getName();
            event.getMessage().getChannel().sendMessage("by " + authName);
            event.getMessage().delete();
        }

        if (event.getMessage().getContent().equals("!truck")) {
            event.getMessage().getChannel().sendFile(new File("src/main/resources/truck.jpg"));
            String authName = event.getMessage().getAuthor().getName();
            event.getMessage().getChannel().sendMessage("by " + authName);
            event.getMessage().delete();
        }

        if (event.getMessage().getContent().equals("!lizstart")) {
            event.getMessage().getChannel().sendMessage(randomLizConvoStarter());
        }


        if (event.getMessage().getContent().toLowerCase().contains("skis") && !event.getMessage().getContent().toLowerCase().startsWith("!")) {
            event.getMessage().getChannel().sendMessage("SKIIIIIIIIIIIIIIS");
        }

        if (event.getMessage().getContent().toLowerCase().contains("kys") && !event.getMessage().getContent().toLowerCase().startsWith("!")) {
            event.getMessage().getChannel().sendMessage("Good Team");
        }

        if (event.getMessage().getContent().equals("!help")) {
            IPrivateChannel privateDM = SkisBot.discordClient.getOrCreatePMChannel(event.getMessage().getAuthor());
            event.getMessage().delete();
            privateDM.sendMessage("!timeforleague - sends time for league picture with mention to @here");
            privateDM.sendMessage("!skis - skiskiski in voice channel you are currently connected to");
            privateDM.sendMessage("!jesus - jesus line from idmyt in voice channel you are currently connected to");
            privateDM.sendMessage("!shame - shame shame shame from idmyt in voice channel you are currently connected to");
            privateDM.sendMessage("!timeforskis - sends time for skis picture to main chat with no mention");
            Thread.sleep(5000);
            privateDM.sendMessage("!rosetta - links them to www.rosettastone.com to help them fix that shit grammar");
            privateDM.sendMessage("!slashingprices - sends goodguys picture to main chat, ask angus for reasons why");
            privateDM.sendMessage("!truck - sends the truck into fullview as requested by angus");
            privateDM.sendMessage("!lizstart - starts a conversation. *Liz style*");
            privateDM.sendMessage("!god - dominus the touch of god plus an extra 4th line");
            Thread.sleep(5000);
            privateDM.sendMessage("!help - this");
        }

        double checknumber = Math.random();
        // event.getMessage().getChannel().sendMessage("the number is " + checknumber);
        if (checknumber <= 0.10) {
            SkisBot.discordClient.streaming(randomStatus(), "");
        }
    }

    private String randomStatus() {
        double number = java.lang.Math.random();
        if(number < 0.34) {
            //System.out.print("1, " + number);
            return "hiitline bling";
        } else {
            if(number < 0.67) {
                //System.out.print("2, " + number);
                return "IDM Youtube";
            } else {
                //System.out.print("3, " + number);
                return "flames";
            }
        }
    }

    private String randomLizConvoStarter() {
        double number = java.lang.Math.random();
        if(number > 0.8){
            return "blah";
        } else {
            if(number > 0.6) {
                return "sigh";
            } else {
                if (number > 0.4) {
                    return "boo";
                } else {
                    if (number > 0.2) {
                        return "im bored";
                    } else {
                        return "hai";
                    }
                }
            }
        }
    }

    private void playVoice(MessageReceivedEvent event, String file) throws InterruptedException, IOException, UnsupportedAudioFileException {
        busy = true;
        IUser theAuthor = event.getMessage().getAuthor();
        IGuild theGuild = event.getMessage().getGuild();
        channel = theAuthor.getVoiceStateForGuild(theGuild).getChannel();
        channel.join();
        event.getMessage().delete();
        //Thread.sleep(1000);
        SkisBot.playAudioFromFile(file, theGuild);
        //Thread.sleep(delay);
        //channel.leave();
    }

    public String getBotChannelId() {
        return botChannelId;
    }
}
//
