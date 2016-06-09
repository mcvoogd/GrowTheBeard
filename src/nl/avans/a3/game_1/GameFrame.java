package nl.avans.a3.game_1;
import nl.avans.a3.util.WiimoteHandler;

import java.awt.EventQueue;

import javax.swing.*;

class GameFrame extends JFrame {
	
	private GameFrame() {
		initGameFrame();
	}

	private void initGameFrame() {
		setSize(1920, 1080);
		add(new GameBoard(new WiimoteHandler()));

		setResizable(false);

		setTitle("Game 01 -- WOODBLOCKS!!");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
            GameFrame gf = new GameFrame();
            gf.setVisible(true);
        });
	}
}
