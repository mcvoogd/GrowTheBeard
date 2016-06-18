package nl.avans.a3.winScreen;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Harmen on 18-6-2016.
 */
public class ConfettiCanon {

    private ArrayList<Confetti> confettis;
    private int counter, x, y;
    private final double STARTANGLE = Math.PI + (Math.PI/20 * 8);

    public ConfettiCanon(int x, int y){
        confettis = new ArrayList<>();
        this.x = x;
        this.y = y;
    }

    public void update(){
        counter++;
        if(counter > 10){
            for (int i = 0; i <= 5; i++) {
                confettis.add(new Confetti(x + 20 ,y, STARTANGLE + (Math.PI/20) * i));
            }
            counter = 0;
        }

        Iterator<Confetti> iterator = confettis.iterator();
        while (iterator.hasNext()){
            Confetti c = iterator.next();
            c.update();
            if(c.getY() > 1000 + (Math.random() * 20)){
                c.setStop(true);
            }
            if(c.getLife() > 200){
                iterator.remove();
            }
        }
    }

    public void draw(Graphics2D g){
        for (Confetti c: confettis) {
            c.draw(g);
        }
    }
}
