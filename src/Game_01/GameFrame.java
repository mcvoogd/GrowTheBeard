package Game_01;
import java.awt.EventQueue;

import javax.swing.JFrame;

public class GameFrame extends JFrame {
	
	public GameFrame() {
		initGameFrame();
	}

	private void initGameFrame() {
		setSize(1920, 1080);
		add(new GameBoard());

		setResizable(false);

		setTitle("Game 01 -- WOODBLOCKS!!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				GameFrame gf = new GameFrame();
				gf.setVisible(true);
			}
		});
	}
}
