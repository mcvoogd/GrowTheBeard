package nl.avans.a3.util;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class SoundPlayer
{
    private Clip clip;

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
    }

    public void stop()
    {
        clip.stop();
    }

    public void loop()
    {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
}
