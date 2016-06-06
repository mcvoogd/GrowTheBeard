package nl.avans.a3.main_menu;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;
import nl.avans.a3.event.MainMenuEvent;
import nl.avans.a3.event.ModelEvent;
import nl.avans.a3.event.NewModel;
import nl.avans.a3.game_2.Game_2_Model;
import nl.avans.a3.mvc_handlers.ModelHandler;
import nl.avans.a3.mvc_interfaces.Controller;
import nl.avans.a3.util.WiimoteHandler;

import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainMenuController implements Controller {
    private static final double SCREEN_OFFSET = 400.0;
    private MainMenuModel model;
    private WiimoteHandler wiimoteHandler;
    private AdvancedPlayer player;
    private boolean musicOn = true;

    public MainMenuController(MainMenuModel model, WiimoteHandler wiimoteHandler)
    {
        this.model = model;
        this.wiimoteHandler = wiimoteHandler;
        wiimoteHandler.activateMotionSensing();
        playMusic("res/music/theme_song.mp3");
    }
    @Override
    public void update() {
        model.update();
        if(wiimoteHandler.isWiiMotesConnected()) {
            if (wiimoteHandler.getIsButtonPressed(0, WiimoteHandler.Buttons.KEY_A)) {
                //ModelHandler.instance.onModelEvent(new MainMenuEvent());
                model.onMenuChoose(wiimoteHandler);
            }
            double xPos = (Double.isNaN(wiimoteHandler.getSinglePointer(0).getX())) ? -100 : wiimoteHandler.getSinglePointer(0).getX() * ((1920.0 + SCREEN_OFFSET) / 1024.0) - SCREEN_OFFSET/2;
            double yPos = (Double.isNaN(wiimoteHandler.getSinglePointer(0).getY())) ? -100 : wiimoteHandler.getSinglePointer(0).getY() * ((1080.0 + SCREEN_OFFSET) / 900.0) - SCREEN_OFFSET/2;
            Point2D pointerLocation = new Point2D.Double(xPos, yPos);
            model.setPointer(pointerLocation);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent a) {
        switch (a.getKeyCode())
        {
            case KeyEvent.VK_F : ModelHandler.instance.changeModel(new NewModel(model, new Game_2_Model())); break;
            case KeyEvent.VK_ESCAPE : System.exit(0); break;
            case KeyEvent.VK_H: ModelHandler.instance.onModelEvent(new MainMenuEvent()); break;
            case KeyEvent.VK_G: model.onMenuChoose(wiimoteHandler); break;
            case KeyEvent.VK_W : model.pointToTop(); break;
            case KeyEvent.VK_S : model.pointToBottem(); break;
            case KeyEvent.VK_A : model.pointToLeft(); break;
            case KeyEvent.VK_D : model.pointToRight(); break;
            case KeyEvent.VK_P :
                if(musicOn){
                    musicOn = false;
                    synchronized(this) {
                        if(player != null) {
                            player.stop();
                            player = null;
                        }
                    }
                }
                else{
                    playMusic("res/music/theme_song.mp3");
                    musicOn = true;
                } break;

         }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void onModelEvent(ModelEvent event) {
    }

    public void playMusic(String filename) {
        try {
            try {
                InputStream is = new BufferedInputStream(new FileInputStream(filename));
                player = new AdvancedPlayer(is, FactoryRegistry.systemRegistry().createAudioDevice());
            } catch (IOException e) {
                System.out.println("ERROR - BufferdINput");
            } catch (JavaLayerException e) {
                System.out.println("ERROR - player exception");
            }
            Thread playerThread = new Thread() {
                public void run() {
                    try {
                        player.play(5000);
                    } catch (JavaLayerException e) {
                        System.out.println("ERROR - Play music");
                    }
                }
            };
            playerThread.start();
        } catch (Exception ex) {
            System.out.println("ERROR - Play music");
        }

    }
}
