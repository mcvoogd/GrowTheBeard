package nl.avans.a3.util;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.File;

public class SoundPlayer
{
    private Clip clip;
    private boolean isplaying = false;

    public SoundPlayer(String URL)
    {
        try
        {
            AudioInputStream sound = AudioSystem.getAudioInputStream(new File(URL));
            clip = AudioSystem.getClip();
            clip.open(sound);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void start()
    {
        clip.start();
        isplaying = true;
    }

    public void stop()
    {
        clip.stop();
        isplaying = false;
    }

    public void loop()
    {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public boolean isPlaying()
    {
        return isplaying;
    }

    /**
     * Method to play a song for a certain ammount of time.
     * @param time : time to play the song for in milliseconds.
     */
    public void playOnce(int time)
    {
        start();
        Timer t = new Timer(time, e -> {if(isplaying) stop();});
        t.start();
    }
}
