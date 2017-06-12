package module.kalj123.skisbot.meme;

import module.kalj123.skisbot.SkisBot;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.*;

import java.io.File;

/**
 * Created by Kale on 13/06/2017.
 */
public class MemeHandler {

    private boolean busy;
    private IVoiceChannel channel;

    public void onTrackFinishEvent() {
        busy = false;
        channel.leave();
    }

    public void handler(MessageReceivedEvent event) {

        updateStatus();

        switch (event.getMessage().getContent()) {
            case "!skis":
                playVoice(event, "src/main/resources/skis.wav");
                break;
            case "!skislongthisisalljusttodiscouragespamandtopreventoverusekthanksbailolrektming":
                playVoice(event, "src/main/resources/skislong.wav");
                break;
            case "!jesus":
                playVoice(event, "src/main/resources/jesus.wav");
                break;
            case "!shame":
                playVoice(event, "src/main/resources/shame.mp3");
                break;
            case "!hiitline":
                playVoice(event, "src/main/resources/hiitline.mp3");
                break;
            case "!god":
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
                break;

            case "!timeforleague":
                sendFile(event.getMessage(), "src/main/resources/timeforleague.jpg");
                break;
            case "!timeforskis":
                sendFile(event.getMessage(), "src/main/resources/timeforskis.jpg");
                break;
            case "!slashingprices":
                sendFile(event.getMessage(), "src/main/resources/slashingprices.png");
                break;
            case "!rosetta":
                event.getMessage().getChannel().sendMessage("http://www.rosettastone.com");
                String authName = event.getMessage().getAuthor().getName();
                event.getMessage().getChannel().sendMessage("by " + authName);
                event.getMessage().delete();
                break;
            case "!backfromthedead":
                sendFile(event.getMessage(), "src/main/resources/skisisback.jpg");
                break;
            case "!truck":
                sendFile(event.getMessage(), "src/main/resources/truck.jpg");
                break;
            case "!lizstart":
                event.getMessage().getChannel().sendMessage(randomLizConvoStarter());
                break;
            case "!help":
                help(event.getMessage());
            default:
                if (event.getMessage().getContent().contains("skis")) {
                    event.getMessage().getChannel().sendMessage("SKIIIIIIIIIIIIIIS");
                }
                if (event.getMessage().getContent().contains("kys")) {
                    event.getMessage().getChannel().sendMessage("Good Team");
                }
                if (event.getMessage().getContent().startsWith("!")) {
                    event.getMessage().delete();
                }
        }
    }

    private void help(IMessage message) {
        IPrivateChannel privateDM = SkisBot.discordClient.getOrCreatePMChannel(message.getAuthor());
        message.delete();
        String helpString = "```";
        helpString += "\n!timeforleague - sends time for league picture with mention to @here";
        helpString += "\n!skis - skiskiski in voice channel you are currently connected to";
        helpString += "\n!jesus - jesus line from idmyt in voice channel you are currently connected to";
        helpString += "\n!shame - shame shame shame from idmyt in voice channel you are currently connected to";
        helpString += "\n!timeforskis - sends time for skis picture to main chat with no mention";
        helpString += "\n!rosetta - links them to www.rosettastone.com to help them fix that shit grammar";
        helpString += "\n!slashingprices - sends goodguys picture to main chat, ask angus for reasons why";
        helpString += "\n!truck - sends the truck into fullview as requested by angus";
        helpString += "\n!lizstart - starts a conversation. *Liz style*";
        helpString += "\n!god - dominus the touch of god plus an extra 4th line";
        helpString += "\n!help - this";
        helpString += "\n!lol help - explanation of league integration commands";
        helpString += "\n```";
        privateDM.sendMessage(helpString);
    }

    private void sendFile(IMessage message, String pathname) {
        try {
            message.getChannel().sendFile(new File(pathname));
        } catch (Exception e) {
            System.out.println("Error sending file: " + e.getMessage());
        }
        String authName = message.getAuthor().getName();
        message.getChannel().sendMessage("@here by " + authName);
        message.delete();
    }

    private String randomLizConvoStarter() {
        double number = java.lang.Math.random();
        if (number > 0.8) {
            return "blah";
        } else {
            if (number > 0.6) {
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

    public void updateStatus() {
        double checknumber = Math.random();
        // event.getMessage().getChannel().sendMessage("the number is " + checknumber);
        if (checknumber <= 0.10) {
            SkisBot.discordClient.streaming(randomStatus(), "");
        }
    }

    private void playVoice(MessageReceivedEvent event, String file) {
        if (!busy) {
            busy = true;
            IUser theAuthor = event.getMessage().getAuthor();
            IGuild theGuild = event.getMessage().getGuild();
            channel = theAuthor.getVoiceStateForGuild(theGuild).getChannel();
            channel.join();
            event.getMessage().delete();
            //Thread.sleep(1000);
            try {
                SkisBot.playAudioFromFile(file, theGuild);
            } catch (Exception e) {
                System.out.println("Error playing audio: " + e.getMessage());
                channel.leave();
            }
            //Thread.sleep(delay);
            //channel.leave();
        }
    }
}
