package nl.avans.a3.game_3;

import nl.avans.a3.event.ModelEvent;
import nl.avans.a3.mvc_interfaces.View;
import nl.avans.a3.util.ResourceHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Game_3_View implements View{

    Game_3_Model gameModel;
    private BufferedImage banner;
    public Game_3_View(Game_3_Model gameModel){
        this.gameModel = gameModel;
    }

    @Override
    public void start() {
        banner =(BufferedImage) ResourceHandler.getImage("res/images_game1/banner.png");
   }

    @Override
    public void draw(Graphics2D g) {

        Font tf = new Font("Verdana", Font.BOLD, 68);
        FontMetrics ft = g.getFontMetrics(tf);

        g.drawImage(banner, 0, 930, 1920, 180,  null);
        g.setFont(tf);
        g.setColor(new Color(159, 44, 22));
        g.drawString("" + gameModel.getTime(), 960 - (ft.stringWidth("" + gameModel.getTime())/2) + 90, 1040);

        gameModel.getTrees()[0].draw(g);
        gameModel.getTrees()[1].draw(g);
        gameModel.getPlayers()[0].draw(g);
        gameModel.getPlayers()[1].draw(g);

        for(DamageNumber i : gameModel.getTrees()[0].getDamageNumbers())
        {
            i.update();
            i.draw(g);
        }for(DamageNumber i : gameModel.getTrees()[1].getDamageNumbers())
        {
            i.update();
            i.draw(g);
        }
        if(gameModel.getHitPlayer(1))
        {
            g.drawOval(300, 1000, 50, 50);
        }
        if(gameModel.getHitPlayer(2))
        {
            g.drawOval(600, 1000, 50, 50);
        }

    }

    @Override
    public void close() {

    }

    @Override
    public void onModelEvent(ModelEvent event) {

    }
}
