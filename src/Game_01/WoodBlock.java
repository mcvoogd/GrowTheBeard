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
	protected boolean blockIsFallen;

	public WoodBlock(int xPos, int yPos, int vel) {
		super(xPos, yPos);
		this.vel = vel;
		visible = true;
		initWoodBlock();
	}

	private void initWoodBlock() {
//		loadImage("Sprite3.png");
		Image image = Images.woodBlock.getScaledInstance(150, 142, BufferedImage.SCALE_DEFAULT);
		setImage(image);
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
			}
		});
		timer.start();
	}
}
