package engine.object;

import engine.exceptions.*;
import engine.physics.*;
import engine.ui.*;

import javax.swing.*;

/**
 * Ein Entity (Lebewesen) ist ein GameObject welches sich bewegen kann.
 * <br>
 * <br>
 * Wie schon im Grundsatz dieser Engine definiert, kann jedes GameObjekt erweitert werden,
 * damit man mehr Funktionen oder Eigenschaften hinzufügen kann.
 * Das Entity ist eine solche Erweiterung. Es besitzt die Möglichkeit bewegt zu werden.
 * Dies funktioniert über Geschwindigkeiten. Zudem kann dadurch auch die Physik an einem Entity
 * wirkung zeigen.
 *
 * @see #move()
 * @see GameObject
 * @see engine.physics.Gravitation
 */
public class Entity extends GameObject
{
    /**
     * Geschwindigkeit in horizontaler Richtung (Weltmaßstab).
     * Ist die Geschwindigkeit positiv, so bewegt sich das Lebewesen nach rechts.
     */
    float velX;
    
    /**
     * Geschwindigkeit in vertikaler Richtung (Weltmaßstab).
     * Ist die Geschwindigkeit positiv, so bewegt sich das Lebewesen nach unten.
     */
    float velY;
    
    /**
     * Die Gravitation, die auf dieses Objekt wirkt.
     */
    Gravitation gravitation;
    
    /**
     * Erstellt ein neues Entity.
     *
     * @param x       horizontale Position
     * @param y       vertikale Position
     * @param width   Weite
     * @param height  Höhe
     * @param texture Textur
     */
    public Entity(float x, float y, float width, float height, ImageIcon texture)
    {
        super(x, y, width, height, texture);
    }
    
    /**
     * Updatet das Entity, falls es geladen ist.
     * <br>
     * <br>
     * Nur geladene Objekte werden geupdatet und gerendert.
     * Sobald dies bei einem Entity der Fall ist, kann es mithilfe der Geschwindigkeiten
     * bewegt werden.
     * <br>
     * Das bedeutet auch, dass wenn diese Methode überschrieben wird, man darauf achten muss,
     * dass die Bewegungsfunktion getriggert wird.
     *
     * @see #move()
     */
    @Override
    public void onUpdate()
    {
        move();
    }
    
    /**
     * Bewegt das Lebewesen.
     * <br>
     * <br>
     * Diese Funktion aktualisiert die Position des Spielers, in abhängigkeit
     * dessen Geschwindigkeit. Sobald eine Geschwindigkeit einen Wert ungleich 0 besitzt,
     * so wird das Lebewesen in die entsprechende Richtung bewegt. Das passiert dadurch, das die
     * Position mit der Geschwindigkeit addiert wird.
     * <br>
     * Außerdem wird die Gravitation und die Kollision ebenfalls berechnet.
     *
     * @see #velX
     * @see #velY
     * @see engine.physics.Gravitation
     * @see Hitbox
     */
    public void move()
    {
        //Das Movement wird, wegen der hohen Geschwindigkeiten, in Quater-Steps unterteilt.
        //Jeder Quater-Step bekommt dann seine eigene Kollisionsabfrage, einmal wenn das Lebewesen sich
        //in horizontaler Richtung bewegt hat und ein andermal, wenn es sich in vertikaler Richtung bewegt hat.
        //Diese Unterteilung ist wichtig, da ansonsten die kollision nicht richtig funktionieren würde.
        for(int i = 0; i < 4; i++)
        {
            //X Position verändern
            setX(getX() + getVelX()/ 4);
    
            //Hitbox des Entities "mitziehen"
            if(getHitbox() != null)
            {
                getHitbox().setX(getX());
                getHitbox().setY(getY());
            }
            
            manageCollision();
            
            //Y Position verändern
            setY(getY() + getVelY() / 4);
    
            //Hitbox des Entities "mitziehen"
            if(getHitbox() != null)
            {
                getHitbox().setX(getX());
                getHitbox().setY(getY());
            }
            
            if(getGravitation() != null && i == 3) getGravitation().fall(this);
            manageCollision();
        }
    }
    
