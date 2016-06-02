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
    private int time = 5;
    private boolean ingame = true;
    private BufferedImage background;

    public int getScorePlayer1() {
        return scorePlayer1;
    }

    public int getScorePlayer2() {
        return scorePlayer2;
    }


    private int scorePlayer1, scorePlayer2;

    public Game_3_Model(){
        hitPlayer1 = true;
        hitPlayer2 = true;
        scorePlayer1 = 0;
        scorePlayer2 = 0;
        background = (BufferedImage) ResourceHandler.getImage("res/images_game3/background.png");

    }

    @Override
    public void start() {

        trees[0] = new Tree(0, 0, true);
        trees[1] = new Tree(1720, 0, false);
        characters[0] = new Character(1, START_X, 500);
        characters[1] = new Character(2, 1920 - START_X - 328, 500); //screenwidth - startPlayer - widthPlayer
        countDownTimer = new Timer(1000, e -> {time--; if(time == -1) ingame = false;} );
        countDownTimer.start();
    }

    @Override
    public void update() {
        for (Tree tree : trees) {
            tree.update();
        }
        if(!ingame)
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

    public void damageTree(int tree, int damage, int character){
        trees[tree].damageTree(damage);
        switch(character)
        {
            case 1 : scorePlayer1 += damage; break;
            case 2 : scorePlayer2 += damage; break;
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

    public boolean getIngame()
    {
        return ingame;
    }
    public BufferedImage getBackground()
    {
        return background;
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
}
