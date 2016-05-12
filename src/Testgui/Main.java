package Testgui;

import Util.Images;
import TimberGame.WiimoteHandler;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame{

    private WiimoteHandler wiimoteHandler;

    public static void main(String[] args) {
        new Main();
    }

    public Main(){
        new Images();
        wiimoteHandler = new WiimoteHandler();
        TestPanel panel = new TestPanel(wiimoteHandler);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension(720, 450));
        setVisible(true);
        setResizable(false);
        setContentPane(panel);
    }
}
