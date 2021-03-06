package nl.avans.a3.game_3;

import nl.avans.a3.main_menu.MainMenuModel;
import nl.avans.a3.mvc_interfaces.Model;
import nl.avans.a3.util.ResourceHandler;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Game_3_Model implements Model{

    private Character[] characters = new Character[2];
    private Tree[] trees = new Tree[2];
    private Bird bird;
    private final int START_X = 350;
    private boolean hitPlayer1, hitPlayer2;
    private Timer countDownTimer;
    private int time = 30;
    private boolean inGame = false;
    private BufferedImage background;
    private ArrayList<Particle> particles;
    private Random rand = new Random();
    private int switchInstructionsCounter = 0;
    private Timer switchInstructionsTimer;
    private BufferedImage[] beards = new BufferedImage[6];

    private boolean preScreen = true;

    private int beardCounter, switchBeardCounter;

    public int getScorePlayer1() {
        return scorePlayer1;
    }

    public int getScorePlayer2() {
        return scorePlayer2;
    }


    private int scorePlayer1, scorePlayer2;

    public Game_3_Model(){
        switchInstructionsTimer = new Timer(1000, e -> {
            switch(switchInstructionsCounter)
            {
                case 0 : switchInstructionsCounter = 1; break;
                case 1 : switchInstructionsCounter = 2; break;
                case 2 : switchInstructionsCounter = 0; break;
            }
        });
        switchInstructionsTimer.start();
        hitPlayer1 = true;
        hitPlayer2 = true;
        scorePlayer1 = 0;
        scorePlayer2 = 0;
        background = ResourceHandler.getImage("res/images_game3/background.png");
        BufferedImage imageBeard = ResourceHandler.getImage("res/images_scoreboard/beard_sprite.png");
        for (int i = 0; i < 6; i++) {
            beards[i] = imageBeard.getSubimage(311 * i, 0, 311, 577);
        }
     }

    // TODO magic values everywhere

    @Override
    public void start() {
        particles = new ArrayList<>();
        bird = new Bird();
        trees[0] = new Tree(0, 0, true);
        trees[1] = new Tree(1720, 0, false);
        characters[0] = new Character(1, START_X, 480);
        characters[1] = new Character(2, 1920 - START_X - 328, 480); //screenwidth - startPlayer - widthPlayer
        countDownTimer = new Timer(1000, e -> {time--; if(time == -1) inGame = false;} );

    }

    @Override
    public void update() {

        if(inGame) {
            for (Tree tree : trees) {
                tree.update();
            }
            Iterator it = particles.iterator();
            while (it.hasNext()) {
                Particle p = (Particle) it.next();
                p.update();
                if (p.getLife() > 10) {
                    it.remove();
                }
            }
            characters[0].update();
            characters[1].update();
            bird.update();
            if (bird.getWait()) {
                bird.setWait(rand.nextInt(300));
            }
        }else  if (!inGame) {
            countDownTimer.stop();
            beardCounter++;
        }




    }

    @Override
    public void close() {

    }

    public int getSwitchInstructionsCounter()
    {
        return switchInstructionsCounter;
    }

    public boolean getisPreScreen()
    {
        return preScreen;
    }

    public void setPreScreen(boolean preScreen)
    {
        this.preScreen = preScreen;
        if(!preScreen)
        {
            inGame = true;
            countDownTimer.start();
            switchInstructionsTimer.stop();
        }
    }

    public boolean getHitPlayer(int player)
    {
        switch(player)
        {
            case 1 : return hitPlayer1;
            case 2 : return hitPlayer2;
        }
        return false;
    }
    public void setHitPlayer(int player, boolean trueOrFalse)
    {
        switch(player)
        {
            case 1 : hitPlayer1 = trueOrFalse; characters[0].setImage(0);break;
            case 2 : hitPlayer2 = trueOrFalse; characters[1].setImage(0);break;
        }
    }

    public Character[] getPlayers(){
        return characters;
    }

    public Tree[] getTrees(){
        return trees;
    }

    // TODO change character's type to an enum

    public void damageTree(int tree, int damage, int character){
        trees[tree].damageTree(damage);
        switch(character)
        {
            case 1 : scorePlayer1 += damage; break;
            case 2 : scorePlayer2 += damage; break;
        }
        switch (tree){
            case 0 : for(int i = 0; i < 10; i++){
                particles.add(new Particle( 100, 780, i*36));
            } break;
            case 1:
                for(int i = 0; i < 10; i++){
                    particles.add(new Particle(1920 - 100, 780, i*36));
                } break;
        }
    }

    public boolean getFallenPerTree(int tree)
    {
        switch(tree)
        {
            case 0 : return trees[tree].getIsFallen();
            case 1 : return trees[tree].getIsFallen();
        }
        return false;
    }

    public int getTime()
    {
        return time;
    }

    public boolean getInGame()
    {
        return inGame;
    }
    public BufferedImage getBackground()
    {
        return background;
    }

    public void startHit(int player){
        switch (player){
            case 1: characters[0].setChop(true); break;
            case 2: characters[1].setChop(true); break;
        }
    }

    public BufferedImage getPlayerImage(int player)
    {
        switch (player)
        {
            case 1 :  return characters[0].getPlayerImage();
            case 2 :  return characters[1].getPlayerImage();

        }
        return null;
    }

    public ArrayList<Particle> getParticles(){
        return particles;
    }

    public Bird getBird(){
        return bird;
    }

    public BufferedImage getBeards(int beardNumber){
        if(beardNumber >= 6) beardNumber = 5;
        return beards[beardNumber]; }

    public int getBeardCounter() {return beardCounter;}

    public void setBeardCounter(int beardCounter) {this.beardCounter = beardCounter;}

    public int getSwitchBeardCounter() {return switchBeardCounter;}

    public void setSwitchBeardCounter(int switchBeardCounter) {this.switchBeardCounter = switchBeardCounter;}
}
