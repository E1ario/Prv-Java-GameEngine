package engine.object;

import engine.exceptions.*;

import javax.swing.*;
import java.util.*;

/**
 * Die Hitbox ist ein GameObject, welches für die Kollision zuständig ist.
 * <br>
 * <br>
 * Dabei gibt es zurück ob, wie und welches Objekt kollidiert.
 */
public class Hitbox extends GameObject
{
    /**
     * Wird zurückgegeben, wenn das Objekt mit der Oberseite des anderen Objektes kollidiert
     */
    public static final int COLLISION_WITH_TOP = 0;
    
    /**
     * Wird zurückgegeben, wenn das Objekt mit der Unterseite des anderen Objektes kollidiert
     */
    public static final int COLLISION_WITH_BOTTOM = 1;
    
    /**
     * Wird zurückgegeben, wenn das Objekt mit der rechten Seite des anderen Objektes kollidiert
     */
    public static final int COLLISION_WITH_RIGHT_SIDE = 2;
    
    /**
     * Wird zurückgegeben, wenn das Objekt mit der linken Seite des anderen Objektes kollidiert
     */
    public static final int COLLISION_WITH_LEFT_SIDE = 3;
    
    /**
     * Speichert, ob die Hitbox durchlässig oder nicht ist.
     * <br>
     * <br>
     * Eine durchlässige Hitbox ermöglicht, dass eine andere Hitbox durch diese durch kann und dennoch
     * eine Kollision zurückgibt.
     * Das kann nützlich sein, um beispielsweise Aktionsfelder zu erstellen.
     */
    private boolean permeable = false;
    
    /**
     * Erstellt eine neue Hitbox.
     *
     * @param x       horizontale Position (Welt)
     * @param y       vertikale Position (Welt)
     * @param width   Weite (Weltmaßstab)
     * @param height  Höhe (Weltmaßstab)
     */
    public Hitbox(float x, float y, float width, float height)
    {
        super(x, y, width, height, new ImageIcon());
    }
    
    /**
     * Gibt die Kollisionsart dieser Hitbox mit der anderen zurück, wenn eine stattfindet.
     * Dabei gibt es folgende Ganzzahl Werte:
     *
     * <li> -1: wenn keine Kollision stattfindet
     * <li> 0: wenn das Objekt mit der Oberseite des zweiten Objects kollidiert
     * <li> 1: wenn das Objekt mit der Unterseite des zweiten Objects kollidiert
     * <li> 2: wenn es mit der rechten seite des zweiten Objects kollidiert
     * <li> 3: wenn es mit der linken seite des zweiten Objects kollidiert
     * <br>
     * <br>
     * Die Kollision im Detail läuft wie folgt ab.
     * Es wird überprüft, ob eine Hitbox mit einer anderen kollidiert. Wenn dem so ist, dann
     * wird überprüft, welche Art von kollision stattfindet. Dabei hat die andere Hitbox Bereiche,
     * in dem die Hitbox sein muss, damit festgelegt werden kann welche Kollision stattfindet.
     *
     * @param hitbox Die andere Hitbox, mit der diese kollidieren soll
     * @return ob und welche Kollision stattfindet
     */
    public int getCollision(Hitbox hitbox)
    {
        //Notwendige Bedingung: hitbox != null
        if(hitbox == null) throw new noValueException("Die übergebene Hitbox ist null", getUID());
        
        Hitbox obj1 = this;
        GameObject obj2 = hitbox;
        
        if(obj2 != obj1)
        {
            //Überprüfe, ob die beiden Hitboxen überhaupt kollidieren
            if(obj1.getX() > obj2.getX() - obj1.getWidth()
            && obj1.getY() > obj2.getY() - obj1.getHeight()
            && obj1.getX() < obj2.getX() + obj2.getWidth()
            && obj1.getY() < obj2.getY() + obj2.getHeight())
            {
                //Überprüfe, ob Kollision von oben
                if(obj1.getY() + obj1.getHeight() - (obj1.getHeight() / 4) <= obj2.getY()) return COLLISION_WITH_TOP;
                //Überprüfe, ob Kollision von unten
                if(obj1.getY() + (obj1.getHeight() / 4) >= obj2.getY() + obj2.getHeight()) return COLLISION_WITH_BOTTOM;
                //Überprüfe, ob Kollision von rechts
                if(obj1.getX() >= obj2.getX() + (obj2.getWidth() / 2)) return  COLLISION_WITH_RIGHT_SIDE;
                //Überprüfe, ob Kollision von links
                if(obj1.getX() <= obj2.getX() + (obj2.getWidth() / 2)) return COLLISION_WITH_LEFT_SIDE;
            }
        }
    
        //Keine Kollision
        return -1;
    }
    
