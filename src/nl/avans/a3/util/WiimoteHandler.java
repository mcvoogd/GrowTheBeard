package nl.avans.a3.util;

import wiiusej.WiiUseApiManager;
import wiiusej.Wiimote;
import wiiusej.values.GForce;
import wiiusej.values.IRSource;
import wiiusej.values.Orientation;
import wiiusej.wiiusejevents.physicalevents.*;
import wiiusej.wiiusejevents.utils.WiimoteListener;
import wiiusej.wiiusejevents.wiiuseapievents.*;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedList;

public class WiimoteHandler {
    private float[] battery = new float[4];
    private boolean[] isMotionSensingActive = new boolean[4];

    public void setBattery(int wiimoteID, float battery){
        this.battery[wiimoteID] = battery;
    }

    public enum Buttons{
        KEY_HOME, KEY_1, KEY_2, KEY_A, KEY_B, KEY_MINUS, KEY_PLUS, KEY_UP, KEY_RIGHT, KEY_DOWN, KEY_LEFT
    }
    
    private Wiimote[] wiimotes;
    private JoystickEvent[] joystickEvents;
    private IRSource[][] irSources = new IRSource[4][5];
    private ArrayList<EnumMap<Buttons, Boolean>> pressedButtons = new ArrayList<>();
    private ArrayList<EnumMap<Buttons, Boolean>> heldButtons = new ArrayList<>();
    private GForce[] gForce = new GForce[4];
    private Orientation[] orientation = new Orientation[4];

    private float[][] oldValue = new float[4][3];
    private boolean[] connectedNunchucks = new boolean[4];
    private boolean[][] peakValue = new boolean[4][3];
    
    private LinkedList<LinkedList<GForce>> gForceWiimoteList = new LinkedList<>();
    private LinkedList<LinkedList<Orientation>> orientationWiimoteList = new LinkedList<>();
    private LinkedList<LinkedList<GForce>> gForceNunchuckList = new LinkedList<>();
    private LinkedList<LinkedList<Orientation>> orientationNunchuckList = new LinkedList<>();
    
    public WiimoteHandler(){
        
    }

