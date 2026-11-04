package engine.object;

import javax.swing.*;
import java.util.*;

/**
 * Die engine handelt von einem Grundprinzip: Alles sind Objekte in einer Welt. Doch was heist das?
 * Konkret heist das nur, dass jedes Lebewesen, jeder Knopf, jedes Bild, ... also alles ein Objekt ist.
 * Jedes Objekt hat dabei folgende primitive Attribute/Funktionen:
 * <li> eine Position.
 * <li> Maße (länge und weite).
 * <li> eine Textur.
 * <li> eine Hitbox.
 * <li> die Fähigkeit geladen/entladen zu sein.
 * <li> eine Funktion, welche jedes Update getriggert wird.
 * <br>
 * <br>
 * Diese Attribute bzw. Funktionen reichen schon aus um ein einfaches Spiel zu programmieren. Für komplexere Spiele,
 * kann man diese Klasse erweitern, um somit einem Objekt entweder eigene Attribute oder eigene Funktionen zu geben.
 * Ein Beispiel wäre das Entity.
 * <br>
 * <br>
 * Der zweite Teil des Grundprinzips ist, dass alle erstellten Objekte in einer Welt sind. Man kann es sich vorstellen,
 * wie einen imaginären Raum, wo all diese Objekte vorhanden sind. Sie sind mit ihren Maßen und texturen an ihrer bestimmten Position
 * in diesem Raum.
 * <br>
 * Besonders ist nur, dass man sich von dem Grundgedanken loslösen muss, dass dieser einen Bezug hat, dem ist nicht so.
 * Diese Welt bzw. dieser imaginäre Raum ist Bezugslos. Man weiß also nicht wo welche Position ist oder wie weit die Objekte voneinander
 * entfernt sind. Man weiß, sie haben beispielsweise einen Abstand von zwei, jedoch weiß man nicht, ob dieser Abstand zwei Meter,
 * Zentimeter oder Pixel ist. Das gleiche auch mit der Position. Man weiß, sie sind beispielsweise am Ursprung, doch wo genau
 * dieser Ursprung im Raum liegt weiß man nicht.
 * <br>
 * Erst beim Versuch diese Welt sichtbar zu machen, kommt es zu einer Konvertierung vom bezugslosen zu einem "Raum" mit Bezug.
 * Es ist deswegen immer notiert, ob diese Position oder Länge in der Welt oder auf einem anderen Bezugsystem ist.
 *
 * @see Entity
 * @see Camera
 */
public class GameObject
{
    /**
     * Alle jemals erstellten Spielobjekte in dieser Welt.
     */
    static ArrayList<GameObject> gameObjects = new ArrayList<>();
    
    /**
     * Alle derzeitig geladenen Spielobjekte.
     */
    static ArrayList<GameObject> loadedGameObjects = new ArrayList<>();
    
    /**
     * Die horizontale Position des Objektes in der Welt.
     */
    float x;
    
    /**
     * Die vertikale Position des Objektes in der Welt
     */
    float y;
    
    /**
     * Die Weite des Objektes im Weltmaßstab.
     */
    float width;
    
    /**
     * Die Höhe des Objektes im Weltmaßstab.
     */
    float height;
    
    /**
     * Die Textur des Objektes
     */
    ImageIcon texture;
    
    /**
     * Die Hitbox des Objektes.
     */
    Hitbox hitbox;
    
    /**
     * Erstellt ein neues GameObject.
     *
     * @param x         horizontale Position in der Welt
     * @param y         vertikale Position in der Welt
     * @param width     Weite (Weltmaßstab)
     * @param height    Höhe (Weltmaßstab)
     * @param texture   Textur
     */
    public GameObject(float x, float y, float width, float height, ImageIcon texture)
    {
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
        setTexture(texture);
        
        gameObjects.add(this);
    }
    
    /**
     * Löscht das GameObject vollständig vom Spiel.
     * <br>
     * <br>
     * Jedes Spielobjekt wird automatisch beim Erstellen in einer Liste mit allen
     * Spielobjekten gespeichert. Beim Löschen eines objektes, löscht man diesen Eintrag aus
     * dieser Liste. Ebenfalls wird das Spielobjekt entladen.
     *
     * @see #gameObjects
     * @see #loadedGameObjects
     */
    public void remove()
    {
        unload();
        gameObjects.remove(this);
        gameObjects.trimToSize();
        if(getHitbox() != null)
        {
            getHitbox().unload();
            getHitbox().remove();
        }
    }
    
    /**
     * Lädt das GameObject.
     * <br>
     * <br>
     * Sobald ein Spielobjekt geladen wird, wird es in eine Liste aufgenommen.
     * Alle Objekte, die in dieser Liste sind, werden automatisch geupdatet und gerendert.
     * Zudem kann mit diesem Spielobjekt interagiert werden.
     * <br>
     * <br>
     * Das Laden oder entladen wird eingesetzt, um Ressourcen zu sparen. Dieser Prozess wird automatisch
     * von den Kameras in der Welt getätigt.
     *
     * @see #loadedGameObjects
     * @see #unload()
     * @see Camera
     */
    public void load()
    {
        if(!loadedGameObjects.contains(this)) loadedGameObjects.add(this);
    }
    
