package nl.avans.a3.game_3;

import nl.avans.a3.mvc_interfaces.Model;

public class Game_3_Model implements Model{

    private Character[] characters = new Character[2];
    private Tree[] trees = new Tree[2];
    private final int START_X = 400;

    public void Game_3_Model(){


    }

    @Override
    public void start() {
        characters[0] = new Character(1, START_X, 500);
        characters[1] = new Character(2, 1920 - START_X - 328, 500); //screenwidth - startPlayer - widthPlayer
        trees[0] = new Tree(0, 0);
        trees[1] = new Tree(1820, 0);
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
