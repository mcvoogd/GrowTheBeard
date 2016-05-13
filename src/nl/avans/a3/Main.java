package nl.avans.a3;

import Support.Logger;

import java.awt.*;

public class Main{

    public static void main(String args[]){
        new Main();
        System.exit(0);
    }
    
    private Main(){
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice screen = ge.getDefaultScreenDevice();
        
        /**
         * Test for available screen sizes and available devices, uncomment code below
         */
        /*for(GraphicsDevice g : ge.getScreenDevices()){
            System.out.println(g.toString());
        }
        for(DisplayMode d : screen.getDisplayModes()){
            System.out.println("Width = " + d.getWidth() + " Height = " + d.getHeight() + " BitDepth = " + d.getBitDepth() + " Refresh rate = " + d.getRefreshRate());
        }*/
        
        DisplayMode oldDisplayMode = screen.getDisplayMode();
        DisplayMode newDisplayMode = new DisplayMode(1920, 1080, screen.getDisplayMode().getBitDepth(), screen.getDisplayMode().getRefreshRate());
        
        if(!screen.isFullScreenSupported()){
            Logger.instance.log("MN001", "Main::Main", "Fullscreen unsupported on this device", Logger.LogType.ERROR);
            System.exit(1);
        }
        
        try{
            Frame frame = new Frame();
            frame.add(new GraphicsWindow());
            frame.setTitle("Grow the Beard");
            frame.setAutoRequestFocus(true);
            frame.setUndecorated(true);
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
            frame.toFront();
            screen.setFullScreenWindow(frame);
            screen.setDisplayMode(newDisplayMode);
            while(true);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            screen.setDisplayMode(oldDisplayMode);
            screen.setFullScreenWindow(null);
        }
    }
}
