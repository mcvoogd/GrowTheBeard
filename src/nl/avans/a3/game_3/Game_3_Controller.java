package nl.avans.a3.game_3;

import nl.avans.a3.event.ModelEvent;
import nl.avans.a3.event.NewModel;
import nl.avans.a3.main_menu.MainMenuModel;
import nl.avans.a3.mvc_handlers.ModelHandler;
import nl.avans.a3.mvc_interfaces.Controller;
import nl.avans.a3.party_mode_handler.PartyModeHandler;
import nl.avans.a3.util.SoundPlayer;
import nl.avans.a3.util.WiimoteHandler;

import java.awt.event.KeyEvent;

public class Game_3_Controller implements Controller{

    private Game_3_Model gameModel;
    private WiimoteHandler wiimoteHandler;
    private SoundPlayer chopSoundPlayer;
    private SoundPlayer backgroundMusic;

    public Game_3_Controller(Game_3_Model gameModel, WiimoteHandler wiimoteHandler){
        this.gameModel = gameModel;
        this.wiimoteHandler = wiimoteHandler;
        this.wiimoteHandler.activateMotionSensing();
        chopSoundPlayer = new SoundPlayer(new String[]{
                "res/music/game3/chop2.wav",
                "res/music/game3/chop3.wav",
                "res/music/game3/chop5.wav",
                "res/music/game3/chop6.wav",
                "res/music/game3/chop7.wav",
                "res/music/game3/chop8.wav",
                "res/music/game3/chop9.wav",
                "res/music/game3/chop10.wav"
        });
        backgroundMusic = new SoundPlayer("res/music/game3/nature.wav");
        backgroundMusic.loop(30);
    }

    @Override
    public void update() {
        gameModel.update();

        if (wiimoteHandler != null && wiimoteHandler.isWiiMotesConnected()) {
            float max1 = wiimoteHandler.getMax(0);
            float max2 = wiimoteHandler.getMax(1);

            if(gameModel.getisPreScreen())
            {
                if(wiimoteHandler.getIsButtonPressed(0, WiimoteHandler.Buttons.KEY_A))
                {
                    gameModel.setPreScreen(false);
                }
            }else if(gameModel.getInGame()) {
                if (wiimoteHandler.getPeakValue(0)[0]) {
                    if (gameModel.getHitPlayer(1)) {
                        gameModel.damageTree(0, (int) (max1 * 10), 1);
                        gameModel.setHitPlayer(1, false);
                        gameModel.startHit(1);
                        chopSoundPlayer.playRandomOnce();
                    }
                }

                if (wiimoteHandler.getPeakValue(1)[0]) {
                    if (gameModel.getHitPlayer(2)) {
                        gameModel.damageTree(1, (int) (max2 * 10), 2);
                        gameModel.setHitPlayer(2, false);
                        gameModel.startHit(2);
                        chopSoundPlayer.playRandomOnce();

                    }
                }
                float pitch1 = wiimoteHandler.getPitch(0);
                if (pitch1 < -80 && pitch1 > -100) {
                    gameModel.setHitPlayer(1, true);
                }

                float pitch2 = wiimoteHandler.getPitch(1);
                if (pitch2 < -80 && pitch2 > -100) {
                    gameModel.setHitPlayer(2, true);
                }
                //NEEDS TO BE DUPLICATE.
            }
            else
            {
                if (PartyModeHandler.getCurrentMode() == PartyModeHandler.Mode.CHOOSE_PARTY) {
                    if (wiimoteHandler.getIsButtonPressed(0, WiimoteHandler.Buttons.KEY_A) || wiimoteHandler.getIsButtonPressed(1, WiimoteHandler.Buttons.KEY_A)) {
                        backgroundMusic.stop();
                        PartyModeHandler.notifyNextGame();
                    }
                } else {
                    if (wiimoteHandler.getIsButtonPressed(0, WiimoteHandler.Buttons.KEY_A) || wiimoteHandler.getIsButtonPressed(1, WiimoteHandler.Buttons.KEY_A)) {
                        backgroundMusic.stop();
                        ModelHandler.instance.changeModel(new NewModel(null, new MainMenuModel()));
                    }
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            System.exit(0);
        }
//        if(e.getKeyCode() == KeyEvent.VK_P){
//            gameModel.startHit(1);
//            gameModel.damageTree(0, 25, 1);
//            chopSoundPlayer.playRandomOnce();
//        }
//        if(e.getKeyCode() == KeyEvent.VK_C){
//            gameModel.startHit(2);
//            gameModel.damageTree(1, 25, 2);
//            chopSoundPlayer.playRandomOnce();
//        }
//        if(e.getKeyCode() == KeyEvent.VK_S)
//        {
//            gameModel.damageTree(0, 25, 1);
//            gameModel.damageTree(1, 25, 2);
//        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void onModelEvent(ModelEvent event) {

    }
}
