package Animation;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SpritesheetExample {
	
	public static void main(String s[])
	{
		JFrame frame = new JFrame("SpriteSheet example");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new SpriteSheetPanel();
		
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
	}
}


class SpriteSheetPanel extends JPanel implements ActionListener {
		
	ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
	String path;
	int index = 0;
	
	
	/* Constructor */
	public SpriteSheetPanel()
	{
		setPreferredSize( new Dimension(640,480));
		
		BufferedImage img = null;
		for(int index = 1; index < 86; index++)
		{
			path = "src/Animation/images/" + index + ".png";
			try {
				img = ImageIO.read(new File(path));
				
			} catch(IOException e) {
				System.out.println(e);
			}
			images.add(img);
		}
		
		Timer timer = new Timer(1000/29, this);
		timer.start();
	}

	// Timer, action performed. Use this to update the world and or objects
	// after that call for a repaint
	public void actionPerformed(ActionEvent arg0)
	{	
		if(!(index == 84))		
			index++;
		repaint();
	}
	
	// Hier alleen tekenen ! Nooit een repaint() !!
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
			g2.drawImage(images.get(index),0, 0, null);
				
	}
}

