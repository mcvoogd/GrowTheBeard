package Game_01;
import nl.avans.a3.WiimoteHandler;
import Util.Images;

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
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
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

	private WiimoteHandler wiimoteHandler;
	private Random rand = new Random();

	private BufferedImage background = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB);

	public GameBoard() {
		initGameBoard();
	}

	private void initGameBoard() {
		new Images();
		scaleBackground();
		wiimoteHandler = new WiimoteHandler();
		wiimoteHandler.SearchWiimotes();
		wiimoteHandler.activateMotionSensing();
		addKeyListener(new KAdapter());
		setFocusable(true);
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(SCHERM_BREEDTE, SCHERM_HOOGTE));
		inGame = true;
		player1 = new Player(START_X_PLAYER1, -206, 1);
		player2 = new Player(START_X_PLAYER2, -206, 2);

		initWoodBlocks();
	if (inGame) {
		endTimer = new Timer(30000, e -> inGame = !inGame);
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
		woodBlocks.add(new WoodBlock(40, -800, -getRandom(2,1)));
		woodBlocks.add(new WoodBlock(90, -800, -getRandom(2,1)));
		woodBlocks.add(new WoodBlock(120, -800, -getRandom(2,1)));
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.scale(getWidth()/1920.0, getHeight()/1080.0);
		if (inGame) {
			g2.drawImage(background, 0, 0, null);
			Font tf = new Font("Calibri", Font.BOLD, 72);
			g2.setFont(tf);
			g2.drawString("Time left: " + time, 750, 50);

			Font pf = new Font("Calibri", Font.PLAIN, 48);
			g2.setFont(pf);
			g2.setColor(new Color(0x161BFF));
			g2.drawString("Score speler 1: " + scorePlayer1, 50, 1000);
			g2.setColor(new Color(0x2CE21C));
			g2.drawString("Score speler 2: " + scorePlayer2, 1500, 1000);

			g2.setColor(Color.BLACK);
			g2.translate(0, 850);
			g2.translate(-1, -1);
			g2.draw(new Line2D.Double(0, 0, 1920, 0));
			g2.translate(0, -40);
			drawPlayers(g);

			for (WoodBlock w : woodBlocks) {
				if (w.getVisible()) {
					g.drawImage(w.getImage(), w.getX(), w.getY(), this);
				}
			}
		}

		if (!inGame) {
			if (scorePlayer1 > scorePlayer2) {
				drawGameEndPL1(g);
			}
			if (scorePlayer2 > scorePlayer1) {
				drawGameEndPL2(g);
			}
			else{
				//drawGameEnd(g);
			}
		}
		Toolkit.getDefaultToolkit().sync();
	}

	private void drawPlayers(Graphics g) {
		if (player1.getVisible() && player2.getVisible()) {
			g.drawImage(player1.getImage(), player1.getX(), player1.getY(), this);
			g.drawImage(player2.getImage(), player2.getX(), player2.getY(), this);
		}
	}

	private void drawGameEnd(Graphics g) {
		String s = " WOODBLOCKS!";
		Font font = new Font("Jokerman", Font.BOLD, 48);
		FontMetrics fm = getFontMetrics(font);

		g.setColor(Color.BLACK);
		g.setFont(font);
		g.drawString(s, (500 - fm.stringWidth(s)) / 2, 400 / 2);
	}

	private void drawGameEndPL1(Graphics g) {
		String s = " PLAYER 1 WINS!";
		Font font = new Font("Jokerman", Font.BOLD, 48);
		FontMetrics fm = getFontMetrics(font);

		g.setColor(Color.BLUE);
		g.setFont(font);
		g.drawString(s, (500 - fm.stringWidth(s)) / 2, 400 / 2);
	}

	private void drawGameEndPL2(Graphics g) {
		String s = " PLAYER 2 WINS!";
		Font font = new Font("Jokerman", Font.BOLD, 48);
		FontMetrics fm = getFontMetrics(font);

		g.setColor(Color.GREEN);
		g.setFont(font);
		g.drawString(s, (500 - fm.stringWidth(s)) / 2, 400 / 2);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		inGame();
		updatePlayer();
		updateWoodBlocks();
		checkCollision();
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
					woodBlocks.add(new WoodBlock(getRandomInt(10, 1880), -800, -getRandom(2,1)));
				} else {
					blockIsFallen = true;
				}
			}
	}

	private void checkCollision() {
		Rectangle rect3 = player1.getBounds();
		Rectangle rect2 = player2.getBounds();

		for (WoodBlock wb : woodBlocks) {
			Rectangle rect1 = wb.getBounds();

			if (rect3.intersects(rect1)) {
				scorePlayer1--;
				if (scorePlayer1 < 0) {
					scorePlayer1 = 0;
				}
			}
			if (rect2.intersects(rect1)) {
				scorePlayer2--;
				if (scorePlayer2 < 0) {
					scorePlayer2 = 0;
				}
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

}
