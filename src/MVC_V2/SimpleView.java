package MVC_V2;

import javax.swing.*;
import java.awt.*;

public class SimpleView implements View {
    @Override
    public void start() {
        System.out.println("started!");
    }

    @Override
    public void draw(Graphics2D g) {
        g.drawString("DIT WeeeeeeeeRRRUKTTTTT", 500, 500);
    }

    @Override
    public void close() {

    }

    @Override
    public void onModelEvent(ModelEvent event) {

    }

	static class Sprite {
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

        protected void setImage(Image image){
            this.image = image;
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
}
