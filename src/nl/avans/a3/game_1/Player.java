package nl.avans.a3.game_1;

import nl.avans.a3.util.WiimoteHandler;
import nl.avans.a3.game_1.Util.Images;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

class Player extends Sprite {

	private int dx, dy, dxCollision;
	private int ty = -50;
	private int number;
	private boolean jump = false, falling = false;
	private Timer engine;
	private float pitchDeadzone = 10f;
	private int floor;
	private GameBoard gameBoard;
	private BufferedImage[] imagesPlayer1, imagesPlayer2;

	public Player(int xPos, int yPos, int number, GameBoard gameBoard) {
		super(xPos, yPos);
		floor = yPos;
		this.gameBoard = gameBoard;
		this.number = number;
		new Images();
		initPlayer();
	}
	
	private void initPlayer() {
		if (number == 1) {
			imagesPlayer1 = new  BufferedImage[4];
			//loadImage("Sprite1.png");
			Image image = Images.player1.getScaledInstance(168 * 4, 246, BufferedImage.SCALE_DEFAULT);
			BufferedImage bufferedImage = new BufferedImage(168 * 4, 246, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = bufferedImage.createGraphics();
			g2.drawImage(image, 0, 0, null);
			for(int i = 0; i < 4; i++){
				imagesPlayer1[i] = bufferedImage.getSubimage(168 * i, 0, 150, 246);
			}
			setImage(imagesPlayer1[0]);
			getDimensions();
		}
		if (number == 2) {
			imagesPlayer2 = new BufferedImage[4];
			//loadImage("Sprite2.png");
			Image image = Images.player2.getScaledInstance(168 * 4, 246, BufferedImage.SCALE_DEFAULT);
			BufferedImage bufferedImage = new BufferedImage(168 * 4, 246, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = bufferedImage.createGraphics();
			g2.drawImage(image, 0, 0, null);
			for(int i = 0; i < 4; i++){
				imagesPlayer2[i] = bufferedImage.getSubimage(168 * i, 0, 168, 246);
			}
			setImage(imagesPlayer2[0]);
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
		int oldX = xPos;
		int oldY = yPos;
		xPos += dx;
		yPos += dy;
		if(gameBoard.getPlayerCollision()){
			xPos += dxCollision;
		}
		if (xPos < 1) {
			xPos = 1;
		}
		if (xPos > 1870) {
			xPos = 1870;
		}
		setPlayerImage();
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
							ty += 3;
						}
						else {
							jump = false;
							falling = true;
						}
					}
					else if (falling) {
						dy = -ty;
						if (ty > -30) {
							ty -= 6;
						}
						if (yPos > floor) {
							falling = false;
							dy = 0;
							ty = -50;
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

	public int getDx(){
		return dx;
	}

	public void setDx(int dx){
		dxCollision = dx;
	}

	public void setPlayerImage(){
		if(number == 1){
			if(ty == -50){
				setImage(imagesPlayer1[0]);
			}
			if(ty > -50){
				setImage(imagesPlayer1[1]);
			}
			if(ty > -40){
				setImage(imagesPlayer1[2]);
			}
			if(ty > -30){
				setImage(imagesPlayer1[3]);
			}
		}
		if(number == 2){
			if(ty == -50){
				setImage(imagesPlayer2[0]);
			}
			if(ty > -50){
				setImage(imagesPlayer2[1]);
			}
			if(ty > -40){
				setImage(imagesPlayer2[2]);
			}
			if(ty > -30){
				setImage(imagesPlayer2[3]);
			}
		}

	}
}
