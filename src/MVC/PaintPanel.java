package MVC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PaintPanel extends JPanel {
    private ViewInterface viewInterface;
    
    public PaintPanel(ViewInterface viewInterface)
    {
        this.viewInterface = viewInterface;

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                int keycode = e.getKeyCode();
                switch (keycode)
                {
                    case KeyEvent.VK_SPACE :  setViewInterface(new MainMenuView());
                        System.out.println("test"); break;
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        setFocusable(true);
        requestFocus();
    }

    public void setViewInterface(ViewInterface newViewInterface){
        this.viewInterface = newViewInterface;
    }
    
    public void paintComponent(Graphics g2){
        super.paintComponent(g2);

        Graphics2D g = (Graphics2D) g2;
        RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHints(renderingHints);
        g.scale(getWidth()/1920.0, getHeight()/1080.0);

        if(viewInterface != null)
            viewInterface.draw(g);

        // fixes stutter on Linux systems
        Toolkit.getDefaultToolkit().sync();
    }
}
