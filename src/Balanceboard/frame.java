package Balanceboard;

import javax.swing.*;

/**
 * Created by kevin on 12/05/2016.
 */
public class frame extends JFrame {
    private int framewidth = 1920;
    private int frameheight = 1080;


    public static void main(String[] args) {
        new frame();
    }
    public frame()
    {
        super("Balanceboard statistics");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(framewidth, frameheight);
        setResizable(true);
        setContentPane(new paintPanel());
    }



}
