package Game_01;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.Timer;

public class Player extends Sprite {

	int dx, dy;
	int number;
	boolean jump = false;

	public Player(int xPos, int yPos, int number) {
		super(xPos, yPos);
		this.number = number;
		initPlayer();
	}
	
	private void initPlayer() {
		if (number == 1) {
			loadImage("Sprite1.png");
			getDimensions();
		}
		if (number == 2) {
			loadImage("Sprite2.png");
			getDimensions();
		}
	}

	public int getNumber() {
		return number;
	}

	public void setJump(boolean jump) {
		this.jump = jump;
	}
	
	public void move() {
		xPos += dx;
		yPos += dy;

		if (xPos < 1) {
			xPos = 1;
		}
		if (xPos > 1870) {
			xPos = 1870;
		}
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (number == 1) {
			if (key == KeyEvent.VK_LEFT) {
				dx = -10;
			}
			if (key == KeyEvent.VK_RIGHT) {
				dx = 10;
			}
			if (key == KeyEvent.VK_UP) {
				jump = true;
				Timer engine = new Timer(1, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (jump) {
							dy = -15;
							if (yPos <= -200) {
								jump = !jump;
							}
						}
						else if (yPos >= 0){
							dy = 0;
						}
						else {
							dy = 15;
						}
					}
				});
				engine.start();
			}
		}
		if (number == 2) {
			if (key == KeyEvent.VK_A) {
				dx = -10;
			}
			if (key == KeyEvent.VK_D) {
				dx = 10;
			}
			if (key == KeyEvent.VK_W) {	
				jump = true;
				Timer engine = new Timer(1, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (jump) {
							dy = -15;
							if (yPos <= -200) {
								jump = !jump;
							}
						}
						else if (yPos >= 0){
							dy = 0;
						}
						else {
							dy = 15;
						}
					}
				});
				engine.start();
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if (number == 1) {
			if (key == KeyEvent.VK_LEFT) {
				dx = 0;
			}
			if (key == KeyEvent.VK_RIGHT) {
				dx = 0;
			}
		}
		if (number == 2) {
			if (key == KeyEvent.VK_A) {
				dx = 0;
			}
			if (key == KeyEvent.VK_D) {
				dx = 0;
			}
		}
	}

}
