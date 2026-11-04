package testProgramm.maps;

import engine.object.*;
import engine.sound.*;
import engine.ui.*;
import testProgramm.background.*;
import testProgramm.windowComponents.Button;
import testProgramm.blocks.Spring;
import testProgramm.blocks.*;
import testProgramm.entities.*;
import testProgramm.entities.items.*;
import testProgramm.io.*;

import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;

/**
 * hier sind alle Methoden die dafür sorgen, dass man unterschiedliche Welten lädt.
 * Beispielsweise die Hub- und Dschungel-Welt
 */
public class Maps
{
    /**
     * Neues Properties Objekt, damit man auf Properties Files zugreifen kann.
     */
    private static Properties conf = new Properties("ressource/config.properties");
    
    /**
     * Lädt die Hub-Welt. Dort kann der Spieler
     * die Steuerungen ausprobieren und einzelne Welten betreten.
     */
    public static void loadWorld_Hub()
    {
        //Background
        ImageIcon bg = new ImageIcon("ressource/maps/hub/background.png");
        float width = bg.getIconWidth();
        float height = bg.getIconHeight();
        
        clearMap();
        
        new GameObject(0, 0, width, height, bg);
        
        //Blöcke
        loadMap("ressource/maps/hub/blockMap.png");
        
        //Entities
        Player player = new Player(
                conf.getFloat("world.hub.object.entity.player.start_position_x"),
                conf.getFloat("world.hub.object.entity.player.start_position_y"),
                conf.getFloat("object.entity.player.width"),
                conf.getFloat("object.entity.player.height")
        );
        
        //Kamera
        Camera camera = new Camera(
                conf.getFloat("object.camera.start_position_x"),
                conf.getFloat("object.camera.start_position_y"),
                Display.getDisplay().getWidth(),
                Display.getDisplay().getHeight(),
                conf.getFloat("object.camera.render_position_x"),
                conf.getFloat("object.camera.render_position_y"));
        camera.setFocusX(camera.getX() + camera.getWidth() / 2);
        camera.setFocusY(camera.getY() + camera.getHeight() / 2);
        camera.setFocusedObj(player);
        camera.setZoom(conf.getFloat("ui.camera.default_Zoom"));
        
        
        //Knöpfe
        Button button = new Button(
                conf.getFloat("world.hub.window_components.button.1.x"),
                conf.getFloat("world.hub.window_components.button.1.y"),
                conf.getFloat("window_components.button.width"),
                conf.getFloat("window_components.button.height"),
                new ImageIcon("ressource/maps/hub/buttons/button1/button1.png"),
                new ImageIcon("ressource/maps/hub/buttons/button1/button1_hovered.png"),
                new ImageIcon("ressource/maps/hub/buttons/button1/button1_pressed.png")
        )
        {
            @Override
            public void action()
            {
                loadWorld_Jungle();
            }
        };
        
        Button button2 = new Button(
                conf.getFloat("world.hub.window_components.button.2.x"),
                conf.getFloat("world.hub.window_components.button.2.y"),
                conf.getFloat("window_components.button.width"),
                conf.getFloat("window_components.button.height"),
                new ImageIcon("ressource/maps/hub/buttons/button2/button2.png"),
                new ImageIcon("ressource/maps/hub/buttons/button2/button2_hovered.png"),
                new ImageIcon("ressource/maps/hub/buttons/button2/button2_pressed.png")
        )
        {
            @Override
            public void action()
            {
                System.exit(0);
            }
        };
    }
    
    /**
     * Das ist die erste Welt, die Dschungelwelt.
     * Dort ist die Aufgabe artefakte zu suchen und einen Tempel zu erkunden.
     * Doch Vorsicht ist geboten, denn Feinde versuchen den "Eindringling" zu stoppen.
     */
    public static void loadWorld_Jungle()
    {
        clearMap();
    
        //Background
        new BackgroundJungle(0, 0, 504, 150);
        new BackgroundJungle(504, 0, 504, 150);
        
        //Blöcke
        loadMap("ressource/maps/jungle/blockMap.png");
    
        //Entities
        Player player = new Player(
                conf.getFloat("world.jungle.object.entity.player.start_position_x"),
                conf.getFloat("world.jungle.object.entity.player.start_position_y"),
                conf.getFloat("object.entity.player.width"),
                conf.getFloat("object.entity.player.height")
        );
        
        Enemy enemy = new Enemy(
                conf.getFloat("world.jungle.object.entity.enemy.start_position_x"),
                conf.getFloat("world.jungle.object.entity.enemy.start_position_Y"),
                conf.getFloat("object.entity.enemy.width"),
                conf.getFloat("object.entity.enemy.height"),
                player);
    
        //Kamera
        Camera camera = new Camera(
                conf.getFloat("object.camera.start_position_x"),
                conf.getFloat("object.camera.start_position_y"),
                Display.getDisplay().getWidth(),
                Display.getDisplay().getHeight(),
                conf.getFloat("object.camera.render_position_x"),
                conf.getFloat("object.camera.render_position_y"));
        camera.setFocusX(camera.getX() + (camera.getWidth() / 2));
        camera.setFocusY(camera.getY() + (camera.getHeight() / 2));
        camera.setFocusedObj(player);
        camera.setZoom(conf.getFloat("ui.camera.default_Zoom"));
        
        //Audio
        Sound jungleMusic = new Sound("ressource/maps/jungle/audio/music.mp3");
        jungleMusic.getAudioClip().start();
        
        Sound jungleAtmosphere = new Sound("ressource/maps/jungle/audio/atmosphere.mp3");
        jungleAtmosphere.getAudioClip().start();
    }
    
