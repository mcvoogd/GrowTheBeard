package Testgui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Harmen on 28-4-2016.
 */
public class Main extends JFrame{

    public static void main(String[] args) {
        new Main();
    }

    public Main(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1920, 1080));
        setVisible(true);
        setResizable(false);
        setContentPane(new TestPanel());
    }
}