    /**
     * Searches for wiimotes.
     */
    public void searchWiimotes(){
        joystickEvents = new JoystickEvent[4];
        wiimotes = WiiUseApiManager.getWiimotes(4, true);
        for(int i = 0; i < wiimotes.length; i++){
            //wiimotes[i].setTimeout();
            gForceWiimoteList.add(i, new LinkedList<>());
            orientationWiimoteList.add(i, new LinkedList<>());
            pressedButtons.add(new EnumMap<>(Buttons.class));
            gForceNunchuckList.add(i, new LinkedList<>());
            orientationNunchuckList.add(i, new LinkedList<>());
            heldButtons.add(new EnumMap<>(Buttons.class));
            boolean[] bool = new boolean[4];
            for(int j = 0; j < bool.length; j++){
                bool[j] = false;
            }
            bool[i] = true;
            wiimotes[i].setLeds(bool[0], bool[1], bool[2], bool[3]);
            wiimotes[i].setAlphaSmoothingValue(1);
            wiimotes[i].activateIRTRacking();
            wiimotes[i].activateMotionSensing();
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
                public void onIrEvent(IREvent e){
                    setIrSources(finalI, e.getIRPoints());
                }

                @Override
                public void onMotionSensingEvent(MotionSensingEvent e){
                    setgForce(finalI, e.getGforce());
                    storeGForce(finalI, e.getGforce());
                    setOrientation(finalI, e.getOrientation());
                    storeOrientation(finalI, e.getOrientation());
                    setPeakValue(finalI);

                }

                @Override
                public void onExpansionEvent(ExpansionEvent e){
                    if(e instanceof NunchukEvent){
                        NunchukEvent ne = (NunchukEvent) e;
                        ButtonsEvent be = ne.getButtonsEvent();
                        JoystickEvent joystickEvent = ne.getNunchukJoystickEvent();
                        storeNunchuckJoystick(finalI, joystickEvent);
                        MotionSensingEvent me = ne.getNunchukMotionSensingEvent();
                        storeNunchuckGForce(finalI, me.getGforce());
                        storeNunchuckOrentation(finalI, me.getOrientation());
                    }
                }

                @Override
                public void onStatusEvent(StatusEvent e){
                    setBattery(finalI, e.getBatteryLevel());
                }

                @Override
                public void onDisconnectionEvent(DisconnectionEvent e){
                    disconnect(finalI);
                }

                @Override
                public void onNunchukInsertedEvent(NunchukInsertedEvent e){
                    setNunchuckConnected(finalI, true);
                }

                @Override
                public void onNunchukRemovedEvent(NunchukRemovedEvent e){
                    setNunchuckConnected(finalI, false);
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

    private void setIrSources(int wiimoteID, IRSource[] irPoints){
        this.irSources[wiimoteID] = irPoints;
    }

    private void disconnect(int wiimoteID){
        wiimotes[wiimoteID] = null;
    }

    private void setgForce(int wiimoteID, GForce gForce){
        this.gForce[wiimoteID] = gForce;
    }

    private void setPeakValue(int wiimoteID){
        float[] newValues = new float[3];
        newValues[0] = gForceWiimoteList.get(wiimoteID).getLast().getX();
        newValues[1] = gForceWiimoteList.get(wiimoteID).getLast().getY();
        newValues[2] = gForceWiimoteList.get(wiimoteID).getLast().getZ();
        for(int i = 0; i < wiimotes.length; i++) {
            peakValue[wiimoteID][i] = (oldValue[wiimoteID][i] - newValues[i]) > 0.5;
            oldValue[wiimoteID][i] = newValues[i];
        }
    }
    
    private void setOrientation(int wiimoteID, Orientation orientation){
        this.orientation[wiimoteID] = orientation;
    }
    
    private void storeGForce(int wiimoteID, GForce gForce){
        gForceWiimoteList.get(wiimoteID).add(gForce);
    }
    
    private void storeOrientation(int wiimoteID, Orientation orientation){
        orientationWiimoteList.get(wiimoteID).add(orientation);
    }

    private void storeNunchuckGForce(int wiiMoteID, GForce gForce){
        gForceNunchuckList.get(wiiMoteID).add(gForce);
    }

    private void storeNunchuckOrentation(int wiiMoteID, Orientation orientation){
        orientationNunchuckList.get(wiiMoteID).add(orientation);
    }
    private void storeNunchuckJoystick(int wiiMoteID, JoystickEvent joystickEvent){
        joystickEvents[wiiMoteID] = joystickEvent;
    }

    /**
     * Draws sensor data from the wiimotes on screen.
     * 
     * @param g Graphics2D component to draw on
     */
    @SuppressWarnings("Duplicates")
    public void drawDebug(Graphics2D g){
        int width = 250;
        @SuppressWarnings("SuspiciousNameCombination") int height = width;
        int scale = height/20;
        for(int i = 0; wiimotes != null && i < wiimotes.length; i++){
            int offset = width * 2 * i;
            if(isMotionSensingActive[i]){
                Font font = new Font("DejaVu Sans Mono", Font.PLAIN, 10);
                g.setFont(font);
                // draw background boxes
                g.setColor(new Color(0, 0, 0, 127));
                g.fillRect(offset, 0, width, height);
                g.fillRect(offset, height, width, height);
                g.fillRect(offset, height * 2, width, height);

                // draw axes
                g.setColor(new Color(255, 255, 255, 63));
                g.drawLine(offset, height / 2, offset + width, height / 2);
                g.drawLine(offset, height / 2 + height, offset + width, height / 2 + height);
                for(int x = 1; x < gForceWiimoteList.get(i).size(); x++){  // g-force for wiimote
                    LinkedList<GForce> gf = gForceWiimoteList.get(i);
                    g.setColor(new Color(255, 0, 0));
                    g.drawLine(offset + x - 1, Math.round(gf.get(x - 1).getX() * -(height / scale) + height / 2), offset + x, Math.round(gf.get(x).getX() * -(height / scale) + height / 2));
                    g.setColor(new Color(0, 255, 0));
                    g.drawLine(offset + x - 1, Math.round(gf.get(x - 1).getY() * -(height / scale) + height / 2), offset + x, Math.round(gf.get(x).getY() * -(height / scale) + height / 2));
                    g.setColor(new Color(0, 0, 255));
                    g.drawLine(offset + x - 1, Math.round(gf.get(x - 1).getZ() * -(height / scale) + height / 2), offset + x, Math.round(gf.get(x).getZ() * -(height / scale) + height / 2));
                }

                for(int x = 1; x < orientationWiimoteList.get(i).size(); x++){  // orientation for wiimote
                    LinkedList<Orientation> o = orientationWiimoteList.get(i);
                    g.setColor(new Color(255, 255, 0));
                    g.drawLine(offset + x - 1, Math.round(o.get(x - 1).getPitch() / 36 * -(height / scale) + height / 2) + height, offset + x, Math.round(o.get(x).getPitch() / 36 * -(height / scale) + height / 2) + height);
                    g.setColor(new Color(0, 255, 255));
                    g.drawLine(offset + x - 1, Math.round(o.get(x - 1).getRoll() / 36 * -(height / scale) + height / 2) + height, offset + x, Math.round(o.get(x).getRoll() / 36 * -(height / scale) + height / 2) + height);
                }

                if(battery[i] < 0.2f){
                    g.setColor(new Color(255, 0, 0));
                }else{
                    g.setColor(new Color(0, 255, 0));
                }
                g.fillRect(offset + 2, height - 12, Math.round(50 * battery[i]), 10);
                g.setColor(new Color(255, 255, 255));
                g.drawRect(offset + 2, height - 12, 50, 10);

                //Draw IR points
                if(irSources != null){
                    for(int j = 0; j < irSources[i].length; j++){
                        int x = irSources[i][j].getX();
                        int y = irSources[i][j].getY();
                        int rx = irSources[i][j].getRx();
                        int ry = irSources[i][j].getRy();
                        double scaleFactor = (double) height / 1000;
                        int newX = (int) Math.round(x * scaleFactor);
                        int newY = (int) Math.round(y * scaleFactor);
                        int newRx = (int) Math.round(rx * scaleFactor);
                        int newRy = (int) Math.round(ry * scaleFactor);
                        g.setColor(new Color(255, 0, 0));
                        g.fillOval(newX + offset, newY + height * 2, 10, 10);
                        g.setColor(new Color(0, 255, 0));
                        g.fillOval(newRx + offset, newRy + height * 2, 10, 10);
                        g.setColor(new Color(255, 255, 255));
                        g.drawString("X" + j + " = " + irSources[i][j].getX() + " Y" + j + " = " + irSources[i][j].getY(), offset + 2, height * 2 + 10 * (j + 1));
                        g.drawString("RX" + j + " = " + irSources[i][j].getRx() + " RY" + j + " = " + irSources[i][j].getRy(), offset + width/2 + 2, height * 2 + 10 * (j + 1));
                    }
                }

                //draw boxes outlines
                g.setColor(new Color(255, 255, 255, 127));
                g.drawRect(offset, 0, width, height);
                g.drawRect(offset, height, width, height);
                g.drawRect(offset, height * 2, width, height);

                // draw text
                g.setColor(new Color(255, 255, 255));
                g.drawString("Wiimote " + wiimotes[i].getId() + " - G-Force", offset + 2, 10);
                g.drawString("Orientation", offset + 2, height + 10);
                if(gForceWiimoteList.get(i).size() > 0){
                    g.drawString("X = " + gForceWiimoteList.get(i).getLast().getX(), offset + 2, 20);
                    g.drawString("Y = " + gForceWiimoteList.get(i).getLast().getY(), offset + 2, 30);
                    g.drawString("Z = " + gForceWiimoteList.get(i).getLast().getZ(), offset + 2, 40);
                    g.drawString("Pitch = " + orientationWiimoteList.get(i).getLast().getPitch(), offset + 2, height + 20);
                    g.drawString("Roll = " + orientationWiimoteList.get(i).getLast().getRoll(), offset + 2, height + 30);
                }


                float newXValue = gForceWiimoteList.get(i).getLast().getX();
                float newYValue = gForceWiimoteList.get(i).getLast().getY();
                float newZValue = gForceWiimoteList.get(i).getLast().getZ();
                if((oldValue[i][0] - newXValue) > 1.5){
                    g.setColor(new Color(250,0,0));
                    g.fillRect(width*2 * i, height * 2, 50, 200);
                    peakValue[i][0] = true;
                }else{
                    peakValue[i][0] = false;
                }
                oldValue[i][0] = newXValue;

                if((oldValue[i][1] - newYValue) > 1.5){
                    g.setColor(new Color(0, 255, 0));
                    g.fillRect((width*2 * i) + 50, height * 2, 50, 200);
                    peakValue[i][1] = true;
                }else{
                    peakValue[i][1] = false;
                }
                oldValue[i][1] = newYValue;

                if((oldValue[i][2] - newZValue) > 1.5){
                    g.setColor(new Color(0, 0, 255));
                    g.fillRect((width*2 * i) + 100, height * 2, 50, 200);
                    peakValue[i][2] = true;
                }else{
                    peakValue[i][2] = false;
                }
                oldValue[i][2] = newZValue;


                if(isNunchuckConnected(i)){
                    // draw background boxes
                    g.setColor(new Color(0, 0, 0, 127));
                    g.fillRect(offset + width, 0, width, height);
                    g.fillRect(offset + width, height, width, height);
                    g.fillRect(offset + width, height * 2, width, height);
                    g.fillOval(offset + width, height * 2, width, height);

                    // draw axes
                    g.setColor(new Color(255, 255, 255, 63));  // draw 0 line
                    g.drawLine(offset + width, height / 2, offset + width * 2, height / 2);
                    g.drawLine(offset + width, height + height / 2, offset + width * 2, height + height / 2);

                    for(int x = 1; x < gForceNunchuckList.get(i).size(); x++){  // g-force for nunchuck
                        LinkedList<GForce> gf = gForceNunchuckList.get(i);
                        g.setColor(new Color(255, 0, 0));
                        g.drawLine(offset + x - 1 + width, Math.round(gf.get(x - 1).getX() * 4 * -(height / scale) + height / 2), offset + x + width, Math.round(gf.get(x).getX() * 4 * -(height / scale) + height / 2));
                        g.setColor(new Color(0, 255, 0));
                        g.drawLine(offset + x - 1 + width, Math.round(gf.get(x - 1).getY() * 4 * -(height / scale) + height / 2), offset + x + width, Math.round(gf.get(x).getY() * 4 * -(height / scale) + height / 2));
                        g.setColor(new Color(0, 0, 255));
                        g.drawLine(offset + x - 1 + width, Math.round(gf.get(x - 1).getZ() * 4 * -(height / scale) + height / 2), offset + x + width, Math.round(gf.get(x).getZ() * 4 * -(height / scale) + height / 2));
                    }

                    for(int x = 1; x < orientationNunchuckList.get(i).size(); x++){  // orientation for nunchuck
                        LinkedList<Orientation> o = orientationNunchuckList.get(i);
                        g.setColor(new Color(255, 255, 0));
                        g.drawLine(offset + x - 1 + width, Math.round(o.get(x - 1).getPitch() / 36 * -(height / scale) + height / 2 + height), offset + x + width, Math.round(o.get(x).getPitch() / 36 * -(height / scale) + height / 2 + height));
                        g.setColor(new Color(0, 255, 255));
                        g.drawLine(offset + x - 1 + width, Math.round(o.get(x - 1).getRoll() / 36 * -(height / scale) + height / 2 + height), offset + x + width, Math.round(o.get(x).getRoll() / 36 * -(height / scale) + height / 2 + height));
                    }

                    int dotSize = 20;
                    double x = ((Math.cos(Math.toRadians(joystickEvents[i].getAngle() - 90)) * ((width / 2 - dotSize / 2) * joystickEvents[i].getMagnitude())));
                    double y = ((Math.sin(Math.toRadians(joystickEvents[i].getAngle() - 90)) * ((height / 2 - dotSize / 2) * joystickEvents[i].getMagnitude())));
                    g.setColor(new Color(255, 0, 0));
                    g.fill(new Ellipse2D.Double(Math.round(x + width / 2 - dotSize / 2 + offset + width), Math.round(y + height / 2 - dotSize / 2 + height * 2), dotSize, dotSize));

                    //draw boxes outlines
                    g.setColor(new Color(255, 255, 255, 127));
                    g.drawRect(offset + width, 0, width, height);
                    g.drawRect(offset + width, height, width, height);
                    g.drawRect(offset + width, height * 2, width, height);

                    // draw text
                    g.setColor(new Color(255, 255, 255));
                    g.drawString("Nunchuck - G-Force", 2 + offset + width, 10);
                    g.drawString("Orientation", 2 + offset + width, height + 10);
                    g.drawString("X = " + gForceNunchuckList.get(i).getLast().getX(), offset + 2 + width, 20);
                    g.drawString("Y = " + gForceNunchuckList.get(i).getLast().getY(), offset + 2 + width, 30);
                    g.drawString("Z = " + gForceNunchuckList.get(i).getLast().getZ(), offset + 2 + width, 40);
                    g.drawString("Pitch = " + orientationNunchuckList.get(i).getLast().getPitch(), offset + 2 + width, height + 20);
                    g.drawString("Roll = " + orientationNunchuckList.get(i).getLast().getRoll(), offset + 2 + width, height + 30);
                    g.drawString("Angle = " + joystickEvents[i].getAngle(), offset + 2 + width, height * 2 + 10);
                    g.drawString("Magnitude = " + joystickEvents[i].getMagnitude(), offset + 2 + width, height * 2 + 20);
                }

                // clean up lists
                while(gForceWiimoteList.get(i).size() > width){
                    gForceWiimoteList.get(i).remove();
                }
                while(orientationWiimoteList.get(i).size() > width){
                    orientationWiimoteList.get(i).remove();
                }
                while(gForceNunchuckList.get(i).size() > width){
                    gForceNunchuckList.get(i).remove();
                }
                while(orientationNunchuckList.get(i).size() > width){
                    orientationNunchuckList.get(i).remove();
                }
            }else{
                g.setColor(new Color(0, 0, 0, 127));
                g.fillRect(offset, 0, width, height);
                g.setColor(new Color(255, 255, 255));
                g.drawString("MotionSensing disabled for Wiimote " + wiimotes[i].getId(), offset + 2, 10);
                g.setColor(new Color(255, 0, 0));
                g.drawString("Press M to force enable on all devices", offset + 2, 20);
            }
        }
    }
    
    private void setButton(int wiimoteID, Buttons button, boolean value){
        /**
         * since this also writes false to pressedButtons, it may happen that at too short presses, no button press will be registered.
         */
        pressedButtons.get(wiimoteID).put(button, value);
        heldButtons.get(wiimoteID).put(button, value);
    }

    public float getWiimoteGForceX(int wiimoteID){
        if(gForceWiimoteList.get(wiimoteID).size() > 0){
            return gForceWiimoteList.get(wiimoteID).getLast().getX();
        }else{
            return 0;
        }
    }
    
    public float getWiimoteGForceY(int wiimoteID){
        if(gForceWiimoteList.get(wiimoteID).size() > 0){
            return gForceWiimoteList.get(wiimoteID).getLast().getY();
        }else{
            return 0;
        }
    }
    
    public float getWiimoteGForceZ(int wiimoteID){
        if(gForceWiimoteList.get(wiimoteID).size() > 0){
            return gForceWiimoteList.get(wiimoteID).getLast().getZ();
        }else{
            return 0;
        }
    }

    public float getRoll(int wiimoteID){
        return orientationWiimoteList.get(wiimoteID).getLast().getRoll();
    }

    public float getPitch(int wiimoteID) {
        if (orientationWiimoteList.get(wiimoteID).size() > 0){
        return orientationWiimoteList.get(wiimoteID).getLast().getPitch();
        }
        else
            return 0;
    }

    public float getZDifference(int wiimoteID){
        float newZValue = gForceWiimoteList.get(wiimoteID).getLast().getZ();
        return oldValue[wiimoteID][2] - newZValue;
    }

    public void activateRumble(int wiiMoteID){
        wiimotes[wiiMoteID].activateRumble();
    }

    public void deactivateRumble(int wiiMoteID){
        wiimotes[wiiMoteID].deactivateRumble();
    }


    /**
     * Returns an estimated point (center) in an area of 1024 * 900
     * 
     * @param wiimoteID index of list of connected wiimotes
     * @return the estimated point
     */
    public Point2D getCenteredPointer(int wiimoteID){
        Point2D point = new Point2D.Double(0, 0);
        for(int i = 0; irSources[wiimoteID].length > i; i++){
            point.setLocation(point.getX() + irSources[wiimoteID][i].getX(), point.getY() + irSources[wiimoteID][i].getY());
        }
        point.setLocation(point.getX()/irSources[wiimoteID].length, point.getY()/irSources[wiimoteID].length);
        return point;
    }

    /**
     * Returns the first point found by the IR camera in an area of 1024*900
     * 
     * @param wiimoteID index of list of connected wiimotes
     * @return the estimated point
     */
    public Point2D getSinglePointer(int wiimoteID){
        Point2D point = new Point2D.Double(0, 0);
        if(irSources[wiimoteID].length > 0){
            point.setLocation(irSources[wiimoteID][0].getX(), irSources[wiimoteID][0].getY());
        }
        return point;
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
        if(pressedButtons.get(wiimoteID).get(button) != null){
            boolean returnVal = pressedButtons.get(wiimoteID).get(button);
            pressedButtons.get(wiimoteID).put(button, false);
            return returnVal;
        }else{
            return  false;
        }

    }

    // TODO Martijn could you please check if this works with a wiiMote
    public boolean isAnyButtonPressed(int wiimoteID)
    {
        if (pressedButtons.get(wiimoteID) != null)
        {
            for (Buttons button : Buttons.values())
            {
                if (pressedButtons.get(wiimoteID).get(button) != null)
                {
                    if (pressedButtons.get(wiimoteID).get(button))
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns if the button of wiimote is being held down.
     * 
     * @param wiimoteID index of list of connected wiimotes
     * @param button enum of button to return the value of
     * @return the stored value for the given wiimote and corresponding button
     */
    public boolean getIsButtonDown(int wiimoteID, Buttons button){
        if(heldButtons.get(wiimoteID).get(button) != null){
            return heldButtons.get(wiimoteID).get(button);
        }else{
            return false;
        }
    }

    private void setNunchuckConnected(int nunchuck, boolean value){
        connectedNunchucks[nunchuck] = value;
    }

    /**
     * Returns true if there is a nunchuck connected.
     * 
     * @param wiimoteID index of list of connected wiimotes
     * @return true if a nunchuck is connected
     */
    public boolean isNunchuckConnected(int wiimoteID){
        return connectedNunchucks[wiimoteID];
    }

    /**
     * Activates motion sensing on all connected wiimotes.
     */
    public void activateMotionSensing(){
        if(wiimotes != null){
            for(int i = 0; i < wiimotes.length; i++){
                activateMotionSensing(i);
            }
        }
    }

    /**
     * Activates motion sensing for given wiimote.
     * 
     * @param wiimoteID index of list of connected wiimotes
     */
    public void activateMotionSensing(int wiimoteID){
        if(wiimotes[wiimoteID] != null){
            wiimotes[wiimoteID].activateMotionSensing();
            isMotionSensingActive[wiimoteID] = true;
        }
    }

    /**
     * Deactivates motion sensing on all connected wiimotes.
     */
    public void deactivateMotionSensing(){
        if(wiimotes != null){
            for(int i = 0; i < wiimotes.length; i++){
                deactivateMotionSensing(i);
            }
        }
    }

    /**
     * Deactivates motion sensing on given wiimote.
     * 
     * @param wiimoteID index of list of connected wiimotes
     */
    public void deactivateMotionSensing(int wiimoteID){
        if(wiimotes[wiimoteID] != null){
            wiimotes[wiimoteID].deactivateMotionSensing();
            isMotionSensingActive[wiimoteID] = false;
        }
    }

    public void activateIRTracking(){
        if(wiimotes != null){
            for(int i = 0; i < wiimotes.length; i++){
                activateIRTracking(i);
            }
        }
    }
    
    public void activateIRTracking(int wiimoteID){
        if(wiimotes[wiimoteID] != null){
            wiimotes[wiimoteID].activateIRTRacking();
        }
    }

    public void deactivateIRTracking(){
        if(wiimotes != null){
            for(int i = 0; i < wiimotes.length; i++){
                deactivateIRTracking(i);
            }
        }
    }
    
    public void deactivateIRTracking(int wiimoteID){
        if(wiimotes[wiimoteID] != null){
            wiimotes[wiimoteID].deactivateIRTRacking();
        }
    }
    
    public boolean isWiiMotesConnected() {
        if (wiimotes != null) {
            if (wiimotes.length > 0) {
                return true;
            }
            return false;
        }
        return false;
    }
    
    public int numberOfWiimotesConnected(){
        return wiimotes.length;
    }

    public boolean[] getPeakValue(int wiiMote){
        return peakValue[wiiMote];
    }

    public float getMax(int wiiMote){
        int max = gForceWiimoteList.get(wiiMote).size();
        ArrayList<Float> values = new ArrayList<>();
        if(max > 10) {
            for (int i = 0; i < 10; i++) {
                float valueToadd = gForceWiimoteList.get(wiiMote).get(max - 1 - i).getX();
                if (valueToadd < 0) {
                    valueToadd = -valueToadd;
                }
                values.add(valueToadd);
            }
            Collections.sort(values);
            return values.get(values.size() - 1);
        }else{
            return 0;
        }
    }
}
