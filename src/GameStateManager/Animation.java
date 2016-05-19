package GameStateManager;

import java.awt.Image;
import java.awt.image.BufferedImage;

public class Animation {

	private BufferedImage spritesheet;
	private BufferedImage[] subimage;
	private int spriteIndex;
	private boolean hasPlayedOnce;
	private long startTime, delay;

	public Animation(Image spritesheet, int width, int height, long delay) {
		this.spritesheet = (BufferedImage) spritesheet;
	
		this.spriteIndex = 0;
		this.delay = delay;
		this.startTime = System.nanoTime();

		this.subimage = new BufferedImage[spritesheet.getWidth(null) / width];
		for (int i = 0; i < subimage.length; i++) {
			subimage[i] = this.spritesheet.getSubimage(i * width, 0, width,	height);
		}
	
	}

	public void update() {

		if (delay == -1)
			return;

		long elapsed = (System.nanoTime() - startTime) / 1000000;
		if (elapsed > delay) {
			spriteIndex++;
			startTime = System.nanoTime();
		}
		if (spriteIndex == subimage.length) {
			spriteIndex = 0;
			hasPlayedOnce = true;
		}

	}

	public BufferedImage getCurrentImage() {
		return subimage[spriteIndex];
	}
	
	
	public void setDelay(long d) {
		delay = d;
	}

	public void setFrame(int i) {
		spriteIndex = i;
	}

	public void setPlayedOnce(boolean bool) {
		this.hasPlayedOnce = bool;
	}
	
	public boolean hasPlayedOnce() {
		return hasPlayedOnce;
	}

}
