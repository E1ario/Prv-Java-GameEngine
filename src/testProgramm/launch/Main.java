package testProgramm.launch;

import engine.threads.*;
import engine.ui.*;
import testProgramm.io.*;
import testProgramm.maps.*;

import javax.swing.*;
import java.awt.*;

public class Main
{
    public static void main(String[] args)
    {
        Properties conf = new Properties("ressource/config.properties");
        
        //Fenster erstellen
        Display display = new Display();
        
        if(conf.getBoolean("ui.window.fullscreen"))
        {
            Display.getDisplay().setUndecorated(true);
            display.init(
                    (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
                    (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight(),
                    "");
        }
        else
        {
            display.init(
                    conf.getInt("ui.window.width"),
                    conf.getInt("ui.window.height"),
                    conf.get("ui.window.title").toString()
            );
        }
        
        //Erstelle Welt
        Maps.loadWorld_Hub();
        
        
        //Starte Threads
        new UpdateThread().start();
        UpdateThread.maxUpdatesPerSecond = conf.getLong("thread.updateThread.updates_per_second");
    }
}
