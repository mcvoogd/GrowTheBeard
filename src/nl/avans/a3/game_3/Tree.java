package nl.avans.a3.game_3;

import nl.avans.a3.util.EasyTransformer;
import nl.avans.a3.util.ResourceHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

public class Tree {

    private int hitPoints;
    private int x;
    private int y;
    private int width;
    private int height;
    private BufferedImage[] sprites;
    private BufferedImage sprite;
    private double rotation;
    private int maxRotation = 95;
    private boolean fallen;
    private boolean treeVisible;
    private int damage = 0;
    private Timer rotator;
    private Timer alphaTimer;
    private Timer treeFlashTimer;
    private boolean leftOrRight;
    private boolean fading = false;
    private final int MAX_HIT_POINTS = 250;
    private BufferedImage image;
    private BufferedImage trunk;
    private int xOffset;
    private float alpha = 1.0f;
    private  int count = 0;
    private boolean switched = false;
    private AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
    private ArrayList<DamageNumber> damageNumbers = new ArrayList<>();

    public Tree(int x, int y, boolean leftOrRight)
    {
        // TODO magic numbers al over the place
        hitPoints = MAX_HIT_POINTS;
        this.x = x;
        this.y = y;
        width = 100;
        height = 1080;
        rotation = 0;
        this.leftOrRight = leftOrRight;
        sprites = new BufferedImage[6];
        if(!leftOrRight) {
            xOffset = 7;
            image = ResourceHandler.getImage("res/images_game3/tree_right.png");
            if (image != null) for(int i = 0; i < 6; i++){
                sprites[i] = image.getSubimage(211 * i, 0, 211, 852);
            }
        }else
        {
            xOffset = -7;
            image = ResourceHandler.getImage("res/images_game3/tree_left.png");
            if (image != null) for(int i = 0; i < 6; i++){
                sprites[i] = image.getSubimage(185 * i, 0, 185, 852);
            }
        }

        trunk = sprites[0];
        changeSprite(sprites[1]);
        alphaTimer = new Timer(100, e -> {if(alpha > 0.05f) alpha -= 0.05f;});
        treeVisible = true;
        treeFlashTimer = new Timer(2000/8, e ->{
            treeVisible = !treeVisible;
            switched = true;
            if(treeVisible) {
                alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
            }
            else
            {
                alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.0f);
            }
        }
        );
    }

    public void update()
    {
        Iterator<DamageNumber> ir = damageNumbers.iterator();
        while (ir.hasNext())
        {
            DamageNumber temp = ir.next();
            if(!temp.getAlive())
            {
                ir.remove();
            }
        }
        if(hitPoints <= 200) // first cut
        {
            changeSprite(sprites[2]);
        }
        if(hitPoints <= 150) // second cut
        {
            changeSprite(sprites[3]);
        }
        if(hitPoints <= 100) // three cut
        {
            changeSprite(sprites[4]);
        }
        if(hitPoints <= 0) // boom falls.
        {
            changeSprite(sprites[5]);
            if(!fallen){
                drawFallingAnimation(leftOrRight);
                fallen = true;
            }
        }
        if(rotation > maxRotation || rotation < -maxRotation)
        {
            rotator.stop();}
    }

    public void draw(Graphics2D g)
    {
        // TODO magic numbers again
        if(!fallen) {
            if(treeFlashTimer.isRunning()) {
                if(treeVisible && switched) {
                    count++;
                    switched = false;
                }
                else if(!treeVisible && switched)
                {
                    count++;
                    switched = false;
                }
                AlphaComposite old = (AlphaComposite) g.getComposite();
                g.setComposite(alcom);
                g.drawImage(sprite, EasyTransformer.rotateAroundCenterWithOffset(sprite, rotation, 0, 375, x + xOffset, y - 60), null);
                g.setComposite(old);
                if(count == 6)
                {
                    treeFlashTimer.stop();
                    count = 0;
                    treeVisible = true;
                    fading = false;
                }
            }
            else {
                g.drawImage(sprite, EasyTransformer.rotateAroundCenterWithOffset(sprite, rotation, 0, 375, x + xOffset, y - 60), null);

            }
        }
        else {
           if(rotation > maxRotation || rotation < -maxRotation){
               alphaTimer.start();
           }
            //alphacomposite to fade away!
            AlphaComposite old = (AlphaComposite) g.getComposite();
            AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
            g.setComposite(alcom);
            g.drawImage(sprite, EasyTransformer.rotateAroundCenterWithOffset(sprite, rotation, 0, 375, x+ xOffset, y - 60), null);
            g.setComposite(old);

            if(alpha < 0.5f)
            {
                alphaTimer.stop();
                resetTree();
            }
        }
        g.drawImage(trunk, x, y + 180, null);
    }

    private void changeSprite(BufferedImage image)
    {
        this.sprite = image;
    }

    public void resetTree()
    {
        if(!treeFlashTimer.isRunning())
        {
            treeFlashTimer.start();
        }
        rotation = 0;
        fallen = false;
        alpha = 1.0f;
        fading = true;
        hitPoints = MAX_HIT_POINTS;
        changeSprite(sprites[1]);
        damageNumbers.clear();
    }

    public void damageTree(int damage)
    {
        if(!fading) {
            if (!fallen) {
                this.hitPoints -= damage;
                Color hitColor = Color.RED;
                if (damage < 10) {
                    hitColor = new Color(255, 250, 30);
                } else if (damage < 25) {
                    hitColor = new Color(255, 120, 30);
                }
                if (damage < 40) {
                    hitColor = new Color(255, 73, 29);
                }
                if (damage >= 50) {
                    hitColor = new Color(240, 185, 36);
                }
                if (leftOrRight) {
                    addDamageNumber(x + 50, y + 800, damage, hitColor);
                } else {
                    addDamageNumber(x - 50, y + 800, damage, hitColor);
                }
                if (hitPoints < 0) {
                    hitPoints = 0;
                }
                this.damage = damage;
            }
        }
    }

    public boolean getIsFallen()
    {
        return fallen;
    }

    public boolean isDamaged()
    {
        return damage > 0;
    }


    // TODO an enum would be better for this
    /**
     * read this to see what the boolean is used for.
     * @param isLeft: true for left, false for right.
     */
    public void drawFallingAnimation(boolean isLeft)
    {
            rotator = new Timer(1000/60, e->
        {
            if(isLeft)
            { // TODO change this with rotation += Math.ceiling(rotation/10)*0.2
                if(rotation < maxRotation)
                {
                    if(rotation >= 0 && rotation <= 10)
                    {
                        rotation += 0.2;
                    }else
                    if(rotation >= 10 && rotation <= 20)
                    {
                        rotation += 0.4;
                    }else
                    if(rotation >= 20 && rotation <= 30)
                    {
                        rotation += 0.6;
                    }else
                    if(rotation > 30 && rotation <= 40)
                    {
                        rotation += 0.8;
                    }else
                    if(rotation > 40 && rotation <= 50)
                    {
                        rotation += 1.2;
                    }else
                    if(rotation > 50 && rotation <= 60)
                    {
                        rotation += 1.8;
                    }else
                    if(rotation >= 60 && rotation <= 70)
                    {
                        rotation += 2.0;
                    }else
                    if(rotation >= 70 && rotation <= 80)
                    {
                        rotation += 2.2;
                    }else
                    if(rotation > 80 && rotation <= maxRotation)
                    {
                        rotation += 2.4;
                    }
                }
            }else
            {
                if(rotation > -maxRotation)
                    if(rotation <= 0 && rotation >= -10)
                    {
                        rotation -= 0.2;
                    }else
                    if(rotation <= -10 && rotation >= -20)
                    {
                        rotation -= 0.4;
                    }else
                    if(rotation <= -20 && rotation >= -30)
                    {
                        rotation -= 0.6;
                    }else
                    if(rotation <= -30 && rotation >= -40)
                    {
                        rotation -= 0.8;
                    }else
                    if(rotation <= -40 && rotation >= -50)
                    {
                        rotation -= 1.2;
                    }else
                    if(rotation < -50 && rotation >= -60)
                    {
                        rotation -= 1.8;
                    }else
                    if(rotation < -60 && rotation >= -70)
                    {
                        rotation -= 2.0;
                    }else
                    if(rotation < -70 && rotation >= -80)
                    {
                        rotation -= 2.2;
                    }else
                    if(rotation < -80 && rotation >= -maxRotation)
                    {
                        rotation -= 2.4;
                    }
            }

        });
        rotator.start();
    }

    public void addDamageNumber(int x, int y, int damage, Color color)
    {
        damageNumbers.add(new DamageNumber(x, y, damage, color, leftOrRight));
    }

    public ArrayList<DamageNumber> getDamageNumbers()
    {
        return damageNumbers;
    }
}
