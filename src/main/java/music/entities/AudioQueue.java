package music.entities;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import util.MessageSender;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.List;

public class AudioQueue
{
    private List<Audio> queue;

    public AudioQueue(List<Audio> queue)
    {
        this.queue = queue;
    }

    public List<Audio> getQueue()
    {
        return queue;
    }

    public Audio getNext()
    {
        if (queue == null || queue.isEmpty())
            return null;

        if (queue.get(0).isPlayed())
            queue.remove(0);

        if (queue.isEmpty())
            return null;

        queue.get(0).setPlayed(true);
        return queue.get(0);
    }

    public static void loadTrack(AudioPlayerManager playerManager, AudioQueue audioQueue, AudioPlayer audioPlayer, MessageChannel textChannel, String url)
    {
        playerManager.loadItem(url, new AudioLoadResultHandler()
        {
            @Override
            public void trackLoaded(AudioTrack track)
            {
                audioQueue.getQueue().add(new Audio(track));
                if (audioPlayer.getPlayingTrack() == null)
                {
                    audioPlayer.playTrack(audioQueue.getNext().getAudioTrack());
                    MessageSender.sendMessage(textChannel, MessageSender.LISTENING_TO + track.getInfo().title);
                }
                else
                {
                    MessageSender.sendMessage(textChannel, MessageSender.ADDED_TO_QUEUE + track.getInfo().title);
                }
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist)
            {
                for (AudioTrack track : playlist.getTracks())
                {
                    audioQueue.getQueue().add(new Audio(track));
                }
                MessageSender.sendMessage(textChannel, MessageSender.ADDED_TO_QUEUE_PLAYLIST + playlist.getName());

                if (audioPlayer.getPlayingTrack() == null)
                {
                    audioPlayer.playTrack(audioQueue.getNext().getAudioTrack());
                }
            }

            @Override
            public void noMatches()
            {
                MessageSender.sendMessage(textChannel, MessageSender.TRACK_NOT_FOUND);
            }

            @Override
            public void loadFailed(FriendlyException throwable)
            {
                MessageSender.sendMessage(textChannel, MessageSender.TRACK_LOADING_ERROR);
            }
        });
    }

}
