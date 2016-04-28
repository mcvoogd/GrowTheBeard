package Testgui;

import TimberGame.WiimoteHandler;

import javax.swing.*;
import java.awt.*;

public class TestPanel extends JPanel {

    private WiimoteHandler wiimoteHandler;

    public TestPanel(WiimoteHandler wiimoteHandler){
        this.wiimoteHandler = wiimoteHandler;
        new Timer(10, e -> repaint()).start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        wiimoteHandler.drawDebug(g2d);
    }
}
