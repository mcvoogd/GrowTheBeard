package GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Created by kevin on 28/04/2016.
 */
public class FramePanel extends JFrame{

    public static void main(String[] args) {
        new FramePanel();
    }

    public FramePanel()
    {
        System.out.println("Frame constructed");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension(1920, 1080));
        setVisible(true);
        setResizable(false);
        setContentPane(new PaintPanel());
    }
}
