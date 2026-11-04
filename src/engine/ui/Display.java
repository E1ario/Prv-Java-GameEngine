package engine.ui;

import engine.controller.*;

import javax.swing.*;
import java.awt.*;

/**
 * Die Display Klasse ist verantwortlich für das Erstellen eines voll funktionstüchtigen Fensters.
 * <br>
 * <br>
 * Dafür wird über ein JFrame (java.swing Library) ein Fenster erstellt. Dieses wird beim Erstellen mit Eigenschaften,
 * wie die Größe oder der Fenstertext personalisiert.
 * <br>
 * <br>
 * Die Klasse Display sorgt dafür, dass grundlegende Abläufe bzw. Grundlegende Eigenschaften gesetzt werden.
 * Ein Beispiel hierfür ist die Startposition des Fensters. Doch auch die Renderengine wird diesem Fenster hinzugefügt.
 * Diese Grundfunktionen/Grundeigenschaften werden über die init() Methode gesetzt. Über diese Methode lässt sich schnell ein
 * Fenster erstellen.
 *
 * @see #init(int, int, String)
 * @see javax.swing.JFrame
 * @see RenderEngine
 */
public class Display
{
    /**
     * Das JFrame, über das ein Fenster erstellt wird.
     * Über dieses JFrame können weitere Eigenschaften gesetzt werden.
     *
     * @see javax.swing.JFrame
     */
    private static JFrame frame = new JFrame();
    
    /**
     * Eine Instanz der Renderengine.
     *
     * @see RenderEngine
     */
    private static RenderEngine renderEngine;
    
    /**
     * Initialisiert ein Display.
     * <br>
     * <br>
     * D.h es wird ein standard Fenster erstellt, wo alle Komponenten
     * und Handler/Listener hinzugefügt werden. Auch die Render Engine
     * hinzugefügt und standard Eigenschaften gesetzt.
     * <br>
     * <br>
     * Die Methode erstellt folgendes Fenster:
     * <li> Das Fenster öffnet sich in der Mitte des Bildschirmes
     * <li> Das Fenster hat die übergebene Größe
     * <li> Das Fenster hat den übergebenen Titel
     * <li> Das Fenster hat einen weißen Hintergrund
     * <li> Das Fenster kann nicht vergrößert werden
     * <li> Das Fenster implementiert alle nötigen Handler und die Renderengine
     *
     * @see RenderEngine
     * @see engine.controller.Controller
     */
    public void init(int width,
                     int height,
                     String title)
    {
        //Setzten der Eigenschaften
        getDisplay().setSize(width, height);
        getDisplay().setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getDisplay().setTitle(title);
        getDisplay().setLayout(null);
        getDisplay().getContentPane().setBackground(new Color(255, 255, 255));
        getDisplay().setLocationRelativeTo(null);
        getDisplay().setResizable(false);
        
        //Hinzufügen der Handler/Listener
        implementInputHandler(frame);
        implementRenderEngine(frame, 0, 0, width, height);
        
        getDisplay().setVisible(true);
    }
    
    /**
     * Implementiert alle InputHandler.
     * <br>
     * <br>
     * D.h es fügt den KeyHandler, MouseHandler und den MouseMotionHandler dem
     * Fenster hinzu, sodass diese verwendet werden können.
     *
     * @param frame Das Fenster, auf das die input Handler gesetzt werden sollen
     * @see engine.controller.KeyHandler
     * @see engine.controller.MouseHandler
     * @see engine.controller.MouseMotionHandler
     */
    public void implementInputHandler(JFrame frame)
    {
        frame.addKeyListener(new KeyHandler());
        frame.addMouseListener(new MouseHandler());
        frame.addMouseMotionListener(new MouseMotionHandler());
    }
    
    /**
     * Implementiert die Render-Engine.
     * <br>
     * <br>
     * Diese ist ein Bereich auf dem Fenster, indem gezeichnet werden kann.
     *
     * @param frame Das Fenster, auf das die Render-Engine gesetzt werden sollen
     * @see RenderEngine
     */
    public void implementRenderEngine(JFrame frame,
                                      int x,
                                      int y,
                                      int width,
                                      int height)
    {
        renderEngine = new RenderEngine();
        renderEngine.setBounds(x, y, width, height);
        renderEngine.setVisible(true);
        frame.add(renderEngine);
    }
    
    
    /**
     * Gibt das Display zurück, damit weitere Eigenschaften gesetzt werden können.
     * @return das Display
     */
    public static JFrame getDisplay()
    {
        return frame;
    }
    
    /**
     * Gibt die Render Engine des Displays zurück
     * @return Render Engine
     */
    public static RenderEngine getRenderEngine()
    {
        return renderEngine;
    }
}
