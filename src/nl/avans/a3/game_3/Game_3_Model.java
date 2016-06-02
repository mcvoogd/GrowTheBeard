package nl.avans.a3.game_3;

import nl.avans.a3.mvc_interfaces.Model;
import nl.avans.a3.util.ResourceHandler;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class Game_3_Model implements Model{

    private Character[] characters = new Character[2];
    private Tree[] trees = new Tree[2];
    private final int START_X = 400;
    private boolean hitPlayer1, hitPlayer2;
    private Timer countDownTimer;
    private int time = 30;
    private boolean ingame;
    private BufferedImage background;

    public Game_3_Model(){
        hitPlayer1 = true;
        hitPlayer2 = true;
        background = (BufferedImage) ResourceHandler.getImage("res/images_game3/background.png");

    }

    @Override
    public void start() {

        trees[0] = new Tree(0, 0, true);
        trees[1] = new Tree(1820, 0, false);
        characters[0] = new Character(1, START_X, 500);
        characters[1] = new Character(2, 1920 - START_X - 328, 500); //screenwidth - startPlayer - widthPlayer
        countDownTimer = new Timer(1000, e -> time--);
        countDownTimer.start();
    }

    @Override
    public void update() {
        for (int i = 0; i < trees.length; i++) {
            trees[i].update();
        }
        if(time == 0)
        {
            countDownTimer.stop();
        }

    }

    @Override
    public void close() {

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
            case 1 : hitPlayer1 = trueOrFalse; break;
            case 2 : hitPlayer2 = trueOrFalse; break;
        }
    }

    public Character[] getPlayers(){
        return characters;
    }

    public Tree[] getTrees(){
        return trees;
    }

    public void damageTree(int tree, int damage){
        trees[tree].damageTree(damage);
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

    public BufferedImage getBackground()
    {
        return background;
    }
}
