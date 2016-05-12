package Balanceboard;

import javax.swing.*;
import java.awt.*;

/**
 * Created by kevin on 12/05/2016.
 */
public class paintPanel extends JPanel {

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.fillRect(50,50, getWidth()-100, getHeight()-100);
        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("Purisa", Font.PLAIN, 30));
        g2d.drawString("Balanceboard statistics", 70, 80);
    }
}
