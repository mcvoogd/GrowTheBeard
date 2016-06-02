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

        gameModel.getTrees()[0].draw(g);
        gameModel.getTrees()[1].draw(g);
        gameModel.getPlayers()[0].draw(g);
        gameModel.getPlayers()[1].draw(g);

        if(!gameModel.getFallenPerTree(0)) {
            for (DamageNumber i : gameModel.getTrees()[0].getDamageNumbers()) {
                i.update();
                i.draw(g);
            }
            if(gameModel.getHitPlayer(1))
            {
                g.fillOval(400, 300, 50, 50);
            }
        }
        if(!gameModel.getFallenPerTree(1)) {
            for (DamageNumber i : gameModel.getTrees()[1].getDamageNumbers()) {
                i.update();
                i.draw(g);
            }
            if(gameModel.getHitPlayer(2)) {
                g.fillOval(1920 - 400, 300, 50, 50);
            }
        }
    }

    @Override
    public void close() {

    }

    @Override
    public void onModelEvent(ModelEvent event) {

    }
}
