package testProgramm.background;

import engine.object.*;
import testProgramm.io.*;

import javax.swing.*;

public class BackgroundJungle extends GameObject
{
    /**
     * Ein neues Properties Objekt, damit man Properties Dateien Lesen kann.
     */
    private static Properties conf = new Properties("ressource/config.properties");
    
    /**
     * Der derzeitige Animations frame, welcher gezeichnet wird.
     */
    private int frame = 0;
    
    /**
     * Eine Variable, die die Updates zählt, in denen das Bild der Animation nicht geändert wird.
     * Damit wird gesteuert, wie schnell die Animationen abspielen.
     */
    private int pause_frame = 0;
    
    /**
     * Die Pause, der einzelnen Bilder einer Animation. Je größer die Pause, desto langsamer ist die Animation
     */
    private int animation_stop_duration = conf.getInt("world.jungle.background.animation_pause_duration");
    
    
    public BackgroundJungle(float x, float y, float width, float height)
    {
        super(x, y, width, height, new ImageIcon("ressource/maps/jungle/background/frame (1).png"));
    }
    
    @Override
    public void onUpdate()
    {
        animate();
        super.onUpdate();
    }
    
    private void animate()
    {
        setTexture(new ImageIcon("ressource/maps/jungle/background/frame (" + frame + ").png"));
        
        //Animation verlängern:
        if(pause_frame > animation_stop_duration)
            pause_frame = 0;
        else pause_frame++;
    
        //Animationsframe erhöhen
        if(pause_frame == 0)
        {
            if (frame > 24)
                frame = 1;
            else
                frame++;
        }
    }
}
