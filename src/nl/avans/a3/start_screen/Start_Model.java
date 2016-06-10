package nl.avans.a3.start_screen;

import nl.avans.a3.mvc_interfaces.Model;

public class Start_Model implements Model {

    public enum ChosenGame {
        GAME1, GAME2, GAME3, BOOT, FINAL
    }

    private ChosenGame chosenGame = ChosenGame.BOOT;

    @Override
    public void start() {

    }

    @Override
    public void update() {

    }

    @Override
    public void close() {

    }

    public ChosenGame getChosenGame()
    {
        return chosenGame;
    }

    public void setChosenGame(ChosenGame arg)
    {
        this.chosenGame = arg;
    }

    public void notifyNextGame()
    {
        switch (getChosenGame())
        {

            case GAME1:setChosenGame(ChosenGame.GAME2);
                break;
            case GAME2:setChosenGame(ChosenGame.GAME3);
                break;
            case GAME3:setChosenGame(ChosenGame.FINAL);
                break;
            case BOOT: setChosenGame(ChosenGame.GAME1);
                break;
        }
    }
}
