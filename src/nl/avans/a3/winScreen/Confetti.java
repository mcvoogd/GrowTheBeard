package nl.avans.a3.winScreen;

import java.awt.*;

/**
 * Created by Harmen on 18-6-2016.
 */
public class Confetti {

    private int x, y;
    private double angle;
    private int speed = 10;
    private int counter;
    private boolean falling;
    private Color color;
    private int life;

    private int verticalSpeed,terminalVelocity= 5, gravity = 1 ;
    private boolean stop;

    public Confetti(int x, int y, double angle){
        this.x = x;
        this.y = y;
        this.angle = angle + Math.random() - 0.5;
        speed = (int) ((Math.random() * 10) + 5);
        color = new Color((int) (Math.random( ) * 200 + 50), (int) (Math.random() * 200 + 50), (int) (Math.random() * 200 + 50));
        verticalSpeed = 40 + (int) (Math.random() * 5);
    }

    public void update(){

        x += Math.cos(angle) * speed;
        if(!stop) {
            if (!falling) {
                y -= verticalSpeed;
                if (verticalSpeed > 0) {
                    verticalSpeed -= gravity;
                } else {
                    falling = true;
                    counter = -50;

                }
            } else {
                y += verticalSpeed;
                if (verticalSpeed < terminalVelocity) {
                    verticalSpeed += gravity;
                }
                if (angle > Math.PI + Math.PI / 2)
                    angle -= 0.01;
                else
                    angle += 0.01;
            }
        }else{
            life++;
        }
    }

    public void draw(Graphics2D g){
        g.setColor(color);
        g.fillRect(x, y, 10, 10);
    }

    public int getY() {
        return y;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
        speed = 0;
    }

    public int getLife() {
        return life;
    }
}
