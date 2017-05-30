package module.kalj123.skisbot; /**
 * Created by Kale on 31/7/2016.
 */

import sun.plugin2.message.Message;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class EventHandler {

    private boolean busy;


    @EventSubscriber
    public void onReadyEvent(ReadyEvent event) {
        System.out.println("The bot is now ready");
        String currentStatus = randomStatus();
        SkisBot.discordClient.streaming(currentStatus,"");
    }

    @EventSubscriber
    public void onMessageEvent(MessageReceivedEvent event) throws IOException, DiscordException, MissingPermissionsException, RateLimitException, UnsupportedAudioFileException, InterruptedException {
        if (!busy) {
            if (event.getMessage().getContent().equals("!skis")) {
                busy = true;
                playVoice(event, "src/main/resources/skis.wav", 2000);
                busy = false;
            }

            if (event.getMessage().getContent().equals("!skislongthisisalljusttodiscouragespamandtopreventoverusekthanksbailolrektming")) {
                busy = true;
                playVoice(event, "src/main/resources/skislong.wav", 20000);
                busy = false;
            }

            if (event.getMessage().getContent().equals("!jesus")) {
                busy = true;
                playVoice(event, "src/main/resources/jesus.wav", 3000);
                busy = false;
            }

            if (event.getMessage().getContent().equals("!shame")) {
                busy = true;
                playVoice(event, "src/main/resources/shame.mp3", 7000);
            }

            if (event.getMessage().getContent().equals("!hiitline")) {
                busy = true;
                playVoice(event, "src/main/resources/hiitline.mp3", 3000);
                busy = false;
            }

            if (event.getMessage().getContent().equals("!god")) {
                busy = true;
                double number = Math.random();
                if (number < 0.25) {
                    playVoice(event, "src/main/resources/god1.wav", 5000);
                } else if (number < 0.5) {
                    playVoice(event, "src/main/resources/god2.wav", 5000);
                } else if (number < 0.75) {
                    playVoice(event, "src/main/resources/god3.wav", 5000);
                } else {
                    playVoice(event, "src/main/resources/god4.wav", 5000);
                }
                busy = false;
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

    private void playVoice(MessageReceivedEvent event, String file, int delay) throws InterruptedException, IOException, UnsupportedAudioFileException {
        IUser theAuthor = event.getMessage().getAuthor();
        IGuild theGuild = event.getMessage().getGuild();
        IVoiceChannel channel = theAuthor.getVoiceStateForGuild(theGuild).getChannel();
        channel.join();
        event.getMessage().delete();
        Thread.sleep(1000);
        SkisBot.playAudioFromFile(file, theGuild);
        Thread.sleep(delay);
        channel.leave();
    }
}
//
