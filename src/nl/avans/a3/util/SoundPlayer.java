package nl.avans.a3.util;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SoundPlayer
{
    private Clip clip;
    private Timer t = null;
    private int time = 100;
    private boolean isplaying = false;
    private Clip selectedClip;
    private ArrayList<Clip> clips = new ArrayList<>();

    public SoundPlayer(String path)
    {
        try
        {
            t = new Timer(time, e -> {
                if (isplaying) {
                    stop();
                    resetClip();
                    t.restart();
                }
            });
            AudioInputStream sound = AudioSystem.getAudioInputStream(new File(path));
            clip = AudioSystem.getClip();
            clip.open(sound);
            selectedClip = clip;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * constructor for different clips.
     */
    public SoundPlayer(ArrayList<String> clips)
    {
        for(String s : clips)
        {
            try {
                AudioInputStream sound = AudioSystem.getAudioInputStream(new File(s));
                clip = AudioSystem.getClip();
                clip.open(sound);

                this.clips.add(clip);
            } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void start()
    {
        selectedClip.setFramePosition(0);
        selectedClip.start();
        isplaying = true;
    }

    public void stop()
    {
        selectedClip.stop();
        isplaying = false;
    }

    public void resetClip()
    {
        selectedClip.setFramePosition(0);
    }

    public void getRandomClip()
    {
        int clipID = (int) (Math.random()*clips.size());
        if(clips.get(clipID) != null) {
            selectedClip = clips.get(clipID);
        }
    }

    public void loop(float volumeReduction)
    {
        FloatControl gainControl =
                (FloatControl) selectedClip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(-volumeReduction); // Reduce volume by 10 decibels.
        selectedClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public boolean isPlaying()
    {
        return isplaying;
    }

    /**
     * Method to play a song for a certain ammount of time.
     * @param time : time to play the song for in milliseconds.
     */
    public void playRandomOnce(int time)
    {
        getRandomClip();
        start();
        resetClip();
    }

    public void playOnce()
    {
        start();
    }
}
