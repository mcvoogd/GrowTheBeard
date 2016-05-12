package Testgui;

import Util.Images;
import TimberGame.WiimoteHandler;

import javax.swing.*;
import java.awt.*;

public class TestPanel extends JPanel {

    private WiimoteHandler wiimoteHandler;
    private Timer timer;

    public TestPanel(WiimoteHandler wiimoteHandler){
        this.wiimoteHandler = wiimoteHandler;
        wiimoteHandler.activateMotionSensing();
        timer = new Timer(10, e -> repaint());
        timer.start();

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawRect(0, 0, 50, 50);
        g2d.drawImage(Images.woodBlock, 0, 0, null);
        //wiimoteHandler.drawDebug(g2d);

    }
}
