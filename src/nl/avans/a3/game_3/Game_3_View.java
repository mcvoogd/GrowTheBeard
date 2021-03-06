package nl.avans.a3.game_3;

import nl.avans.a3.event.ModelEvent;
import nl.avans.a3.mvc_interfaces.View;
import nl.avans.a3.util.Beard;
import nl.avans.a3.util.EasyTransformer;
import nl.avans.a3.util.ResourceHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Game_3_View implements View{

    private Game_3_Model gameModel;
    private BufferedImage banner;
    private BufferedImage winScreen;
    private BufferedImage[] winner;
    private BufferedImage winnerImage;
    private BufferedImage text;
    private BufferedImage preScreen;
    private final int WIDTH = 1920;
    private final int HEIGHT = 1080;
    private double textScale = 0.1;
    private static final double CHANGE_SPEED = 0.0005;
    private double change = CHANGE_SPEED;
    private static final double MAX_SCALE = 0.15;
    private static final double MIN_SCALE = 0.1;
    private BufferedImage[] instructions;
    private BufferedImage chosenImage;
    private boolean updateScore;
    private BufferedImage[] playerImage;
    private BufferedImage playerImages;
    public Game_3_View(Game_3_Model gameModel){
        this.gameModel = gameModel;
    }

    @Override
    public void start() {
        playerImage = new BufferedImage[2];
        instructions = new BufferedImage[3];
        winner = new BufferedImage[3];
        playerImages = ResourceHandler.getImage("res/images_scoreboard/person.png");
        chosenImage = ResourceHandler.getImage("res/images_game3/instructions.png");
        text = ResourceHandler.getImage("res/images_scoreboard/text.png");
        winnerImage = ResourceHandler.getImage("res/images_scoreboard/winner.png");
        banner = ResourceHandler.getImage("res/images_game1/banner.png");
        winScreen = ResourceHandler.getImage("res/images_scoreboard/background.png");

        for(int i = 0; i < 3; i++){
            winner[i] = winnerImage.getSubimage(0, (242 * i), winnerImage.getWidth(), 726/3);
            instructions[i] = chosenImage.getSubimage(0, (1080*i), 1920, 1080);
        }
        for(int i = 0; i < 2; i++)
        {
            playerImage[i] = playerImages.getSubimage(311*i, 0, 311, 577);
        }
   }

    @Override
    public void draw(Graphics2D g) {
        if(gameModel.getisPreScreen())
        {
            g.drawImage(gameModel.getBackground(), 0, 0, null);
            g.drawImage(banner, 0, -25, WIDTH, 180, null);
            g.drawImage(instructions[gameModel.getSwitchInstructionsCounter()], 0 , 0, null);
        }
        else if(gameModel.getInGame()) {
            g.drawImage(gameModel.getBackground(), 0, 0, null);
            gameModel.getBird().draw(g);
            Font tf = new Font("Verdana", Font.BOLD, 68);
            FontMetrics ft = g.getFontMetrics(tf);

            gameModel.getTrees()[0].draw(g);
            gameModel.getTrees()[1].draw(g);

            g.drawImage(banner, 0, -25, WIDTH, 180, null);
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
            }
            if (!gameModel.getFallenPerTree(1)) {
                for (DamageNumber i : gameModel.getTrees()[1].getDamageNumbers()) {
                    i.update();
                    i.draw(g);
                }
            }

            for (Particle p : gameModel.getParticles()) {
                p.draw(g);
            }
        }
        else {
            if(!updateScore){updateScore();}
            if (gameModel.getScorePlayer1() > gameModel.getScorePlayer2()) {
                drawGameEnd(g, 1);
            }else if (gameModel.getScorePlayer2() > gameModel.getScorePlayer1()) {
                drawGameEnd(g, 2);
            }else {
                drawGameEnd(g, 0);
            }
        }
    }

    private void drawGameEnd(Graphics2D g, int player) {

        g.drawImage(winScreen, 0, 0, WIDTH, HEIGHT, null);

        textScale += change;
        if(textScale > MAX_SCALE){
            change = -CHANGE_SPEED;
        }else if(textScale < MIN_SCALE){
            change = CHANGE_SPEED;
        }

        g.drawImage(text, EasyTransformer.scaleImageFromCenter(text, textScale, (WIDTH/2) - text.getWidth(null)/2, 200), null);

        switch(player)
        {
            case 0 :g.drawImage(winner[2], 500, 100, null); break; //TEKST
            case 1 :g.drawImage(winner[0], 500, 100, null); break; //TEKST
            case 2 :g.drawImage(winner[1], 500, 100, null); break; //TEKST
        }


        g.drawImage(playerImage[0],(WIDTH/2) - (1315/8) - 500, 300, 311, 577,  null);
        g.drawImage(playerImage[1], (WIDTH/2) - (1315/8) + 530, 300, 311, 577, null);

        int oldBeard1 = ((Beard.beardPlayer1 - 2) < 0) ? 0 : Beard.beardPlayer1 - 2;
        int oldBeard2 = ((Beard.beardPlayer2 - 2) < 0) ? 0 : Beard.beardPlayer2 - 2;
        switch (player){
            case 0:
                g.drawImage(gameModel.getBeards(Beard.beardPlayer1),(WIDTH/2) - (1315/8) - 500, 300,  311, 577, null);
                g.drawImage(gameModel.getBeards(Beard.beardPlayer2),(WIDTH/2) - (1315/8) + 530, 300,  311, 577, null);
                break;
            case 1:
                if(gameModel.getBeardCounter() < 15 && gameModel.getSwitchBeardCounter() < 3){
                    g.drawImage(gameModel.getBeards(oldBeard1),(WIDTH/2) - (1315/8) - 500, 300,  311, 577, null);
                }else if(gameModel.getBeardCounter() < 30 && gameModel.getSwitchBeardCounter() < 3){
                    g.drawImage(gameModel.getBeards(Beard.beardPlayer1),(WIDTH/2) - (1315/8) - 500, 300,  311, 577, null);
                }else{
                    g.drawImage(gameModel.getBeards(Beard.beardPlayer1),(WIDTH/2) - (1315/8) - 500, 300,  311, 577, null);
                    gameModel.setBeardCounter(0);
                    gameModel.setSwitchBeardCounter(gameModel.getSwitchBeardCounter() + 1);
                }
                g.drawImage(gameModel.getBeards(Beard.beardPlayer2),(WIDTH/2) - (1315/8) + 530, 300,  311, 577, null);
                break;
            case 2:
                if(gameModel.getBeardCounter() < 15 && gameModel.getSwitchBeardCounter() < 3){
                    g.drawImage(gameModel.getBeards(oldBeard2),(WIDTH/2) - (1315/8) + 530, 300,  311, 577, null);
                }else if(gameModel.getBeardCounter() < 30 && gameModel.getSwitchBeardCounter() < 3){
                    g.drawImage(gameModel.getBeards(Beard.beardPlayer2),(WIDTH/2) - (1315/8) + 530, 300, 311, 577, null);
                }else{
                    g.drawImage(gameModel.getBeards(Beard.beardPlayer2),(WIDTH/2) - (1315/8) + 530, 300, 311, 577, null);
                    gameModel.setBeardCounter(0);
                    gameModel.setSwitchBeardCounter(gameModel.getSwitchBeardCounter() + 1);
                }
                g.drawImage(gameModel.getBeards(Beard.beardPlayer1),(WIDTH/2) - (1315/8) - 500, 300, 311, 577, null);
                break;
        }
    }


    @Override
    public void close() {

    }

    @Override
    public void onModelEvent(ModelEvent event) {

    }

    public void updateScore(){
        if (gameModel.getScorePlayer1() > gameModel.getScorePlayer2()) {
            Beard.beardPlayer1 += 2;
            Beard.beardPlayer2 = ((Beard.beardPlayer2 - 1) < 0) ? 0 : Beard.beardPlayer2 - 1;
            if(Beard.beardPlayer2 > 5) Beard.beardPlayer2 = 5;
        }else if (gameModel.getScorePlayer2() > gameModel.getScorePlayer1()) {
            Beard.beardPlayer2 += 2;
            Beard.beardPlayer1 = ((Beard.beardPlayer1 - 1) < 0) ? 0 : Beard.beardPlayer1 - 1;
            if(Beard.beardPlayer1  > 5) Beard.beardPlayer1 = 5;
        } else if (gameModel.getScorePlayer1() == gameModel.getScorePlayer2()) {
            if(Beard.beardPlayer1 > 5) {Beard.beardPlayer1 = 5;}
            if(Beard.beardPlayer2 > 5) {Beard.beardPlayer2 = 5;}
        }
        updateScore = true;
    }
}
