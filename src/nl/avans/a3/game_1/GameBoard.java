package nl.avans.a3.game_1;
import nl.avans.a3.event.NewModel;
import nl.avans.a3.main_menu.MainMenuModel;
import nl.avans.a3.mvc_handlers.ModelHandler;
import nl.avans.a3.party_mode_handler.PartyModeHandler;
import nl.avans.a3.util.Beard;
import nl.avans.a3.util.EasyTransformer;
import nl.avans.a3.util.ResourceHandler;
import nl.avans.a3.util.WiimoteHandler;
import nl.avans.a3.game_1.Util.Images;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GameBoard extends JPanel implements ActionListener {

	private Timer gameLogicTimer;
	private Timer endTimer;
	private Timer timeLeft;

	private int time = 30;
	private int scorePlayer1;
	private int scorePlayer2;

	private Player player1;
	private Player player2;
	private ArrayList<WoodBlock> woodBlocks;

	private boolean inGame;

	private boolean blockIsFallen;

	private final int SCREEN_WIDTH = 1920;
	private final int SCREEN_HEIGHT = 1080;

	private final int START_X_PLAYER1 = 640;
	private final int START_X_PLAYER2 = 1280;
	private final int PLAYER_Y = -100;

	private BufferedImage text;
    private double textScale = 0.1;
    private static final double CHANGE_SPEED = 0.005;
    private double change = CHANGE_SPEED;
    private static final double MAX_SCALE = 0.15;
    private static final double MIN_SCALE = 0.1;

    private BufferedImage winScreen;
    private BufferedImage[] winner;
    private BufferedImage winnerImage;

	private WiimoteHandler wiimoteHandler;
	private Random rand = new Random();
	private BufferedImage[] woodImages = new BufferedImage[3];
    private BufferedImage[] instructions;
    private BufferedImage chosenImage;
    private BufferedImage background = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB);

    private Timer switchInstructionsTimer;
    private int switchInstructionsCounter = 0;
    private boolean player1Rumble, player2Rumble;
	private int rumbleCounter1, rumbleCounter2, rumbleTime = 5;

	private ArrayList<Particle> particles;
	private boolean playerCollision = false;

	private final int WOODBLOCK_START_COUNT = 3;
    private boolean notTriggerd = true;

	private boolean preScreen;

	private int beardCounter;
	private BufferedImage playerWin1, playerWin2;
	private BufferedImage[] beards = new BufferedImage[6];
	private int blinkCounter = 0;

    public GameBoard(WiimoteHandler wiimoteHandler) {
		this.wiimoteHandler = wiimoteHandler;
		initGameBoard();
	}
	
	private void initGameBoard() {
		new Images();
		scaleBackground();
		addWoodImages();
		wiimoteHandler.activateMotionSensing();
		addKeyListener(new KAdapter());
		setFocusable(true);
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        inGame = false;
        preScreen = true;
		player1 = new Player(START_X_PLAYER1, PLAYER_Y, 1, this);
		player2 = new Player(START_X_PLAYER2, PLAYER_Y, 2, this);
		particles = new ArrayList<>();

        winner = new BufferedImage[3];
        instructions = new BufferedImage[3];
        chosenImage = ResourceHandler.getImage("res/images_game1/instructions.png");
        text = ResourceHandler.getImage("res/images_scoreboard/text.png");
        winnerImage = ResourceHandler.getImage("res/images_scoreboard/winner.png");
        winScreen = ResourceHandler.getImage("res/images_scoreboard/background.png");

        switchInstructionsTimer = new Timer(1000, e -> {
            switch(switchInstructionsCounter)
            {
                case 0 : switchInstructionsCounter = 1; break;
                case 1 : switchInstructionsCounter = 2; break;
                case 2 : switchInstructionsCounter = 0; break;
            }
        });
		for(int i = 0; i < 3; i++){
			winner[i] = winnerImage.getSubimage(0, (242 * i), winnerImage.getWidth(), 726/3);
            instructions[i] = chosenImage.getSubimage(0, (1080*i), 1920, 1080);
		}

        switchInstructionsTimer.start();
        initWoodBlocks();

			endTimer = new Timer(time * 1000, e -> inGame = false);
			timeLeft = new Timer(1000, e -> {
				time--;
				scorePlayer1++;
				scorePlayer2++;
			});
		cutPlayerImage();

		cutBeards();

		gameLogicTimer = new Timer(1000/60, this);
	}

	private void initWoodBlocks() {
		woodBlocks = new ArrayList<>();

		for (int i = 0; i < WOODBLOCK_START_COUNT; i++)
			woodBlocks.add(new WoodBlock(getRandomInt(10, 1880), -1000, -getRandom(2,1),  woodImages[getRandom(2,0)], getRandom(360,0)));
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.scale(getWidth()/1920.0, getHeight()/1080.0);
        if(preScreen){
            g2.drawImage(background, 0, 0, null);
            g2.drawImage(Images.rescaleImage(1920, 160, Images.banner), 0, 930, null);
            g2.drawImage(instructions[switchInstructionsCounter], 0, 0, null); //make instructions
            player1.checkWiiMote(wiimoteHandler, 0);
        }else{
            if(inGame){
                g2.drawImage(background, 0, 0, null);
                AffineTransform oldFrom = g2.getTransform();
                g2.translate(0, 850);
                g2.translate(-1, -1);
                g2.translate(0, -40);
                woodBlocks.stream().filter(Sprite::getVisible).forEach(w ->
                        g2.drawImage(w.getImage(), EasyTransformer.rotateAroundCenterWithOffset(w.getImage(), w.getRotation(), 0, 0, w.getX(), w.getY()), null));
                g2.setTransform(oldFrom);
                Font tf = new Font("Verdana", Font.BOLD, 68);
                FontMetrics ft = g2.getFontMetrics(tf);

                g2.drawImage(Images.rescaleImage(1920, 160, Images.banner), 0, 930, null);
                g2.setColor(new Color(159, 44, 22));
                g2.setFont(tf);
                g2.drawString("" + time, 960 - (ft.stringWidth("" + time) / 2) + 90, 1030);


                Font pf = new Font("Calibri", Font.PLAIN, 48);
                g2.setFont(pf);

                g2.setColor(Color.BLACK);
                g2.translate(0, 850);
                g2.translate(-1, -1);
                g2.translate(0, -40);

                drawPlayers(g);

                for(Particle p : particles){
                    p.draw(g2);
                }
            }else{
                wiimoteHandler.deactivateRumble(0);
                wiimoteHandler.deactivateRumble(1);
                if(scorePlayer1 > scorePlayer2){
                    Beard.beardPlayer1 = 2;
                    drawGameEnd(g2, GameResult.PLAYER_1_WIN);
                }else if(scorePlayer2 > scorePlayer1){
                    Beard.beardPlayer2 = 2;
                    drawGameEnd(g2, GameResult.PLAYER_2_WIN);
                }else if(scorePlayer2 == scorePlayer1){
                    drawGameEnd(g2, GameResult.DRAW);
                }

                if(PartyModeHandler.getCurrentMode() == PartyModeHandler.Mode.CHOOSE_PARTY){
                    if(wiimoteHandler.getIsButtonPressed(0, WiimoteHandler.Buttons.KEY_A) || wiimoteHandler.getIsButtonPressed(1, WiimoteHandler.Buttons.KEY_A)){
                        PartyModeHandler.notifyNextGame();
                    }
                }else{
                    if(wiimoteHandler.getIsButtonPressed(0, WiimoteHandler.Buttons.KEY_A) || wiimoteHandler.getIsButtonPressed(1, WiimoteHandler.Buttons.KEY_A)){
                        ModelHandler.instance.changeModel(new NewModel(null, new MainMenuModel()));
                    }
                }
            }
        }
		Toolkit.getDefaultToolkit().sync();
	}


	private void drawPlayers(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		if (player1.getVisible() && player2.getVisible()) {
			g2.drawImage(player2.getImage(), player2.getX(), player2.getY(), this);
			g2.drawImage(player2.getBeard(Beard.beardPlayer1), player2.getX(), player2.getY(), this);
			g2.drawImage(player2.getImage2(), player2.getX(), player2.getY(), this);
			g2.drawImage(player1.getImage(), player1.getX(), player1.getY(), this);
			g2.drawImage(player1.getBeard(Beard.beardPlayer2), player1.getX(), player1.getY(), this);
			g2.drawImage(player1.getImage2(), player1.getX(), player1.getY(), this);
		}
	}

	private enum GameResult {PLAYER_1_WIN, PLAYER_2_WIN, DRAW}

    private void drawGameEnd(Graphics2D g, GameResult winner) {

        g.drawImage(winScreen, 0, 0, 1920, 1080, null);

        textScale += change;
        if(textScale > MAX_SCALE){
            change = -CHANGE_SPEED;
        }else if(textScale < MIN_SCALE){
            change = CHANGE_SPEED;
        }

        g.drawImage(text, EasyTransformer.scaleImageFromCenter(text, textScale, (1920/2) - text.getWidth(null)/2, 200), null);

        switch(winner)
        {
			case DRAW : g.drawImage(this.winner[2], 500, 100, null); break;
			case PLAYER_1_WIN: g.drawImage(this.winner[0], 500, 100, null); break; //TEKST
			case PLAYER_2_WIN: g.drawImage(this.winner[1], 500, 100, null); break; //TEKST
        }

		g.drawImage(playerWin1, (1920/2) - (1315/8) - 500, 300, null);
        g.drawImage(playerWin2, (1920/2) - (1315/8) + 530, 300, null);

		drawBeard(winner, g);
    }
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(inGame) {
			inGame();
			updatePlayer();
			updateWoodBlocks();
			checkCollision();
			checkRumble();
			Iterator<Particle> pI = particles.iterator();
			while (pI.hasNext()) {
				Particle p = pI.next();
				p.update();
				if (p.getLife() > 10) {
					pI.remove();
				}
			}
			repaint();
			player1.checkWiiMote(wiimoteHandler, 0);
			player2.checkWiiMote(wiimoteHandler, 1);

		}
	}

	public void startTimer()
	{
		gameLogicTimer.start();
	}

	private void inGame() {
		if (!inGame) {
			gameLogicTimer.stop();
		}
	}

	private void updatePlayer() {
		if (player1.getVisible() && player2.getVisible()) {
			player1.move();
			player2.move();
		}
	}

	private void updateWoodBlocks() {
			for (int i = 0; i < woodBlocks.size(); i++) {
				WoodBlock w = woodBlocks.get(i);
				if (w.getVisible()) {
					w.move();
				}
				if (w.blockIsFallen) {
					woodBlocks.remove(i);
					woodBlocks.add(new WoodBlock(getRandomInt(10, 1880), -1000, -getRandom(2,1),  woodImages[getRandom(2,0)], getRandom(360,0)));
				} else {
					blockIsFallen = true;
				}
			}
	}

	private void checkCollision() {
		Rectangle rect3 = player1.getBounds();
		Rectangle rect2 = player2.getBounds();
		Iterator<WoodBlock> it = woodBlocks.iterator();
		while(it.hasNext()) {
			WoodBlock woodBlock = it.next();
			Rectangle rect1 = woodBlock.getBounds();
			boolean hit = false;
			if (rect3.intersects(rect1)) {
				scorePlayer1-=2;
				hit = true;
				rumble(0);
				if (scorePlayer1 < 0) {
					scorePlayer1 = 0;
				}
			}
			if (rect2.intersects(rect1)) {
				scorePlayer2-=2;
				hit = true;
				rumble(1);
				if (scorePlayer2 < 0) {
					scorePlayer2 = 0;
				}
			}

			if(hit){
				for(int i = 0; i < 10; i++){
					particles.add(new Particle(woodBlock.getX(), woodBlock.getY(), i*36));
				}
				it.remove();

			}

		}
		while(woodBlocks.size() < 4){
			woodBlocks.add(new WoodBlock(getRandomInt(10, 1880), -1000, -getRandom(2,1),  woodImages[getRandom(2,0)], getRandom(360,0)));
		}

		if(rect3.intersects(rect2)){
			playerCollision = true;
			int dxPlayer1 = player1.getDx();
			int dxPlayer2 = player2.getDx();
			if(dxPlayer1 < 0){
				dxPlayer1 = -dxPlayer1;
			}
			if(dxPlayer2 < 0){
				dxPlayer2 = -dxPlayer2;
			}
			if(dxPlayer1 > dxPlayer2){
				player2.setDx(player1.getDx());
			}else{
				player1.setDx(player2.getDx());
			}
		}else{
			playerCollision = false;
		}
	}

	public boolean isInGame() {
		return inGame;
	}

	public boolean isPreScreen() {
		return preScreen;
	}

	public void setPreScreen(boolean prescreen)
	{
		this.preScreen = prescreen;
        if(!prescreen)
        {
            inGame = true;
            if(!gameLogicTimer.isRunning() && !timeLeft.isRunning() && !endTimer.isRunning() ) {
                gameLogicTimer.start();
				timeLeft.start();
				endTimer.start();
                switchInstructionsTimer.stop();
            }

        }
	}

	private int getRandomInt(int min, int max) {
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}

	private class KAdapter extends KeyAdapter {
		@Override
		public void keyReleased(KeyEvent e) {
			player1.keyReleased(e);
			player2.keyReleased(e);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			player1.keyPressed(e);
			player2.keyPressed(e);
		}
	}

	public void scaleBackground(){
		Graphics g = background.createGraphics();
		g.drawImage(Images.game1Background, 0, 0, 1920, 1080, null);
	}

	public int getRandom(int max, int min){
		return rand.nextInt((max - min) + 1) + min;
	}

	public void addWoodImages(){
		woodImages[0] = Images.woodBlock;
		woodImages[1] = Images.woodBlock2;
		woodImages[2] = Images.woodBlock3;
	}

	public void rumble(int player){
		wiimoteHandler.activateRumble(player);
		if(player == 0){
			player1Rumble = true;
		}else if(player == 1) {
			player2Rumble = true;
		}
	}

	public void checkRumble(){
        //noinspection Duplicates
        if(player1Rumble){
			rumbleCounter1++;
			if(rumbleCounter1 > rumbleTime){
				player1Rumble = false;
				rumbleCounter1 = 0;
			}
		}

        //noinspection Duplicates
        if(player2Rumble){
			rumbleCounter2++;
			if(rumbleCounter2 > rumbleTime){
				player2Rumble = false;
				rumbleCounter2 = 0;
			}
		}

		if(!player1Rumble){
			wiimoteHandler.deactivateRumble(0);
		}
		if(!player2Rumble){
			wiimoteHandler.deactivateRumble(1);
		}
	}

	public boolean getPlayerCollision(){
		return  playerCollision;
	}

	private void cutPlayerImage(){
		BufferedImage image = ResourceHandler.getImage("res/images_scoreboard/person.png");
		playerWin1 = image.getSubimage(0, 0, 311, 577);
		playerWin2 = image.getSubimage(311, 0, 311, 577);
	}

	private void drawBeard(GameResult gameResult, Graphics2D g){
		g.drawImage(playerWin1, (1920/2) - (1315/8) - 500, 300, null);
		g.drawImage(playerWin2, (1920/2) - (1315/8) + 530, 300, null);
		beardCounter++;
		int oldBeard1 = Beard.beardPlayer1 - 2;
		if(oldBeard1 < 0) {oldBeard1 = 0;}
		int oldBeard2 = Beard.beardPlayer2 - 2;
		if(oldBeard2 < 0){oldBeard2 = 0;}
		switch(gameResult)
		{
			case DRAW :
				g.drawImage(beards[Beard.beardPlayer1], (1920/2) - (1315/8) - 500, 300, null);
				g.drawImage(beards[Beard.beardPlayer2], (1920/2) - (1315/8) + 530, 300, null);
				break;
			case PLAYER_1_WIN:
				if(beardCounter < 10 && blinkCounter < 3){
					g.drawImage(beards[0], (1920/2) - (1315/8) - 500, 300, null);
					System.out.println("OLD");
				}else if (beardCounter < 20 && blinkCounter < 3){
					g.drawImage(beards[Beard.beardPlayer1], (1920/2) - (1315/8) - 500, 300, null);
				}else{
					g.drawImage(beards[Beard.beardPlayer1], (1920/2) - (1315/8) - 500, 300, null);
					beardCounter = 0;
					blinkCounter++;
				}
				g.drawImage(beards[oldBeard1], (1920/2) - (1315/8) - 500, 300, null);
				g.drawImage(beards[Beard.beardPlayer2], (1920/2) - (1315/8) + 530, 300, null);
				break;
			case PLAYER_2_WIN:
				g.drawImage(beards[Beard.beardPlayer1], (1920/2) - (1315/8)  - 500, 300, null);
				g.drawImage(beards[oldBeard1], (1920/2) - (1315/8) + 530, 300, null);
				if(beardCounter < 10  && blinkCounter < 3){
					System.out.println("OLD");
					g.drawImage(beards[0], (1920/2) - (1315/8) + 530, 300, null);
				}else if (beardCounter < 20 && blinkCounter < 3){
					g.drawImage(beards[Beard.beardPlayer2], (1920/2) - (1315/8) + 530, 300, null);
				}else{
					g.drawImage(beards[Beard.beardPlayer2], (1920/2) - (1315/8) + 530, 300, null);
					beardCounter = 0;
					blinkCounter++;
				}
				break;
		}
	}

	private void cutBeards(){
		BufferedImage image = ResourceHandler.getImage("res/images_scoreboard/beard_sprite.png");
		for(int i = 0; i < 6; i++){
			beards[i] = image.getSubimage(311 * i, 0, 311, 577);
		}
	}
}