    /**
     * Gibt die Hitboxen zurück, mit dem diese Hitbox kollidiert.
     * @return Hitbox
     */
    public Hitbox[] getCollidingHitboxes()
    {
        ArrayList<Hitbox> hitboxes = new ArrayList<>();
        
        for (int i = 0; i < getLoadedGameObjects().size(); i++)
        {
            GameObject obj1 = this;
            GameObject obj2 = getLoadedGameObjects().get(i);
    
            if (obj2 instanceof Hitbox && obj1 != obj2)
            {
                //Überprüfe, ob die beiden Hitboxen überhaupt kollidieren
                if (obj1.getX() > obj2.getX() - obj1.getWidth()
                        && obj1.getY() > obj2.getY() - obj1.getHeight()
                        && obj1.getX() < obj2.getX() + obj2.getWidth()
                        && obj1.getY() < obj2.getY() + obj2.getHeight())
                {
                    hitboxes.add((Hitbox) obj2);
                }
            }
        }
        
        return hitboxes.toArray(new Hitbox[0]);
    }
    
    /**
     * Gibt das Objekt zurück, welches diese Hitbox besitzt.
     * <br>
     * <br>
     * Das ist die umgekehrte Funktion wie die getHitbox() Funktion eines Objektes.
     * Anstatt das die Hitbox eines Objektes zurück gegeben wird, wird das Objekt einer Hitbox zurückgegeben.
     *
     * @return Objekt einer Hitbox.
     * @see GameObject
     */
    public GameObject getObject()
    {
        for(int i = 0; i < GameObject.getLoadedGameObjects().size(); i++)
            if(GameObject.getLoadedGameObjects().get(i).getHitbox() != null)
                if(GameObject.getLoadedGameObjects().get(i).getHitbox() == this)
                    return GameObject.getLoadedGameObjects().get(i);
        
        return null;
    }
    
    /**
     * Setzt die Durchlässigkeit einer Hitbox.
     * <br>
     * <br>
     * Eine durchlässige Hitbox ermöglicht, dass eine andere Hitbox durch diese durch kann und dennoch
     * eine Kollision zurückgibt.
     * Das kann nützlich sein, um beispielsweise Aktionsfelder zu erstellen.
     *
     * @param aFlag true: Hitbox ist durchlässig; false: Hitbox ist undurchlässig.
     */
    public void setPermeability(boolean aFlag)
    {
        this.permeable = aFlag;
    }
    
    /**
     * Gibt zurück, ob die Hitbox durchlässig oder undurchlässig ist.
     * <br>
     * <br>
     * Eine durchlässige Hitbox ermöglicht, dass eine andere Hitbox durch diese durch kann und dennoch
     * eine Kollision zurückgibt.
     * Das kann nützlich sein, um beispielsweise Aktionsfelder zu erstellen.
     *
     * @return Gibt zurück, ob die Hitbox durchlässig oder undurchlässig ist
     * @see #permeable
     */
    public boolean getPermeability()
    {
        return this.permeable;
    }
}
