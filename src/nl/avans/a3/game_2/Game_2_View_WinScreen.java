package nl.avans.a3.game_2;

import javafx.util.Pair;
import nl.avans.a3.event.ModelEvent;
import nl.avans.a3.mvc_interfaces.View;
import nl.avans.a3.util.EasyTransformer;
import nl.avans.a3.util.ResourceHandler;

import nl.avans.a3.util.Beard;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by flori on 19-Jun-16.
 */
public class Game_2_View_WinScreen implements View {
    Game_2_Model model;

    private final int WIDTH = 1920;
    private final int HEIGHT = 1080;
    private double textScale = 0.1;
    private static double CHANGE_SPEED = 0.0005;
    private double change = CHANGE_SPEED;
    private static final double MAX_SCALE = 0.15;
    private static final double MIN_SCALE = 0.1;
    private BufferedImage text;
    private BufferedImage[] winner;
    private BufferedImage winScreen;
    private BufferedImage[] playerImage;
    private BufferedImage[] beards;
    private int beardCounter, switchBeardCounter;

    public Game_2_View_WinScreen(Game_2_Model model)
    {
        this.model = model;
    }

    @Override
    public void start() {
        winner = new BufferedImage[3];
        text = ResourceHandler.getImage("res/images_scoreboard/text.png");
        BufferedImage winnerImage = ResourceHandler.getImage("res/images_scoreboard/winner.png");
        winScreen = ResourceHandler.getImage("res/images_scoreboard/background.png");
        playerImage = new BufferedImage[2];
        BufferedImage playerImages = ResourceHandler.getImage("res/images_scoreboard/person.png");
        for(int i = 0; i < 2; i++)
        {
            playerImage[i] = playerImages != null ? playerImages.getSubimage(311 * i, 0, 311, 577) : null;
        }
        for(int i = 0; i < 3; i++){
            winner[i] = winnerImage.getSubimage(0, (242 * i), winnerImage.getWidth(), 726/3);
        }
        beards = new BufferedImage[6];
        BufferedImage imageBeard = ResourceHandler.getImage("res/images_scoreboard/beard_sprite.png");
        for (int i = 0; i < 6; i++) {
            beards[i] = imageBeard != null ? imageBeard.getSubimage(311 * i, 0, 311, 577) : null;
        }
    }

    private int scoreToPlayer(Pair<Integer, Integer> score)
    {
        if (score.getKey() > score.getValue()) return 1;
        if (score.getKey() < score.getValue()) return 2;
        return 0;
    }

    @Override
    public void draw(Graphics2D g) {
        g.drawImage(winScreen, 0, 0, WIDTH, HEIGHT, null);
        textScale += change;
        if(textScale > MAX_SCALE){
            change = -CHANGE_SPEED;
        }else if(textScale < MIN_SCALE){
            change = CHANGE_SPEED;
        }
        beardCounter++;
        g.drawImage(text, EasyTransformer.scaleImageFromCenter(text, textScale, (WIDTH/2) - text.getWidth(null)/2, 200), null);

        g.drawImage(winner[scoreToPlayer(model.getScores())], 500, 100 ,null);

        g.drawImage(playerImage[0],(WIDTH/2) - (1315/8) - 500, 300, 311, 577,  null);
        g.drawImage(playerImage[1], (WIDTH/2) - (1315/8) + 530, 300, 311, 577, null);

        int oldBeard1 = ((Beard.beardPlayer1 - 2) < 0) ? 0 : Beard.beardPlayer1 - 2;
        int oldBeard2 = ((Beard.beardPlayer2 - 2) < 0) ? 0 : Beard.beardPlayer2 - 2;
        switch (scoreToPlayer(model.getScores())){
            case 0:
                g.drawImage(beards[Beard.beardPlayer1],(WIDTH/2) - (1315/8) - 500, 300,  311, 577, null);
                g.drawImage(beards[Beard.beardPlayer2],(WIDTH/2) - (1315/8) + 530, 300,  311, 577, null);
                break;
            case 1:
                if(beardCounter< 15 && switchBeardCounter < 3){
                    g.drawImage(beards[oldBeard1],(WIDTH/2) - (1315/8) - 500, 300,  311, 577, null);
                }else if(beardCounter < 30 &&switchBeardCounter < 3){
                    g.drawImage(beards[Beard.beardPlayer1],(WIDTH/2) - (1315/8) - 500, 300,  311, 577, null);
                }else{
                    g.drawImage(beards[Beard.beardPlayer1],(WIDTH/2) - (1315/8) - 500, 300,  311, 577, null);
                    beardCounter = 0;
                    ++switchBeardCounter;
                }
                g.drawImage(beards[Beard.beardPlayer2],(WIDTH/2) - (1315/8) + 530, 300,  311, 577, null);
                break;
            case 2:
                if(beardCounter < 15 && switchBeardCounter < 3){
                    g.drawImage(beards[oldBeard2],(WIDTH/2) - (1315/8) + 530, 300,  311, 577, null);
                }else if(beardCounter < 30 && switchBeardCounter < 3){
                    g.drawImage(beards[Beard.beardPlayer2],(WIDTH/2) - (1315/8) + 530, 300, 311, 577, null);
                }else{
                    g.drawImage(beards[Beard.beardPlayer2],(WIDTH/2) - (1315/8) + 530, 300, 311, 577, null);
                    beardCounter = 0;
                    ++switchBeardCounter;
                }
                g.drawImage(beards[Beard.beardPlayer1],(WIDTH/2) - (1315/8) - 500, 300, 311, 577, null);
                break;
        }
    }

    @Override
    public void close() {

    }

    @Override
    public void onModelEvent(ModelEvent event) {

    }
}
