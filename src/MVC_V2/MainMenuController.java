package MVC_V2;

import java.awt.event.KeyEvent;

public class MainMenuController implements Controller {
    private boolean exit = false;
    private Model model;

    public MainMenuController(MainMenuModel model)
    {
        this.model = model;
    }
    @Override
    public void update() {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent a) {
        switch (a.getKeyCode())
        {
            case KeyEvent.VK_ESCAPE : System.exit(0); break;
            case KeyEvent.VK_A: ModelHandler.instance.onModelEvent(new MainMenuEvent());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void onModelEvent(ModelEvent event) {

    }
}
