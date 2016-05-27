package MVC.main_mvc_classes;

import MVC.game_models.MainMenu;
import MVC.interfaces_listener.ModelEvent;
import MVC.interfaces_listener.ModelInterface;
import MVC.interfaces_listener.ModelListener;
import MVC.interfaces_listener.NewModel;
import nl.avans.a3.WiimoteHandler;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class GameController implements KeyListener, ModelListener {

    private GameModel model;
    private WiimoteHandler handler;
    private Timer refreshTimer;

    public GameController(GameModel model)
    {
        this.model = model;
        this.handler = new WiimoteHandler();
        this.refreshTimer = new Timer(1000/60, e ->
        {
            model.getModelInterface().update();
            model.getViewInterface().update();

        });
        refreshTimer.start();
        //central timer

    }

    public void changeModel(ModelInterface modelinterface)
    {
        this.model = model;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println(" keycode : " + e.getKeyCode());
          switch (e.getKeyChar())
          {
              case ' ' :
                 // System.out.println("space"); break;
                  model.changeModel(new MainMenu());

                  break;
              case 'd' :
              System.exit(0);

          }



        }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void onModelEvent(ModelEvent e) {
        if(e instanceof NewModel) {
            changeModel(((NewModel) e).getNewInterface());
       }
    }
}

