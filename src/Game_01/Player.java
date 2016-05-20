package Game_01;
import Util.Images;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.Timer;

public class Player extends Sprite {

	private int dx, dy;
	private int ty = -15;
	private int number;
	private boolean jump = false, falling = false;

	public Player(int xPos, int yPos, int number) {
		super(xPos, yPos);
		this.number = number;
		initPlayer();
	}
	
	private void initPlayer() {
		if (number == 1) {
			//loadImage("Sprite1.png");
			Image image = Images.player1.getScaledInstance(150, 246, BufferedImage.SCALE_DEFAULT);
			setImage(image);
			getDimensions();
		}
		if (number == 2) {
			//loadImage("Sprite2.png");
			Image image = Images.player2.getScaledInstance(150, 246, BufferedImage.SCALE_DEFAULT);
			setImage(image);
			getDimensions();
		}
	}

	public int getNumber() {
		return number;
	}

	public void setJump(boolean jump) {
		this.jump = jump;
	}
	
	void move() {
		xPos += dx;
		yPos += dy;

		if (xPos < 1) {
			xPos = 1;
		}
		if (xPos > 1870) {
			xPos = 1870;
		}
	}

	void keyPressed(KeyEvent e) {
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
				Timer engine = new Timer(25, e1 -> {
                    if (jump) {
						dy = ty;
						if (ty < 0) {
							ty++;
						}
						else {
							jump = false;
							falling = true;
						}
					}
					else if (falling) {
						dy = -ty;
						if (ty  > -10) {
							ty--;
						}
						if (yPos >= -206) {
							dy = 0;
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
				Timer engine = new Timer(1, e1 -> {
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
                });
				engine.start();
			}
		}
	}

	void keyReleased(KeyEvent e) {
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
