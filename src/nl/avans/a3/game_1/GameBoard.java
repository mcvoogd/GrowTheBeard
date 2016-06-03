package nl.avans.a3.game_1;
import nl.avans.a3.util.EasyTransformer;
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

	private Timer timer;
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

	private boolean player1Win;
	private boolean player2Win;

	private final int SCHERM_BREEDTE = 1920;
	private final int SCHERM_HOOGTE = 1080;

	private final int START_X_PLAYER1 = 640;
	private final int START_X_PLAYER2 = 1280;
	private final int PLAYER_Y = -100;

	private WiimoteHandler wiimoteHandler;
	private Random rand = new Random();
	private BufferedImage[] woodImages = new BufferedImage[3];

	private BufferedImage background = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB);
	private boolean player1Rumble, player2Rumble;
	private int rumbleCounter1, rumbleCounter2, rumbleTime = 5;

	private ArrayList<Particle> particles;
	private boolean playerCollision = false;

	public GameBoard(WiimoteHandler wiimoteHandler) {
		this.wiimoteHandler = wiimoteHandler;
		initGameBoard();
	}


	private void initGameBoard() {
//		test();
		new Images();
		scaleBackground();
		addWoodImages();
		wiimoteHandler.activateMotionSensing();
		addKeyListener(new KAdapter());
		setFocusable(true);
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(SCHERM_BREEDTE, SCHERM_HOOGTE));
		inGame = true;
		player1 = new Player(START_X_PLAYER1, PLAYER_Y, 1, this);
		player2 = new Player(START_X_PLAYER2, PLAYER_Y, 2, this);
		particles = new ArrayList<>();

		initWoodBlocks();
		if (inGame) {
			endTimer = new Timer(time * 1000, e -> inGame = !inGame);
			endTimer.start();

			timeLeft = new Timer(1000, e -> {
				time--;
				scorePlayer1++;
				scorePlayer2++;
			});
			timeLeft.start();
		}
		timer = new Timer(1000/60, this);
		timer.start();
	}

	private void initWoodBlocks() {
		woodBlocks = new ArrayList<>();
		woodBlocks.add(new WoodBlock(getRandomInt(10, 1880), -1000, -getRandom(2,1),  woodImages[getRandom(2,0)], getRandom(360,0)));
		woodBlocks.add(new WoodBlock(getRandomInt(10, 1880), -1000, -getRandom(2,1),  woodImages[getRandom(2,0)], getRandom(360,0)));
		woodBlocks.add(new WoodBlock(getRandomInt(10, 1880), -1000, -getRandom(2,1),  woodImages[getRandom(2,0)], getRandom(360,0)));
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.scale(getWidth()/1920.0, getHeight()/1080.0);
		if (inGame) {
			g2.drawImage(background, 0, 0, null);
			AffineTransform oldFrom = g2.getTransform();
			g2.translate(0, 850);
			g2.translate(-1, -1);
			g2.translate(0, -40);
			for (WoodBlock w : woodBlocks) {
				if (w.getVisible()) {
					g2.drawImage(w.getImage(), EasyTransformer.rotateAroundCenterWithOffset(w.getImage(), w.getRotation(), 0, 0, w.getX(), w.getY()), null);

				}
			}
			g2.setTransform(oldFrom);
			Font tf = new Font("Verdana", Font.BOLD, 68);
			FontMetrics ft = g2.getFontMetrics(tf);

			g2.drawImage(Images.rescaleImage(1920,160, Images.banner), 0, 930, null);
			g2.setColor(new Color(159, 44, 22));
			g2.setFont(tf);
			g2.drawString("" + time, 960 - (ft.stringWidth("" + time)/2) + 90, 1030);



			Font pf = new Font("Calibri", Font.PLAIN, 48);
			g2.setFont(pf);
//			g2.setColor(new Color(0x161BFF));
//			g2.drawString("Score speler 1: " + scorePlayer1, 50, 1050);
//			g2.setColor(new Color(0x2CE21C));
//			g2.drawString("Score speler 2: " + scorePlayer2, 1500, 1050);

			g2.setColor(Color.BLACK);
			g2.translate(0, 850);
			g2.translate(-1, -1);
			g2.translate(0, -40);

			drawPlayers(g);



			for(Particle p : particles){
				p.draw(g2);
			}
		}

		if (!inGame) {
			wiimoteHandler.deactivateRumble(0);
			wiimoteHandler.deactivateRumble(1);
			if (scorePlayer1 > scorePlayer2) {
				drawGameEnd(g, 1);
			}
			if (scorePlayer2 > scorePlayer1) {
				drawGameEnd(g, 2);
			}
			else{
				//drawGameEnd(g);
			}
		}
		Toolkit.getDefaultToolkit().sync();
	}

	private void drawPlayers(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		if (player1.getVisible() && player2.getVisible()) {
			g2.drawImage(player1.getImage(), player1.getX(), player1.getY(), this);
			g2.drawImage(player2.getImage(), player2.getX(), player2.getY(), this);
		}
	}

	private void drawGameEnd(Graphics g, int player) {
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(Images.game1Winscreen.getScaledInstance(1920, 1080, BufferedImage.SCALE_DEFAULT), 0, 0, null);
		Font font = new Font("Sansserif", Font.BOLD, 360);
		FontMetrics fm = getFontMetrics(font);
		g2.setFont(font);
		String s = "DRAW";
		switch(player)
		{
			case 0 : g.setColor(Color.BLACK);
				g.drawString(s, ((1920/2) - (fm.stringWidth(s) / 2)), 300);
				break; //default
			case 1 :
				s = "WINNER";
				g.setColor(new Color(50, 200, 55));
				g.drawString(s, ((1920/2) - (fm.stringWidth(s) / 2)), 300);
				break;
			case 2 :
				s = "WINNER";
				g.setColor(new Color(200, 50, 50));
				g.drawString(s, ((1920/2) - (fm.stringWidth(s) / 2)), 300);
		}

		g2.drawImage(Images.player1.getSubimage(0, 0, 1315, 1922), ((1920/2) - (1315/8) - 200), 450, 1315/4, 1922/4, null);
		g2.drawImage(Images.player2.getSubimage(0, 0, 1315, 1922), ((1920/2) - (1315/8) + 200), 450, 1315/4, 1922/4, null);


	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		inGame();
		updatePlayer();
		updateWoodBlocks();
		checkCollision();
		checkRumble();
		Iterator<Particle> pI = particles.iterator();
		while(pI.hasNext()){
			Particle p = pI.next();
			p.update();
			if(p.getLife() > 10){
				pI.remove();
			}
		}
		repaint();
		player1.checkWiiMote(wiimoteHandler, 0);
		player2.checkWiiMote(wiimoteHandler, 1);
	}

	private void inGame() {
		if (!inGame) {
			timer.stop();
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
				scorePlayer1--;
				hit = true;
				rumble(0);
				if (scorePlayer1 < 0) {
					scorePlayer1 = 0;
				}
			}
			if (rect2.intersects(rect1)) {
				scorePlayer2--;
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
		if(player1Rumble){
			rumbleCounter1++;
			if(rumbleCounter1 > rumbleTime){
				player1Rumble = false;
				rumbleCounter1 = 0;
			}
		}

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

	public void test(){
		time = 5;
		scorePlayer2 = 100;
	}
}
