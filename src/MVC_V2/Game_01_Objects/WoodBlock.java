package MVC_V2.Game_01_Objects;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.Timer;

public class WoodBlock extends Sprite {

	private Timer timer;
	private int vel = -1;
	private int rotation;
	protected boolean blockIsFallen;
	private Random rand = new Random();
	private int rotationChance = getRandom(20,5);
	private int rotationCounter;

	public WoodBlock(int xPos, int yPos, int vel, BufferedImage image, int rotation) {
		super(xPos, yPos);
		this.vel = vel;
		this.rotation = rotation;
		visible = true;
		initWoodBlock(image);
	}

	private void initWoodBlock(BufferedImage image) {
//		loadImage("Sprite3.png");
		Image imageNew = image.getScaledInstance(75, 121, BufferedImage.SCALE_DEFAULT);
		setImage(imageNew);
		getDimensions();
	}

	public void move() {
		timer = new Timer(40, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				yPos -= vel;
				if (yPos >= 400) {
					visible = false;
					blockIsFallen = true;
				}
				rotationCounter++;
				if(rotationCounter > rotationChance){
					rotation++;
					rotationCounter = 0;
				}
			}
		});
		timer.start();
	}

	public int getRotation(){
		return rotation;
	}

	public int getRandom(int max, int min){
		return rand.nextInt((max - min) + 1) + min;
	}
}
