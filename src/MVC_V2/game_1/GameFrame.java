package MVC_V2.game_1;
import MVC_V2.util.WiimoteHandler;

import java.awt.EventQueue;

import javax.swing.JFrame;

class GameFrame extends JFrame {
	
	private GameFrame() {
		initGameFrame();
	}

	private void initGameFrame() {
		setSize(1920, 1080);
		add(new GameBoard(new WiimoteHandler()));

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
