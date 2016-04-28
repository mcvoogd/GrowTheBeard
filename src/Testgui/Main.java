package Testgui;

import TimberGame.WiimoteHandler;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame{

    private WiimoteHandler wiimoteHandler;

    public static void main(String[] args) {
        new Main();
    }

    public Main(){
        wiimoteHandler = new WiimoteHandler();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1920, 1080));
        setVisible(true);
        setResizable(false);
        setContentPane(new TestPanel(wiimoteHandler));
    }
}