    /**
     * Entlädt das GameObject.
     * <br>
     * <br>
     * Entladene Spielobjekte, werden weder geupdatet, noch gerendert oder sonst zu interagieren.
     * Sie werden aus einer Liste mit allen geladenen Spielobjekten entfernt
     *
     * @see #loadedGameObjects
     * @see #load()
     */
    public void unload()
    {
        if(loadedGameObjects.contains(this))
        {
            loadedGameObjects.remove(this);
            loadedGameObjects.trimToSize();
        }
    }
    
    /**
     * Diese Funktion wird jedes Update von der Spiel-Schleife geupdatet.
     * <br>
     * <br>
     * Sobald ein GameObjekt geladen ist, wird es auch geupdatet. Das heist das jedes Frame diese Methode
     * ausgeführt wird.
     * <br>
     * Standardmäßig sind keine Operationen festgelegt. Diese Methode kann also ohne Probleme überschrieben werden.
     */
    public void onUpdate()
    {
        //No Op
    }
    
    
    /**
     * Gibt die horizontale Position des GameObjects in der Welt zurück.
     * @return horizontale Position des GameObjects
     */
    public float getX()
    {
        return x;
    }
    
    /**
     * Setzt die horizontale Position in der Welt.
     * @param x neue Position
     */
    public void setX(float x)
    {
        this.x = x;
    }
    
    /**
     * Gibt die vertikale Position des GameObjects in der Welt zurück.
     * @return vertikale Position
     */
    public float getY()
    {
        return y;
    }
    
    /**
     * Setzt die vertikale Position in der Welt
     * @param y neue vertikale Position
     */
    public void setY(float y)
    {
        this.y = y;
    }
    
    /**
     * Gibt die Weite des GameObjects zurück im Weltmaßstab.
     * @return Weite des Objekts
     */
    public float getWidth()
    {
        return width;
    }
    
    /**
     * Setzt die Weite des GameObjects im Weltmaßstab.
     * @param width neue Weite des Objektes
     */
    public void setWidth(float width)
    {
        this.width = width;
    }
    
    /**
     * Gibt die derzeitige Höhe des GameObjects im Weltmaßstab zurück.
     * @return derzeitige Höhe
     */
    public float getHeight()
    {
        return height;
    }
    
    /**
     * Setzt die Höhe des GameObjects im Weltmaßstab.
     * @param height neue Höhe des Objektes
     */
    public void setHeight(float height)
    {
        this.height = height;
    }
    
    /**
     * Gibt die Textur des GameObjects zurück.
     * @return textur des Objektes; 'null' wenn keine Textur vorhanden
     */
    public ImageIcon getTexture()
    {
        return texture;
    }
    
    /**
     * Setzt eine neue Textur für das GameObject.
     * @param texture neue Textur
     */
    public void setTexture(ImageIcon texture)
    {
        this.texture = texture;
    }
    
    /**
     * Gibt die Hitbox des Objektes zurück, falls dieses eine besitzt.
     * @return Hitbox des Objektes; 'null' falls keine vorhanden
     */
    public Hitbox getHitbox()
    {
        return hitbox;
    }
    
    /**
     * Setzt eine Hitbox.
     * <br>
     * <br>
     * Eine Hitbox, dient zur Überprüfung und Reaktion von Kollision.
     * @param hitbox neue Hitbox
     */
    public void setHitbox(Hitbox hitbox)
    {
        this.hitbox = hitbox;
    }
    
    /**
     * Gibt die universal Identifier zurück.
     * <br>
     * <br>
     * Die uID ist der Index des Objektes in der Liste von allen erstellten Spielobjekten.
     * Sie kann helfen, um bestimmte Objekte zu finden.
     * @return die uID
     */
    public int getUID()
    {
        return getGameObjects().indexOf(this);
    }
    
    /**
     * Gibt die Liste an allen je erstellten Spielobjekten zurück.
     * <br>
     * <br>
     * Sobald ein Spielobjekt erstellt wird, wird dieses in eine Liste aufgenommen.
     * In dieser Liste sind demnach alle Spielobjekte zu finden.
     *
     * @return Liste von allen Spielobjekten
     * @see #gameObjects
     */
    public static ArrayList<GameObject> getGameObjects()
    {
        return gameObjects;
    }
    
    /**
     * Gibt eine Liste von allen geladenen Spielobjekten zurück.
     * <br>
     * <br>
     * Wenn ein Spielobjekt geladen wird, wird es in einer Liste aufgenommen.
     * Diese Liste kann dann durchgegangen werden, um es beispielsweise zu updaten.
     * Somit reduziert man drastisch die Ressourcen, da man viel weniger Objekte überprüfen muss.
     * <br>
     * Diese Liste wird im Normalfall von den Kameras befüllt.
     *
     * @return Liste an geladenen Spielobjekten.
     * @see #loadedGameObjects
     * @see #load()
     * @see Camera
     */
    public static ArrayList<GameObject> getLoadedGameObjects()
    {
        return loadedGameObjects;
    }
}
