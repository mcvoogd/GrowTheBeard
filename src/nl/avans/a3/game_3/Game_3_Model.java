package nl.avans.a3.game_3;

import nl.avans.a3.mvc_interfaces.Model;

public class Game_3_Model implements Model{

    private Character[] characters = new Character[2];
    private Tree[] trees = new Tree[2];
    private final int START_X = 400;
    private boolean hitPlayer1, hitPlayer2;

    public void Game_3_Model(){


    }

    @Override
    public void start() {

        trees[0] = new Tree(0, 0, true);
        trees[1] = new Tree(1820, 0, false);
        characters[0] = new Character(1, START_X, 500);
        characters[1] = new Character(2, 1920 - START_X - 328, 500); //screenwidth - startPlayer - widthPlayer
    }

    @Override
    public void update() {
        for (int i = 0; i < trees.length; i++) {
            trees[i].update();
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

    public void damageTree(int tree){
        trees[tree].damageTree(100);
    }
}
