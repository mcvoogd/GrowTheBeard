package Game_01;
import Util.Images;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.Timer;

public class WoodBlock extends Sprite {

	private Timer timer;
	private int vel = -1;
	private int rotation;
	protected boolean blockIsFallen;

	public WoodBlock(int xPos, int yPos, int vel, BufferedImage image, int rotation) {
		super(xPos, yPos);
		this.vel = vel;
		this.rotation = rotation;
		visible = true;
		initWoodBlock(image);
	}

	private void initWoodBlock(BufferedImage image) {
//		loadImage("Sprite3.png");
		Image imageNew = image.getScaledInstance(150, 142, BufferedImage.SCALE_DEFAULT);
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
				rotation++;
			}
		});
		timer.start();
	}

	public int getRotation(){
		return rotation;
	}
}
