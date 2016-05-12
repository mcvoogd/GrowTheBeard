package Game_01;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class WoodBlock extends Sprite {

	private Timer timer;
	private int vel = -1;
	protected boolean blockIsFallen;

	public WoodBlock(int xPos, int yPos) {
		super(xPos, yPos);
		visible = true;
		initWoodBlock();
	}

	private void initWoodBlock() {
		loadImage("Sprite3.png");
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
