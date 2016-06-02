package nl.avans.a3.game_3;

import nl.avans.a3.event.ModelEvent;
import nl.avans.a3.mvc_interfaces.View;

import java.awt.*;

public class Game_3_View implements View{

    Game_3_Model gameModel;

    public Game_3_View(Game_3_Model gameModel){
        this.gameModel = gameModel;
    }

    @Override
    public void start() {

    }

    @Override
    public void draw(Graphics2D g) {
        gameModel.getPlayers()[0].draw(g);
        gameModel.getPlayers()[1].draw(g);
        gameModel.getTrees()[0].draw(g);
        gameModel.getTrees()[1].draw(g);
    }

    @Override
    public void close() {

    }

    @Override
    public void onModelEvent(ModelEvent event) {

    }
}
