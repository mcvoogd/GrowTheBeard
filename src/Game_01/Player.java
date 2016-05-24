package Game_01;
import nl.avans.a3.WiimoteHandler;
import Util.Images;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.Timer;

public class Player extends Sprite {

	private int dx, dy;
	private int ty = -15;
	private int number;
	private boolean jump = false, falling = false;
	private Timer engine;
	private float pitchDeadzone = 10f;
	private int floor;

	public Player(int xPos, int yPos, int number) {
		super(xPos, yPos);
		floor = yPos;
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

	public void checkWiiMote(WiimoteHandler wiimoteHandler, int id){
		float pitch = wiimoteHandler.getPitch(id);
		if(wiimoteHandler.getIsButtonDown(id, WiimoteHandler.Buttons.KEY_LEFT)){
			dx = -10;
		}else if(wiimoteHandler.getIsButtonDown(id, WiimoteHandler.Buttons.KEY_RIGHT)){
			dx = 10;
		}else if(pitch > pitchDeadzone || pitch < -pitchDeadzone){
			dx = Math.round(-pitch/2);
		}
		else
		{
			dx = 0;
		}



		if(wiimoteHandler.getIsButtonDown(id, WiimoteHandler.Buttons.KEY_A)){
			if(!falling)
				jump = true;
			if(engine == null){

				engine = new Timer(25, e1 -> {
					if (jump) {
						dy = ty;
						if (ty <= 0) {
							ty++;
							System.out.println(ty);
						}
						else {
							jump = false;
							falling = true;
						}
					}
					else if (falling) {
						dy = -ty;
						if (ty > -10) {
							ty--;
						}
						if (yPos > floor) {
							falling = false;
							dy = 0;
							ty = -15;
						}
					}
				});
				engine.start();
			}
		}

//		if(wiimoteHandler.getZDifference(id) > 0.25){
//			if(!falling)
//				jump = true;
//			if(engine == null){
//
//				engine = new Timer(25, e1 -> {
//					if (jump) {
//						dy = ty;
//						if (ty <= 0) {
//							ty++;
//							System.out.println(ty);
//						}
//						else {
//							jump = false;
//							falling = true;
//						}
//					}
//					else if (falling) {
//						dy = -ty;
//						if (ty > -10) {
//							ty--;
//						}
//						if (yPos > -210) {
//							falling = false;
//							dy = 0;
//							ty = -15;
//						}
//					}
//				});
//				engine.start();
//			}
//		}
	}

}
