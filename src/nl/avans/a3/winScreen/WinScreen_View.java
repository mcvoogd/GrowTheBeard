package nl.avans.a3.winScreen;


import nl.avans.a3.event.ModelEvent;
import nl.avans.a3.mvc_interfaces.View;
import nl.avans.a3.util.Beard;
import nl.avans.a3.util.EasyTransformer;
import nl.avans.a3.util.ResourceHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class WinScreen_View implements View{

    private WinScreen_Model model;
    private BufferedImage text;
    private BufferedImage[] players = new BufferedImage[2];
    private BufferedImage[] beard = new BufferedImage[2];
    private BufferedImage backGround, treeBackground;
    private BufferedImage congratz;
    private final int WIDTH = 1920;
    private final int HEIGHT = 1080;
    private double textScale = 0.1;
    private static final double MAX_SCALE = 0.15;
    private static final double MIN_SCALE = 0.1;
    private static final double CHANGE_SPEED = 0.0005;
    private double change = CHANGE_SPEED;

    public WinScreen_View(WinScreen_Model model){
        this.model = model;
        BufferedImage image = ResourceHandler.getImage("res/images_scoreboard/person.png");
        for (int i = 0; i < 2; i++) {
            players[i] = image.getSubimage((image.getWidth()/2) * i, 0, image.getWidth()/2, image.getHeight());
        }
        BufferedImage backgroundEnd = ResourceHandler.getImage("res/images_scoreboard/background_end.png");
        backGround = backgroundEnd.getSubimage(0, 0, 1920, 1080);
        treeBackground = backgroundEnd.getSubimage(0, 1080, 1920, 1080);
        BufferedImage image2 = ResourceHandler.getImage("res/images_scoreboard/congraz.png");
        if(Beard.beardPlayer1 > Beard.beardPlayer2){
            congratz = image2.getSubimage(0, 0, image2.getWidth(), image2.getHeight()/2);
        }else{
            congratz = image2.getSubimage(0, (image2.getHeight()/2), image2.getWidth(), image2.getHeight()/2);
        }
        text = ResourceHandler.getImage("res/images_scoreboard/text.png");
        BufferedImage beardImage = ResourceHandler.getImage("res/images_scoreboard/beard_sprite.png");
        beard[0] = beardImage.getSubimage((beardImage.getWidth()/6) * Beard.beardPlayer1, 0, beardImage.getWidth()/6, beardImage.getHeight());
        beard[1] = beardImage.getSubimage((beardImage.getWidth()/6) * Beard.beardPlayer2, 0, beardImage.getWidth()/6, beardImage.getHeight());
    }

    @Override
    public void start() {

    }

    @Override
    public void draw(Graphics2D g) {
        g.drawImage(backGround, 0, 0, null);
        textScale += change;
        if(textScale > MAX_SCALE){
            change = -CHANGE_SPEED;
        }else if(textScale < MIN_SCALE){
            change = CHANGE_SPEED;
        }
        model.getConfettiCanon().draw(g);
        g.drawImage(treeBackground, 0, 0, null);
        g.drawImage(congratz, 20, 100, null);
        g.drawImage(text, EasyTransformer.scaleImageFromCenter(text, textScale, (WIDTH/2) - text.getWidth(null)/2, 200), null);

        g.drawImage(players[0],(WIDTH/2) - (1315/8) - 500, 310, null);
        g.drawImage(beard[0],(WIDTH/2) - (1315/8) - 500, 310, null);

        g.drawImage(players[1], (WIDTH/2) - (1315/8) + 530, 320, null);
        g.drawImage(beard[1], (WIDTH/2) - (1315/8) + 530, 320, null);

    }

    @Override
    public void close() {

    }

    @Override
    public void onModelEvent(ModelEvent event) {

    }
}
