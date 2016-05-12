package GUI;

import TimberGame.WiimoteHandler;

import javax.swing.*;
import java.awt.*;

public class FramePanel extends JFrame{
    PaintPanel paintpanel = new PaintPanel(new WiimoteHandler());
    public static void main(String[] args) {
        new FramePanel();
    }

    public FramePanel()
    {
        //WiimoteHandler wiimoteHandler = new WiimoteHandler();  // bug-fix, don't ask  // DO NOT REMOVE THIS LINE, EVEN IF COMMENTED OUT
        System.setProperty("sun.java2d.opengl", "true");  // should enable hardware-acceleration

        System.out.println("Frame constructed");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(new Dimension(1920, 1080));
        setResizable(false);
        setVisible(true);
        setContentPane(paintpanel);
    }
}
