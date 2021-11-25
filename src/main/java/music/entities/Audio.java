package music.entities;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class Audio
{
    private AudioTrack audioTrack;
    private boolean isPlayed;

    public Audio(AudioTrack audioTrack)
    {
        this.audioTrack = audioTrack;
        this.isPlayed = false;
    }

    public boolean isPlayed()
    {
        return isPlayed;
    }

    public void setPlayed(boolean played)
    {
        isPlayed = played;
    }

    public AudioTrack getAudioTrack()
    {
        return audioTrack;
    }

    public void setAudioTrack(AudioTrack audioTrack)
    {
        this.audioTrack = audioTrack;
    }

}
