package Test;

import nl.avans.a3.util.WiimoteHandler;

import javax.swing.*;
import java.awt.*;

public class PaintDebug extends JFrame{
    public static void main(String[] args){
        new PaintDebug();
    }

    public PaintDebug(){
        WiimoteHandler w;
        System.setProperty("sun.java2d.opengl", "true");  // should enable hardware-acceleration

        w = new WiimoteHandler();
        w.searchWiimotes();
        w.activateMotionSensing();

        try{
            Thread.sleep(5000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }

        setTitle("Test window");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(800, 600);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        setContentPane(new GraphicsWindow(w));
        Timer timer = new Timer(1000 / 60, e -> this.repaint());
        timer.start();
    }

    private class GraphicsWindow extends JPanel{
        WiimoteHandler w;
        public GraphicsWindow(WiimoteHandler w){
            this.w = w;

        }

        @Override
        public void paintComponent(Graphics graphics){
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics;
            w.drawDebug(g);
            g.setColor(Color.RED);
            g.fillOval((int)( w.getSinglePointer(0).getX() * ((1920.0 + 400) / 1024.0)) - (400 / 2), (int) ( w.getSinglePointer(0).getY() * ((1080.0 + 400) / 900.0)) - (400 / 2), 10, 10);
            // fixes stutter on Linux systems
            Toolkit.getDefaultToolkit().sync();
        }
    }
}

