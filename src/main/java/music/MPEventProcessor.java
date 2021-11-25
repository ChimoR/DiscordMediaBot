package music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import util.MessageSender;
import music.entities.Audio;
import music.entities.AudioQueue;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import util.Timer;

import java.util.ArrayList;

public class MPEventProcessor
{
    public static MPEventProcessor instance = null;
    private static AudioPlayer audioPlayer = null;
    private static AudioPlayerManager playerManager = null;
    private static Guild guild = null;
    private static AudioManager audioManager = null;
    private static AudioQueue audioQueue = new AudioQueue(new ArrayList<>());
    private static MessageChannel textChannel = null;
    private static VoiceChannel currentVoiceChannel = null;

    public MPEventProcessor()
    {
        YoutubeAudioSourceManager youtubeAudioSourceManager = new YoutubeAudioSourceManager(); // ютуб сорс
        youtubeAudioSourceManager.configureRequests(config -> RequestConfig.copy(config)
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                .build());

        playerManager = new DefaultAudioPlayerManager(); // менеджер плеера
        playerManager.registerSourceManager(youtubeAudioSourceManager); // регистрация ютуб сорса в плеере
        AudioSourceManagers.registerRemoteSources(playerManager); // регистрация менеджера
        audioPlayer = playerManager.createPlayer();// в плеер надо добавить листенер, который будет взаимодействовать с траками. Листенер имплементит AudioEventAdapter или AudioEventListener

        TrackScheduler trackScheduler = new TrackScheduler(audioPlayer); // шедулер треков
        audioPlayer.addListener(trackScheduler); // добавляем шедулер в плеер
    }

    public void processPlayEvent(MessageReceivedEvent event)
    {
        String message = event.getMessage().getContentRaw();
        String url = message.substring(message.indexOf(" ") + 1);

        setAndConnectToChannel(event, url);
    }

    public void processPauseEvent(MessageReceivedEvent event)
    {
        if (audioPlayer.getPlayingTrack() == null) {
            MessageSender.sendMessage(event.getChannel(), MessageSender.NOTHING_TO_STOP);
        }
        else
        {
            audioPlayer.setPaused(true);
        }

    }

    public void processResumeEvent(MessageReceivedEvent event)
    {
        if (audioPlayer.getPlayingTrack() == null) {
            MessageSender.sendMessage(event.getChannel(), MessageSender.NOTHING_TO_RESUME);
        }
        else
        {
            audioPlayer.setPaused(false);
        }
    }

    public void processSkipEvent(MessageReceivedEvent event)
    {
        if (audioPlayer.getPlayingTrack() == null)
            MessageSender.sendMessage(event.getChannel(), MessageSender.NOTHING_TO_SKIP);
        else
        {
            Audio nextTrack = audioQueue.getNext();
            if (nextTrack != null)
            {
                audioPlayer.playTrack(nextTrack.getAudioTrack());
                MessageSender.sendMessage(event.getChannel(), MessageSender.LISTENING_TO + nextTrack.getAudioTrack().getInfo().title);
            }
            else
            {
                Timer.startTimer(Timer.MP_TIMER);
                audioPlayer.playTrack(null);
            }
        }
    }

    public void processSkipAllEvent(MessageReceivedEvent event)
    {
        if (audioPlayer.getPlayingTrack() == null)
            MessageSender.sendMessage(event.getChannel(), MessageSender.NOTHING_TO_SKIP);
        else
        {
            Timer.startTimer(Timer.MP_TIMER);
            audioQueue = new AudioQueue(new ArrayList<>());
            audioPlayer.playTrack(null);
        }
    }

    public void setAndConnectToChannel(MessageReceivedEvent event, String url)
    {
        if (event.getMember() != null)
        {
            if (event.getMember().getVoiceState() != null)
            {
                if (event.getMember().getVoiceState().getChannel() != null)
                {
                    VoiceChannel voiceChannel = event.getMember().getVoiceState().getChannel();
                    if ((guild == null && audioManager == null) || (guild != voiceChannel.getGuild()  && audioManager != guild.getAudioManager()))
                    {
                        textChannel = event.getChannel();
                        guild = voiceChannel.getGuild(); // канал для коннекта
                        audioManager = guild.getAudioManager(); // аудиоменеджер канала

                        AudioPlayerSendHandler handler = new AudioPlayerSendHandler(audioPlayer);
                        audioManager.setSendingHandler(handler); // установить отправляющий хэндлер
                        //connectToChannel(voiceChannel); // открыть коннекшен к голосовому каналу
                    }
                    AudioQueue.loadTrack(playerManager, audioQueue, audioPlayer, textChannel, url);
                    connectToChannel(voiceChannel);
                }
            }
        }
    }

    public static void connectToChannel(VoiceChannel voiceChannel)
    {
        if (currentVoiceChannel != voiceChannel)
        {
            currentVoiceChannel = voiceChannel;
            audioManager.openAudioConnection(currentVoiceChannel);
        }
    }

    public static void disconnectFromChannel()
    {
        if (currentVoiceChannel != null)
        {
            audioManager.closeAudioConnection();
            currentVoiceChannel = null;
        }
    }

    public static MPEventProcessor getInstance()
    {
        if (instance == null)
            instance = new MPEventProcessor();
        return instance;
    }

    public static AudioPlayer getAudioPlayer()
    {
        return audioPlayer;
    }

    public static AudioQueue getAudioQueue()
    {
        return audioQueue;
    }
}

//TODO Random Gachi
//TODO Поддержка нескольких каналов
