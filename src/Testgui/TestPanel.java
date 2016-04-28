package Testgui;

import TimberGame.WiimoteHandler;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Harmen on 28-4-2016.
 */
public class TestPanel extends JPanel {

    private WiimoteHandler wiimoteHandler;

    public TestPanel(){
        wiimoteHandler = new WiimoteHandler();
        new Timer(10, e -> repaint()).start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        wiimoteHandler.drawDebug(g2d);
    }
}
