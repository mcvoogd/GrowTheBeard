package TimberGame;

import wiiusej.WiiUseApiManager;
import wiiusej.Wiimote;
import wiiusej.wiiusejevents.physicalevents.*;
import wiiusej.wiiusejevents.utils.WiimoteListener;
import wiiusej.wiiusejevents.wiiuseapievents.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.EnumMap;

public class WiimoteHandler{
    public enum Buttons{
        KEY_HOME, KEY_1, KEY_2, KEY_A, KEY_B, KEY_MINUS, KEY_PLUS, KEY_UP, KEY_RIGHT, KEY_DOWN, KEY_LEFT
    }
    
    private Wiimote[] wiimotes;
    private ArrayList<EnumMap<Buttons, Boolean>> pressedButtons = new ArrayList<>(4);
    private ArrayList<EnumMap<Buttons, Boolean>> heldButtons = new ArrayList<>(4);

    private boolean[] connectedNunchucks = new boolean[4];

    public WiimoteHandler(){
        SearchWiimotes();
    }

    public static void main(String[] args){
        new WiimoteHandler();
    }

    public void SearchWiimotes(){
        wiimotes = WiiUseApiManager.getWiimotes(4, true);
        for(int i = 0; i < wiimotes.length; i++){
            boolean[] bool = new boolean[4];
            for(int j = 0; j < bool.length; j++){
                bool[j] = false;
            }
            bool[i] = true;
            wiimotes[i].setLeds(bool[0], bool[1], bool[2], bool[3]);
            final int finalI = i;  // Java anonymous classes can't handle swag, but can handle final variables.
            wiimotes[i].addWiiMoteEventListeners(new WiimoteListener(){
                @Override
                public void onButtonsEvent(WiimoteButtonsEvent e){  // godfuckingdamnit why can't this be easier, jesus fuck.
                    if(e.isButtonHomeJustPressed())
                        setButton(finalI, Buttons.KEY_HOME, true);
                    if(e.isButtonHomeJustReleased())
                        setButton(finalI, Buttons.KEY_HOME, false);
                    if(e.isButtonOneJustPressed())
                        setButton(finalI, Buttons.KEY_1, true);
                    if(e.isButtonOneJustReleased())
                        setButton(finalI, Buttons.KEY_1, false);
                    if(e.isButtonTwoJustPressed())
                        setButton(finalI, Buttons.KEY_2, true);
                    if(e.isButtonTwoJustReleased())
                        setButton(finalI, Buttons.KEY_2, false);
                    if(e.isButtonAJustPressed())
                        setButton(finalI, Buttons.KEY_A, true);
                    if(e.isButtonAJustReleased())
                        setButton(finalI, Buttons.KEY_A, false);
                    if(e.isButtonBJustPressed())
                        setButton(finalI, Buttons.KEY_B, true);
                    if(e.isButtonBJustReleased())
                        setButton(finalI, Buttons.KEY_B, false);
                    if(e.isButtonMinusJustPressed())
                        setButton(finalI, Buttons.KEY_MINUS, true);
                    if(e.isButtonMinusJustReleased())
                        setButton(finalI, Buttons.KEY_MINUS, false);
                    if(e.isButtonPlusJustPressed())
                        setButton(finalI, Buttons.KEY_PLUS, true);
                    if(e.isButtonPlusJustReleased())
                        setButton(finalI, Buttons.KEY_PLUS, false);
                    if(e.isButtonUpJustPressed())
                        setButton(finalI, Buttons.KEY_UP, true);
                    if(e.isButtonUpJustReleased())
                        setButton(finalI, Buttons.KEY_UP, false);
                    if(e.isButtonRightJustPressed())
                        setButton(finalI, Buttons.KEY_RIGHT, true);
                    if(e.isButtonRightJustReleased())
                        setButton(finalI, Buttons.KEY_RIGHT, false);
                    if(e.isButtonDownJustPressed())
                        setButton(finalI, Buttons.KEY_DOWN, true);
                    if(e.isButtonDownJustReleased())
                        setButton(finalI, Buttons.KEY_DOWN, false);
                    if(e.isButtonLeftJustPressed())
                        setButton(finalI, Buttons.KEY_LEFT, true);
                    if(e.isButtonLeftJustReleased())
                        setButton(finalI, Buttons.KEY_LEFT, false);
                }

                @Override
                public void onIrEvent(IREvent irEvent){
                }

                @Override
                public void onMotionSensingEvent(MotionSensingEvent motionSensingEvent){
                }

                @Override
                public void onExpansionEvent(ExpansionEvent e){
                    if(e instanceof NunchukEvent){
                        NunchukEvent ne = (NunchukEvent) e;
                        System.out.println(ne.getNunchukJoystickEvent().getAngle());
                        System.out.println(ne.getNunchukJoystickEvent().getMagnitude());
                    }
                }

                @Override
                public void onStatusEvent(StatusEvent statusEvent){
                }

                @Override
                public void onDisconnectionEvent(DisconnectionEvent disconnectionEvent){
                }

                @Override
                public void onNunchukInsertedEvent(NunchukInsertedEvent e){
                    setNunchuckConnected(e.getWiimoteId(), true);
                }

                @Override
                public void onNunchukRemovedEvent(NunchukRemovedEvent e){
                    setNunchuckConnected(e.getWiimoteId(), false);
                }

                @Override
                public void onGuitarHeroInsertedEvent(GuitarHeroInsertedEvent guitarHeroInsertedEvent){
                }

                @Override
                public void onGuitarHeroRemovedEvent(GuitarHeroRemovedEvent guitarHeroRemovedEvent){
                }

                @Override
                public void onClassicControllerInsertedEvent(ClassicControllerInsertedEvent classicControllerInsertedEvent){
                }

                @Override
                public void onClassicControllerRemovedEvent(ClassicControllerRemovedEvent classicControllerRemovedEvent){
                }
            });
        }
    }
    
