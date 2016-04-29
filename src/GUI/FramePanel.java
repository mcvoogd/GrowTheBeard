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
        WiimoteHandler wiimoteHandler = new WiimoteHandler();  // bug-fix, don't ask

        System.out.println("Frame constructed");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension(1920, 1080));
        setVisible(true);
        setResizable(false);
        setContentPane(paintpanel);
    }
}
