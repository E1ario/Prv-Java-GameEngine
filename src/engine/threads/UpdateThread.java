package engine.threads;

import engine.object.*;
import engine.ui.*;

/**
 * Der UpdateThread, ist verantwortlich für eine reibungslose Aktualisierung des Spiels.
 * <br>
 * <br>
 * Dieser UpdateThread kann man dann durch die start() Methode starten, sodass das Spiel geupdatet wird.
 * Dabei versucht der UpdateThread eine bestimmte, vorgesetzte Updaterate zu erreichen. Diese Updaterate sind
 * die Updates pro Sekunde, kurz UPS.
 * <br>
 * Wenn der Thread gestartet wurde, dann ist er verantwortlich dafür, dass die Kameras ihre sichtbaren Objekte laden
 * und dass diese geladenen Objekte mit deren onUpdate() Methode geupdatet werden.
 *
 * @see Thread
 * @see engine.object.GameObject
 * @see engine.object.Camera
 */
public class UpdateThread extends Thread
{
    /**
     * Updates pro Sekunde (UPS), die das System versucht zu erreichen. Es kann dabei nicht über diese Anzahl gehen.
     */
    public static long maxUpdatesPerSecond = 60;
    
    /**
     * Speichert, ob alle Threads pausiert werden sollen.
     */
    public static boolean stopRequest;
    
    
    /**
     * Die Methode start() startet einen separaten Thread. Dieser Thread ruft anschließend die run() Methode auf,
     * welche alle definierten Befehle in der onUpdate() funktion aufruft.
     * @see #run()
     * @see #onUpdate()
     */
    public final void start()
    {
        stopRequest = false;
        super.start();
    }
    
    /**
     * Die Run Methode überschreibt die der java.lang.Thread Klasse.
     * Normalerweise würde, wenn ein Thread gestartet wird, diese Methode nur einmal aufgerufen, da aber eine Schleife vorliegt
     * wird diese in einem bestimmten Intervall durchlaufen. Dieses Intervall sind die Updates pro Sekunde (UPS)
     * <p>
     * <p>
     * Damit ein Thread auch gestoppt werden kann, unterliegt diese schleife der StopRequest.
     * Wenn also ein stop angefragt wird, hört diese Schleife einfach auf zu laufen und der Garbage Collector kümmert sich um den Rest.
     *
     * @see #onUpdate()
     * @see Thread
     */
    @Override
    public final void run()
    {
        super.run();
        while(!isStopRequested())
        {
            if(Display.getDisplay().isShowing())
            {
                //Update Thread
                onUpdate();
            }
            
            
            //Delay, damit kein Stackoverflow zustande kommt
            try
            {
                sleep(1000 / maxUpdatesPerSecond);
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        }
        
        System.gc();
    }
    
    /**
     * Die onUpdate Methode wird jedes Update von dem UpdateThread aufgerufen.
     * <br>
     * <br>
     * Ihre Aufgabe ist es entsprechende Spielobjekte mithilfe der Kameras zu laden, und alle geladenen GameObjects
     * zu updaten. Das passiert über deren onUpdate() Funktion. Zudem wird hier auch das Fenster neu gezeichnet.
     *
     * @see #run()
     */
    public void onUpdate()
    {
        //Entladen aller Objekte, damit erneut entschieden werden kann, welches Objekt geladen werden soll und welches nicht.
        GameObject.getLoadedGameObjects().clear();
    
        //Laden aller Objekte, die in der Kamera zu sehen sind
        for(int i = 0; i < GameObject.getGameObjects().size(); i++)
            if(GameObject.getGameObjects().get(i) instanceof  Camera)
                ((Camera) GameObject.getGameObjects().get(i)).loadObjectsOnCamera(GameObject.getGameObjects().toArray(new GameObject[0]));
        
        //Updaten aller geladenen Objekte
        for(int i = 0; i < GameObject.getLoadedGameObjects().size(); i++)
            GameObject.getLoadedGameObjects().get(i).onUpdate();
    
        Display.getRenderEngine().setSize(Display.getDisplay().getWidth(), Display.getDisplay().getHeight()); //Setzt die größe der Renderengine, damit falls das Fenster größer gezogen wird, man gleich viel sieht.
        Display.getDisplay().repaint();
    }
    
    /**
     * Fragt einen Stop des Threads an.
     * <br>
     * <br>
     * Wenn der Thread stoppt, dann wird das Spiel nicht mehr geupdatet.
     *
     * @see #onUpdate()
     * @see #stopRequest
     */
    public final void requestStop()
    {
        stopRequest = true;
    }
    
    /**
     * Gibt zurück, ob der Thread pausiert werden soll.
     *
     * @return true, wenn er pausiert werden soll, false wenn nicht.
     */
    public final boolean isStopRequested()
    {
        return stopRequest;
    }
}
