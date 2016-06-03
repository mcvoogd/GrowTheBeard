package nl.avans.a3.game_3;

import nl.avans.a3.event.ModelEvent;
import nl.avans.a3.game_1.Util.Images;
import nl.avans.a3.mvc_interfaces.View;
import nl.avans.a3.util.ResourceHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Game_3_View implements View{

    Game_3_Model gameModel;
    private BufferedImage banner;
    private BufferedImage winscreen;
    private BufferedImage[] winner;
    private BufferedImage winnerImage;
    public Game_3_View(Game_3_Model gameModel){
        this.gameModel = gameModel;
    }

    @Override
    public void start() {
        winner = new BufferedImage[2];
        winnerImage = (BufferedImage) ResourceHandler.getImage("res/images_scoreboard/winner.png");
        banner = (BufferedImage)  ResourceHandler.getImage("res/images_game1/banner.png");
        winscreen = (BufferedImage) ResourceHandler.getImage("res/images_scoreboard/background.png");

        for(int i = 0; i < 2; i++){
            winner[i] = winnerImage.getSubimage(0, (winnerImage.getHeight()/2 * i), winnerImage.getWidth(), winnerImage.getHeight()/2);
        }

   }

    @Override
    public void draw(Graphics2D g) {
        if(gameModel.getIngame()) {
            g.drawImage(gameModel.getBackground(), 0, 0, null);
            Font tf = new Font("Verdana", Font.BOLD, 68);
            FontMetrics ft = g.getFontMetrics(tf);

            gameModel.getTrees()[0].draw(g);
            gameModel.getTrees()[1].draw(g);

            g.drawImage(banner, 0, -25, 1920, 180, null);
            g.setFont(tf);
            g.setColor(new Color(159, 44, 22));
            g.drawString("" + gameModel.getTime(), 960 - (ft.stringWidth("" + gameModel.getTime()) / 2) + 90, 80);

            gameModel.getPlayers()[0].draw(g);
            gameModel.getPlayers()[1].draw(g);

            if (!gameModel.getFallenPerTree(0)) {
                for (DamageNumber i : gameModel.getTrees()[0].getDamageNumbers()) {
                    i.update();
                    i.draw(g);
                }
                if (gameModel.getHitPlayer(1)) {
                    g.fillOval(400, 300, 50, 50);
                }
            }
            if (!gameModel.getFallenPerTree(1)) {
                for (DamageNumber i : gameModel.getTrees()[1].getDamageNumbers()) {
                    i.update();
                    i.draw(g);
                }
                if (gameModel.getHitPlayer(2)) {
                    g.fillOval(1920 - 400, 300, 50, 50);
                }
            }
        }
        else {
            if (gameModel.getScorePlayer1() > gameModel.getScorePlayer2()) {
                drawGameEnd(g, 1);
            }else if (gameModel.getScorePlayer2() > gameModel.getScorePlayer1()) {
                drawGameEnd(g, 2);
            }else
            {
                drawGameEnd(g, 0);
            }
        }

        for (Particle p : gameModel.getParticles()) {
            p.draw(g);
        }
    }

    private void drawGameEnd(Graphics2D g, int player) {

        g.drawImage(winscreen, 0, 0, 1920, 1080, null);
        Font font = new Font("Sansserif", Font.BOLD, 360);
        FontMetrics fm = g.getFontMetrics(font);
        g.setFont(font);
        String s = "DRAW";
        switch(player)
        {
            case 0 : g.setColor(Color.BLACK);
                g.drawString(s, ((1920/2) - (fm.stringWidth(s) / 2)), 300);
                break; //default
            case 1 :
                g.drawImage(winner[0], 500, 100, null); break; //TEKST
            case 2 :
                g.drawImage(winner[1], 500, 100, null); break; //TEKST

        }

        g.drawImage(gameModel.getPlayerImage(1),(1920/2) - (1315/8) - 500, 450, 1315/4, 1922/4,  null);
        g.drawImage(gameModel.getPlayerImage(2), (1920/2) - (1315/8) + 530, 450, 1315/4, 1922/4, null);


    }


    @Override
    public void close() {

    }

    @Override
    public void onModelEvent(ModelEvent event) {

    }
}
