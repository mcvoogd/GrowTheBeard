package MVC_V2;

import java.awt.event.KeyEvent;

class BootController implements Controller {

    private boolean aPressed = false;
    private boolean bPressed = false;
    private BootModel bootModel;

    public BootController(BootModel model)
    {
        this.bootModel = model;
    }
    @Override
    public void onModelEvent(ModelEvent event) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent a) {
        switch (a.getKeyCode())
        {
            case KeyEvent.VK_A : aPressed = true; break;
            case KeyEvent.VK_B : bPressed = true; break;
            case KeyEvent.VK_SPACE : aPressed = bPressed = true;;break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void update() {
        if(aPressed && bPressed)
        {
            bootModel.onABPressed();
        }
        aPressed = bPressed = false;
    }
}
