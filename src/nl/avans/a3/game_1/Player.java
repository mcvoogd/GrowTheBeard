package nl.avans.a3.game_1;

import nl.avans.a3.util.ResourceHandler;
import nl.avans.a3.util.WiimoteHandler;
import nl.avans.a3.game_1.Util.Images;

import javax.annotation.Resource;
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
	private BufferedImage[] beard;

	public Player(int xPos, int yPos, int number, GameBoard gameBoard) {
		super(xPos, yPos);
		floor = yPos;
		this.gameBoard = gameBoard;
		this.number = number;
		new Images();
		initPlayer();
	}
	
	private void initPlayer() {
		BufferedImage beards = ResourceHandler.getImage("res/images_game1/beard.png");
//		beards = (BufferedImage) beards.getScaledInstance(168 * 6, 246, BufferedImage.SCALE_DEFAULT);
		beard = new BufferedImage[6];
		for (int i = 0; i < 6; i++) {
			beard[i] = beards.getSubimage(168 * i, 0, 168, 246);
		}
		if (number == 1) {
			imagesPlayer1 = new  BufferedImage[8];
			//loadImage("Sprite1.png");
			Image image = Images.player1;
			BufferedImage bufferedImage = new BufferedImage(168 * 8, 246, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = bufferedImage.createGraphics();
			g2.drawImage(image, 0, 0, null);
			for(int i = 0; i < 8; i++){
				imagesPlayer1[i] = bufferedImage.getSubimage(168 * i, 0, 168, 246);
			}
			setImage(imagesPlayer1[0]);
			getDimensions();
		}
		if (number == 2) {
			imagesPlayer2 = new BufferedImage[8];
			//loadImage("Sprite2.png");
			Image image = Images.player2;
			BufferedImage bufferedImage = new BufferedImage(168 * 8, 246, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = bufferedImage.createGraphics();
			g2.drawImage(image, 0, 0, null);
			for(int i = 0; i < 8; i++){
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
		if (xPos > 1770) {
			xPos = 1770;
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

        if(gameBoard.isInGame()) {
            float pitch = wiimoteHandler.getPitch(id);

            if (pitch > pitchDeadzone || pitch < -pitchDeadzone) {
                dx = Math.round(-pitch / 2);
            } else {
                dx = 0;
            }


            if (checkAllButtons(id, wiimoteHandler)) {
                if (!falling)
                    jump = true;
                if (engine == null) {
                    engine = new Timer(25, e1 -> {
                        if (jump) {
                            dy = ty;
                            if (ty <= 0) {
                                ty += 3;
                            } else {
                                jump = false;
                                falling = true;
                            }
                        } else if (falling) {
                            dy = -ty;
                            if (ty > -30) {
                                ty -= 6;
                            }
                            if (yPos > floor) {
                                falling = false;
                                dy = 0;
                                ty = -50;
                                yPos = floor;
                            }
                        }
                    });
                    engine.start();
                }
            }
        }
        else if(gameBoard.isPreScreen())
        {

            if(wiimoteHandler.getIsButtonPressed(id, WiimoteHandler.Buttons.KEY_A)) {
                {
                    gameBoard.setPreScreen(false);
                    System.out.println("wow");
                }
            }
        }
	}

	public boolean checkAllButtons(int wiimoteID, WiimoteHandler wiimoteHandler)
	{if(wiimoteHandler.getIsButtonDown(wiimoteID, WiimoteHandler.Buttons.KEY_A)
		|| wiimoteHandler.getIsButtonDown(wiimoteID, WiimoteHandler.Buttons.KEY_B)
			|| wiimoteHandler.getIsButtonDown(wiimoteID, WiimoteHandler.Buttons.KEY_1)
			|| wiimoteHandler.getIsButtonDown(wiimoteID, WiimoteHandler.Buttons.KEY_2)
			|| wiimoteHandler.getIsButtonDown(wiimoteID, WiimoteHandler.Buttons.KEY_DOWN)
			|| wiimoteHandler.getIsButtonDown(wiimoteID, WiimoteHandler.Buttons.KEY_LEFT)
			|| wiimoteHandler.getIsButtonDown(wiimoteID, WiimoteHandler.Buttons.KEY_MINUS)
			|| wiimoteHandler.getIsButtonDown(wiimoteID, WiimoteHandler.Buttons.KEY_PLUS)
			|| wiimoteHandler.getIsButtonDown(wiimoteID, WiimoteHandler.Buttons.KEY_RIGHT)
			|| wiimoteHandler.getIsButtonDown(wiimoteID, WiimoteHandler.Buttons.KEY_UP))
	{
		return true;
	}
		return false;
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
				setImage2(imagesPlayer1[1]);
			}
			if(ty > -50){
				setImage(imagesPlayer1[2]);
				setImage2(imagesPlayer1[3]);
			}
			if(ty > -40){
				setImage(imagesPlayer1[4]);
				setImage2(imagesPlayer1[5]);
			}
			if(ty > -30){
				setImage(imagesPlayer1[6]);
				setImage2(imagesPlayer1[7]);
			}
		}
		if(number == 2){
			if(ty == -50){
				setImage(imagesPlayer2[0]);
				setImage2(imagesPlayer2[1]);
			}
			if(ty > -50){
				setImage(imagesPlayer2[2]);
				setImage2(imagesPlayer2[3]);
			}
			if(ty > -40){
				setImage(imagesPlayer2[4]);
				setImage2(imagesPlayer2[5]);
			}
			if(ty > -30){
				setImage(imagesPlayer2[6]);
				setImage2(imagesPlayer2[7]);
			}
		}

	}

	public BufferedImage getBeard(int beard){
		return this.beard[beard];
	}
}
