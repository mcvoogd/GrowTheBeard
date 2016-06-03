package nl.avans.a3.game_3;

import java.awt.*;
import java.util.Random;

public class DamageNumber {

    private int x;
    private int y;
    private Color color;
    private int damage;
    private int life;
    private boolean leftOrRight;
    private boolean alive = true;
    private Random rand;

    public DamageNumber(int x, int y, int damage, Color color, boolean leftOrRight)
    {
        this.x = x;
        this.y = y;
        this.leftOrRight = !leftOrRight;
        this.damage = damage;
        this.color = color;
        rand = new Random();
        life = rand.nextInt(255);
    }

    public void update()
    {
        life--;
        if(leftOrRight)
        {
            x += -rand.nextInt(5) + Math.cos(rand.nextInt((int)Math.PI*2));
        }else {
            x += rand.nextInt(5) + Math.cos(rand.nextInt((int) Math.PI/2));
        }
        y += -10+  Math.sin(rand.nextInt((int) (Math.PI/2)) );
        if(life < 0)
        {
            alive = false;
        }
    }

    public void draw(Graphics2D g)
    {
        g.setColor(color);
        Font newFont = new Font("Verdana", Font.BOLD, damage);
        g.setFont(newFont);
        g.drawString("" + damage, x,y);
    }

    public boolean getAlive()
    {
        return alive;
    }

}