    public void drawDebug(Graphics2D g){
        g.setColor(new Color(0, 0, 0, 63));
        g.fillRect(0, 0, 200*wiimotes.length, 200);
    }
    
    private void setButton(int wiimoteID, Buttons button, boolean value){
        /**
         * since this also writes false to pressedButtons, it may happen that at too short presses, no button press will be registered.
         * TODO: testing needed
         */
        pressedButtons.get(wiimoteID).put(button, value);
        heldButtons.get(wiimoteID).put(button, value);
    }

    /**
     * Returns if the button of wiimote has been pressed.
     * 
     * @param wiimoteID index of list of connected wiimotes
     * @param button enum of button to return the value of
     * @return the stored value for the given wiimote and corresponding button
     */
    public boolean getIsButtonPressed(int wiimoteID, Buttons button){
        // store current value in var, then change value to false so the button has been read
        boolean returnVal = pressedButtons.get(wiimoteID).get(button);
        pressedButtons.get(wiimoteID).put(button, false);
        return returnVal;
    }

    /**
     * Returns if the button of wiimote is being held down.
     * 
     * @param wiimoteID index of list of connected wiimotes
     * @param button enum of button to return the value of
     * @return the stored value for the given wiimote and corresponding button
     */
    public boolean getIsButtonDown(int wiimoteID, Buttons button){
        return heldButtons.get(wiimoteID).get(button);
    }

    private void setNunchuckConnected(int nunchuck, boolean value){
        connectedNunchucks[nunchuck] = value;
    }

    /**
     * Returns true if there is a nunchuck connected.
     * 
     * @param wiimoteID index of list of connected wiimotes
     * @return true if a nunchuck connected is
     */
    public boolean isNunchuckConnected(int wiimoteID){
        return connectedNunchucks[wiimoteID];
    }
}
