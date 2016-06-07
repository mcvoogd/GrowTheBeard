package nl.avans.a3.game_1;

import javax.swing.*;
import java.awt.*;

class Sprite {
	protected int xPos, yPos;
	protected boolean visible;
	protected int width, height;
	protected Image image;
	protected Image image2;
	
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

	protected void setImage(Image image){
		this.image = image;
	}

	protected void setImage2(Image image2){
		this.image2 = image2;
	}

	public Image getImage() {
		return image;
	}

	public Image getImage2() {
		return image2;
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
