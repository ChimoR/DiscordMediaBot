package music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import util.Timer;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class TrackScheduler extends AudioEventAdapter
{
    private final BlockingDeque<AudioTrack> queue = new LinkedBlockingDeque<>();

    public TrackScheduler(AudioPlayer player)
    {
    }

    @Override
    public void onPlayerPause(AudioPlayer player)
    {
        player.setPaused(true);
    }

    @Override
    public void onPlayerResume(AudioPlayer player)
    {
        player.setPaused(false);
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track)
    {
        Timer.stopTimer(Timer.MP_TIMER);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason)
    {
        if (endReason.mayStartNext)
        {
            Timer.startTimer(Timer.MP_TIMER);
            MPEventProcessor.getAudioPlayer().playTrack(MPEventProcessor.getAudioQueue().getNext().getAudioTrack());
        }

        // endReason == FINISHED: A track finished or died by an exception (mayStartNext = true).
        // endReason == LOAD_FAILED: Loading of a track failed (mayStartNext = true).
        // endReason == STOPPED: The player was stopped.
        // endReason == REPLACED: Another track started playing while this had not finished
        // endReason == CLEANUP: Player hasn't been queried for a while, if you want you can put a
        //                       clone of this back to your queue
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception)
    {
        exception.printStackTrace();
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs)
    {
        // Audio track has been unable to provide us any audio, might want to just start a new track
    }
}
