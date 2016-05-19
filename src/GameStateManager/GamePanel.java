package GameStateManager;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.Timer;

import control.GameController;
import control.GameStateManager;

public class GamePanel extends JPanel{
	
	private GameStateManager gsm;
	private Timer paintTimer;
	private Timer gameTimer;
	private GameController gc;
	public GamePanel(GameController gc){
		this.gc = gc;
		this.gsm = this.gc.getGameStateManager();
		this.setFocusable(true);
		paintTimer = new Timer(1000/60,new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
		});
		paintTimer.start();
		gameTimer = new Timer(1000/30,new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gsm.update();
			}
		});
		gameTimer.start();
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e)
			{
				gsm.keyPressed(e.getKeyCode());
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
				{
					System.out.println("Game closed...");
					System.exit(0);
				}
			}
			public void keyReleased(KeyEvent e){
				gsm.keyReleased(e.getKeyCode());
			}
		});
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		gsm.draw(g2);
	}
}