    /**
     * Löscht alle Spiel-Objekte, sodass eine Neue Welt geladen werden kann.
     */
    private static void clearMap()
    {
        GameObject.getGameObjects().clear();
    }
    
    /**
     * Konvertiert farbige Pixel in entsprechende Blöcke.
     * Diese Blöcke haben dann die Position je nachdem wie der Pixel ist.
     * Beispiel: ist ein schwarzes Pixel auf dem Bild bei (1, 1) so ist der
     * Block bei (1 * width, 1 * height) also bei (8, 8).
     *
     * @param mapFile Der Datei Pfad, in dem die Map abgespeichert ist.
     */
    private static void loadMap(String mapFile)
    {
        BufferedImage img;
    
        try
        {
            img = ImageIO.read(new File(mapFile));
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        
        for(int y = 0; y < img.getHeight(); y++)
            for(int x = 0; x < img.getWidth(); x++)
            {
                if(img.getRGB(x, y) == new Color(0, 0, 0).getRGB())
                    new Block(x * 8, y * 8, 8, 8, new ImageIcon("ressource/textures/blocks/stone_brick.png"));
                if(img.getRGB(x, y) == new Color(1, 1, 1).getRGB())
                    new Block(x * 8, y * 8, 8, 8, new ImageIcon("ressource/textures/blocks/stone_brick2.png"));
                if(img.getRGB(x, y) == new Color(2, 2, 2).getRGB())
                    new Block(x * 8, y * 8, 8, 8, new ImageIcon("ressource/textures/blocks/stone_brick3.png"));
                if(img.getRGB(x, y) == new Color(3, 3, 3).getRGB())
                    new Block(x * 8, y * 8, 8, 8, new ImageIcon("ressource/textures/blocks/stone_brick4.png"));
                if(img.getRGB(x, y) == new Color(4, 4, 4).getRGB())
                    new Block(x * 8, y * 8, 8, 8, new ImageIcon("ressource/textures/blocks/sculp1/sculp1_1.png"));
                if(img.getRGB(x, y) == new Color(5, 5, 5).getRGB())
                    new Block(x * 8, y * 8, 8, 8, new ImageIcon("ressource/textures/blocks/sculp1/sculp1_2.png"));
                if(img.getRGB(x, y) == new Color(6, 6, 6).getRGB())
                    new Block(x * 8, y * 8, 8, 8, new ImageIcon("ressource/textures/blocks/sculp1/sculp1_3.png"));
                if(img.getRGB(x, y) == new Color(7, 7, 7).getRGB())
                    new Block(x * 8, y * 8, 8, 8, new ImageIcon("ressource/textures/blocks/sculp1/sculp1_4.png"));
                if(img.getRGB(x, y) == new Color(8, 8, 8).getRGB())
                    new Block(x * 8, y * 8, 8, 8, new ImageIcon("ressource/textures/blocks/sculp2/sculp2_1.png"));
                if(img.getRGB(x, y) == new Color(9, 9, 9).getRGB())
                    new Block(x * 8, y * 8, 8, 8, new ImageIcon("ressource/textures/blocks/sculp2/sculp2_2.png"));
                if(img.getRGB(x, y) == new Color(10, 10, 10).getRGB())
                    new Block(x * 8, y * 8, 8, 8, new ImageIcon("ressource/textures/blocks/sculp2/sculp2_3.png"));
                if(img.getRGB(x, y) == new Color(11, 11, 11).getRGB())
                    new Block(x * 8, y * 8, 8, 8, new ImageIcon("ressource/textures/blocks/sculp2/sculp2_4.png"));
                if(img.getRGB(x, y) == new Color(12, 12, 12).getRGB())
                    new Block(x * 8, y * 8, 8, 8, new ImageIcon("ressource/textures/blocks/metal.png"));
                if(img.getRGB(x, y) == new Color(13, 13, 13).getRGB())
                    new Spring(x * 8, y * 8, 8, 8, conf.getFloat("object.block.spring.boost_height"));
                if(img.getRGB(x, y) == new Color(14, 14, 14).getRGB())
                    new SpeedBoost(x * 8, y * 8, 8, 8);
                if(img.getRGB(x, y) == new Color(15, 15, 15).getRGB())
                    new TransparentBlock(x * 8, y * 8, 8, 8, new ImageIcon("ressource/textures/transparentBlocks/stone_brick.png"));
            }
    }
}
