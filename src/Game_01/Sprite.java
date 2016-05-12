package Game_01;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Sprite {
	protected int xPos, yPos;
	protected boolean visible;
	protected int width, height;
	protected Image image;
	
	public Sprite(int xPos, int yPos) {
		this.xPos = xPos;
		this.yPos = yPos;
		visible = true;
	}
	
	protected void getDimensions() {
		width = image.getWidth(null);
		height = image.getHeight(null);
	}
	
	protected void loadImage(String imageName) {
		ImageIcon ii = new ImageIcon(imageName);
		image = ii.getImage();
	}
	
	public Image getImage() {
		return image;
	}
	
	public int getX() {
		return xPos;
	}
	
	public int getY() {
		return yPos;
	}
	
	public boolean getVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(xPos, yPos, width, height);
	}
}
