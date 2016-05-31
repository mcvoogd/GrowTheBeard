package MVC_V2;

import Game_01_Objects.*;

import java.awt.*;

public class WoodDodgingModel implements Model{


    private Image woodBlock, woodBlock2, woodBlock3;
    private Image player_1_Image;
    private Image player_2_Image;
    private Image game1Background, banner;
    private Image game1Winscreen;
    private Image particle;

    private Player player1;
    private Player player2;

    @Override
    public void start() {
        woodBlock = ResourceHandler.getImage("images_game1\\wood1");
        woodBlock2 = ResourceHandler.getImage("images_game1\\wood2");
        woodBlock3 = ResourceHandler.getImage("images_game1\\wood3");
        player1 = ResourceHandler.getImage("images_game1\\person1");
        player2 = ResourceHandler.getImage("images_game1\\person2");
        game1Background = ResourceHandler.getImage("images_game1\\background2");
        particle = ResourceHandler.getImage("images_game1\\particle");
        game1Winscreen = ResourceHandler.getImage("images_game1\\winscreen");
        banner = ResourceHandler.getImage("images_game1\\banner");
    }

    @Override
    public void update() {

    }

    @Override
    public void close() {

        ResourceHandler.unloadImage("images_game1\\wood1");
        ResourceHandler.unloadImage("images_game1\\wood2");
        ResourceHandler.unloadImage("images_game1\\wood3");
        ResourceHandler.unloadImage("images_game1\\person1");
        ResourceHandler.unloadImage("images_game1\\person2");
        ResourceHandler.unloadImage("images_game1\\background2");
        ResourceHandler.unloadImage("images_game1\\particle");
        ResourceHandler.unloadImage("images_game1\\winscreen");
        ResourceHandler.unloadImage("images_game1\\banner");
    }

    public void onRumbleEvent()
    {

    }
}

