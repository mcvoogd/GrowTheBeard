package nl.avans.a3.winScreen;

import nl.avans.a3.mvc_interfaces.Model;
import nl.avans.a3.util.Beard;

public class WinScreen_Model implements Model{

    private ConfettiCanon confettiCanon;
    private final int WIDTH = 1920;
    private final int HEIGHT = 1080;

    public WinScreen_Model(){
        if(Beard.beardPlayer1 > Beard.beardPlayer2){
            confettiCanon = new ConfettiCanon((WIDTH/2) - (1315/8) - 350, HEIGHT - 75);
        }else{
            confettiCanon = new ConfettiCanon((WIDTH/2) - (1315/8) + 650, HEIGHT - 75);
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
        confettiCanon.update();
    }

    @Override
    public void close() {

    }

    public ConfettiCanon getConfettiCanon() {
        return confettiCanon;
    }
}
