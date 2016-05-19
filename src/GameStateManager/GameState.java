package GameStateManager;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import control.GameController;
import control.GameStateManager;

public abstract class GameState {
	protected GameController gameControl;
	public GameState(GameController gameControl){
		this.gameControl = gameControl;
	}
	public abstract void init();
	public abstract void draw(Graphics2D g);
	public abstract void update();
	public abstract void keyPressed(int e);
	public abstract void keyReleased(int e);
}
