package nl.avans.a3.util;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class SoundPlayer{

    private Clip clip;
    private Timer t = null;
    private boolean isPlaying = false;
    private Clip selectedClip;
    private Clip[] clips;

    public SoundPlayer(String path){
        try{
            int time = 100;
            t = new Timer(time, e -> {
                if (isPlaying) {
                    stop();
                    resetClip();
                    t.restart();
                }
            });
            AudioInputStream sound = AudioSystem.getAudioInputStream(new File(path));
            clip = AudioSystem.getClip();
            clip.open(sound);
            selectedClip = clip;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Constructor for multiple clips.
     */
    public SoundPlayer(String[] path){
        clips = new Clip[path.length];
        for(int i = 0; i < path.length; i++){
            try{
                AudioInputStream sound = AudioSystem.getAudioInputStream(new File(path[i]));
                clip = AudioSystem.getClip();
                clip.open(sound);
                clips[i] = clip;
            }catch(UnsupportedAudioFileException | IOException | LineUnavailableException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 
     */
    public void start(){
        selectedClip.setFramePosition(0);
        selectedClip.start();
        isPlaying = true;
    }

    /**
     * 
     */
    public void stop(){
        if (isPlaying) selectedClip.stop();
        isPlaying = false;
    }

    /**
     * 
     */
    public void resetClip(){
        selectedClip.setFramePosition(0);
    }

    /**
     * 
     */
    public void getRandomClip(){
        int clipID = (int) (Math.random()*clips.length);
        if(clips[clipID] != null) {
            selectedClip = clips[clipID];
        }
    }

    /**
     * @param volumeReduction
     */
    public void loop(float volumeReduction){
        FloatControl gainControl = (FloatControl) selectedClip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(-volumeReduction); // Reduce volume by parameter decibels.
        selectedClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * @return
     */
    public boolean isPlaying(){
        return isPlaying;
    }

    /**
     * Play a random sound 
     */
    public void playRandomOnce(){
        getRandomClip();
        start();
        resetClip();
    }

    /**
     * 
     */
    public void playOnce(){
        start();
    }
}