    /**
     * Diese Funktion kümmert sich darum, ob das Entity mit anderen kollidiert
     * und verschiebt es entsprechend.
     * <br>
     * <br>
     * Die Kollision in diesem Spiel funktioniert über Hitboxen. Jedes GameObject hat die möglichkeit eine
     * Hitbox angeheftet zu bekommen. Diese Hitbox kann dann überprüfen, ob eine Kollision mit einer anderen Hitbox
     * vorliegt. Ist dem so, so wird die Hitbox entsprechen verschoben, bis sie nicht mehr mit der anderen Hitbox
     * kollidiert. Das dazugehörige GameObject wird dann mit der Position der Hitbox gleichgesetzt.
     *
     * @see Hitbox
     */
    public void manageCollision()
    {
        for(int i = 0; i < getHitbox().getCollidingHitboxes().length; i++)
        {
            Hitbox collidingHitbox = getHitbox().getCollidingHitboxes()[i];
            int collisionType = getHitbox().getCollision(collidingHitbox);
    
            //GameObject wird nicht verschoben, bei Kollision mit durchlässiger Hitbox:
            if (collidingHitbox != null && !collidingHitbox.getPermeability())
            {
                //Verschieben der Hitbox
                switch (collisionType)
                {
                    case Hitbox.COLLISION_WITH_TOP -> getHitbox().setY(collidingHitbox.getY() - getHitbox().getHeight());
                    case Hitbox.COLLISION_WITH_BOTTOM -> getHitbox().setY(collidingHitbox.getY() + collidingHitbox.getHeight());
                    case Hitbox.COLLISION_WITH_LEFT_SIDE -> getHitbox().setX(collidingHitbox.getX() - getHitbox().getWidth());
                    case Hitbox.COLLISION_WITH_RIGHT_SIDE -> getHitbox().setX(collidingHitbox.getX() + collidingHitbox.getWidth());
                }
        
                //Verschieben des GameObjects
                if (collisionType != -1)
                {
                    setX(getHitbox().getX());
                    setY(getHitbox().getY());
                }
            }
        }
    }
    
    /**
     * Lässt das Lebewesen um eine vorgesehene Höhe im Weltmaßstab fallen.
     *
     * @param height Höhe des Sprungs
     */
    public void jump(float height)
    {
        if(getGravitation() != null) getGravitation().jump(height);
    }
    
    /**
     * Gibt die Gravitation zurück, falls dieses Lebewesen eine besitzt.
     * @return derzeitige Gravitation; 'null' wenn keine vorhanden.
     */
    public Gravitation getGravitation()
    {
        return gravitation;
    }
    
    /**
     * Setzt die Gravitation.
     * Wenn diese 'null' ist, so wird eine noValueException geworfen.
     *
     * @param gravitation Die Gravitation die übergeben werden soll.
     */
    public void setGravitation(Gravitation gravitation)
    {
        if(gravitation != null) this.gravitation = gravitation;
        else throw new noValueException("die übergebene Gravitation ist 'null'", getUID());
    }
    
    /**
     * Gibt die derzeitige horizontale Geschwindigkeit im Weltmaßstab zurück.
     * @return horizontale Geschwindigkeit
     */
    public float getVelX()
    {
        return velX;
    }
    
    /**
     * Setzt eine neue horizontale Geschwindigkeit im Weltmaßstab.
     * @param velX neue Geschwindigkeit
     */
    public void setVelX(float velX)
    {
        this.velX = velX;
    }
    
    
    /**
     * Gibt die derzeitige vertikale Geschwindigkeit im Weltmaßstab zurück.
     *
     * @return vertikale Geschwindigkeit.
     */
    public float getVelY()
    {
        return velY;
    }
    
    /**
     * Setzt eine neue vertikale Geschwindigkeit im Weltmaßstab.
     *
     * @param velY vertikale Geschwindigkeit
     */
    public void setVelY(float velY)
    {
        this.velY = velY;
    }
}
