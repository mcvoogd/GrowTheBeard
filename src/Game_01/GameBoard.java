package Game_01;
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
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GameBoard extends JPanel implements ActionListener {

	private Timer timer;
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

	public GameBoard() {
		initGameBoard();
	}

	private void initGameBoard() {
		addKeyListener(new KAdapter());
		setFocusable(true);
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(SCHERM_BREEDTE, SCHERM_HOOGTE));

		inGame = true;
		player1 = new Player(START_X_PLAYER1, 0, 1);
		player2 = new Player(START_X_PLAYER2, 0, 2);

		initWoodBlocks();

		timer = new Timer(1000/60, this);
		timer.start();
	}

	public void initWoodBlocks() {
		woodBlocks = new ArrayList<>();
		woodBlocks.add(new WoodBlock(40, -800));
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		if (inGame) {
			g2.translate(0, 850);
			g2.translate(-1, -1);
			g2.draw(new Line2D.Double(0, 0, 1920, 0));
			g2.translate(0, -40);
			drawPlayers(g);

			for (int i = 0; i < woodBlocks.size(); i++) {
				WoodBlock w = woodBlocks.get(i);
				if (w.getVisible()) {
					g.drawImage(w.getImage(), w.getX(), w.getY(), this);
				}
			}
		}

		if (!inGame && player1Win) {
			drawGameEndPL1(g);
		}

		if (!inGame && player2Win) {
			drawGameEndPL2(g);
		}
		if (!inGame && !player1Win && !player2Win) {
			drawGameEnd(g);
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
				woodBlocks.add(new WoodBlock(getRandomInt(10, 1880), -800));
			} else {
				blockIsFallen = true;
			}
		}
	}

	public void checkCollision() {
		Rectangle rect3 = player1.getBounds();
		Rectangle rect2 = player2.getBounds();

		for (WoodBlock wb : woodBlocks) {
			Rectangle rect1 = wb.getBounds();

			if (rect3.intersects(rect1)) {
				player1.setVisible(false);
				inGame = false;
				player2Win = true;
			}
			if (rect2.intersects(rect1)) {
				player2.setVisible(false);
				inGame = false;
				player1Win = true;
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

}
