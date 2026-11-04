package engine.physics;

import engine.exceptions.*;
import engine.object.*;

/**
 * Die Klasse Gravitation kümmert sich darum, dass Entities fallen bzw. springen können.
 * <br>
 * <br>
 * Die Gravitation wird über eine Formel berechnet. Diese Formel ist vom freien fall: s(t) = 1/2 * a * t^2
 * <br>
 * Jedem Entity kann eine eigene Gravitation hinzugefügt werden. Dadurch kann man Werte wie die Beschleunigung oder
 * die Richtung der Gravitation bestimmen.
 * <br>
 * <br>
 * Das Entity kann dann jedes Update Fallen. Das heist, dass die fall() Funktion die Geschwindigkeit für das Entity
 * automatisch setzt und kalkuliert. Vorausgesetzt wird demnach, dass jedes Objekt eine Geschwindigkeit und eine Methode hat,
 * dass es sich bewegen kann. Optimal bietet sich deswegen ein engine.object.Entity Objekt an.
 * 
 * @see engine.object.Entity
 */
public class Gravitation
{
    /**
     * Fallrichtung des Lebewesens nach unten.
     */
    public static final int DIRECTION_DOWN = 0;
    
    /**
     * Fallrichtung des Lebewesens nach links.
     */
    public static final int DIRECTION_LEFT = 1;
    
    /**
     * Fallrichtung des Lebewesens nach oben.
     */
    public static final int DIRECTION_UP = 2;
    
    /**
     * Fallrichtung des Lebewesens nach rechts.
     */
    public static final int DIRECTION_RIGHT = 3;
    
    /**
     * Der derzeitige Zeitpunkt, des Fallens/Springens.
     * <br>
     * <br>
     * Das Fallen/Springen basiert auf einer Funktion des freien Falls. Dessen Ableitung kalkuliert die Geschwindigkeit.
     * Diese Geschwindigkeit verändert sich jedoch in jedem Moment, desswegen benötigt man eine Variable, welche 
     * ständig erhöht wird.
     */
    float tick = 0;
    
    /**
     * Die Beschleunigung, des freien Falls, bzw. die Gravitationskonstante.
     */
    float acceleration;
    
    /**
     * Die Richtung der Gravitation. Standardgemäß ist diese Richtung nach unten gerichtet.
     * 
     * @see #DIRECTION_DOWN
     * @see #DIRECTION_UP
     * @see #DIRECTION_LEFT
     * @see #DIRECTION_RIGHT
     */
    int direction = 0;
    
    /**
     * Erstellt eine neue Gravitation. Jede Gravitation basiert auf der Formel für den freien Fall:#
     * s(t) = 1/2 * a * t^2.
     * <br>
     * Diese Formel besitzt die Gravitationskonstante. Zudem, kann die Richtung der Gravitation gesetzt werden.
     * 
     * @param acceleration Gravitationskonstante
     * @param direction Richtung der Gravitation
     */
    public Gravitation(float acceleration, int direction)
    {
        setAcceleration(acceleration);
        setDirection(direction);
    }
    
    /**
     * Diese Methode ist hauptverantwortlich für die Gravitation und setzt die Geschwindigkeiten damit, das Entity fällt
     * bzw. springt.
     * <br>
     * <br>
     * Die Geschwindigkeit wird über die Formel des freien Falls berechnet: s(t) = 1/2 * a * t^2.
     * <br>
     * Diese gibt die bis einem Zeitpunkt t die zurückgelegte Strecke an. Das Programm benötigt jedoch die zeitliche
     * Ableitung, da diese eine Geschwindigkeit berechnen kann, welche man dem Lebewesen jedes Update gibt.
     * Daraus ergibt sich folgende Formel für die Geschwindigkeit: v(t) = a * t.
     * <br>
     * <br>
     * Das Lebewesen fällt jedoch nur, bis es einen "Boden" (relativ zur Fallrichtung gesprochen) berührt.
     * Aus dem Grund benötigt das Entity eine hitbox, da nur darüber Kollision überprüft werden kann.
     * 
     * 
     * @param entity Das Entity, welches fallen soll.
     * @see engine.object.Entity
     * @see engine.object.Hitbox
     */
    public void fall(Entity entity)
    {
        //Notwendige Bedingung: Entity braucht Hitbox. Und darf nicht null sein.
        if(entity == null) throw new noValueException("Das Entity, welches Gravitation erfahren soll ist 'null'", -1);
        if(entity.getHitbox() == null) return;
        
        //Setzen der Fallgeschwindigkeit
        switch(direction)
        {
            case DIRECTION_DOWN:
            {
                entity.setVelY(acceleration * tick);
                tick++;
            } break;
            
            case DIRECTION_LEFT:
            {
                entity.setVelX(-acceleration * tick);
                tick++;
            } break;
            
            case DIRECTION_UP:
            {
                entity.setVelY(-acceleration * tick);
                tick++;
            } break;
            
            case DIRECTION_RIGHT:
            {
                entity.setVelX(acceleration * tick);
                tick++;
            } break;
        }
        
        //Bei Kollision: stoppen des Falls
        for(int i = 0; i < entity.getHitbox().getCollidingHitboxes().length; i++)
        {
            Hitbox collidingHitbox = entity.getHitbox().getCollidingHitboxes()[i];
            int collision = entity.getHitbox().getCollision(collidingHitbox);
    
            //Setzen der Geschwindigkeit für das Entity, je nach Gravitationsrichtung.
            switch(direction)
            {
                case DIRECTION_DOWN:
                {
                    if(collision == Hitbox.COLLISION_WITH_TOP && !collidingHitbox.getPermeability())
                    {
                        entity.setVelY(0);
                        tick = 0;
                    }
                } break;
        
                case DIRECTION_LEFT:
                {
                    if(collision == Hitbox.COLLISION_WITH_RIGHT_SIDE && !collidingHitbox.getPermeability())
                    {
                        entity.setVelX(0);
                        tick = 0;
                    }
                } break;
        
                case DIRECTION_UP:
                {
                    if(collision == Hitbox.COLLISION_WITH_BOTTOM && !collidingHitbox.getPermeability())
                    {
                        entity.setVelY(0);
                        tick = 0;
                    }
                } break;
        
                case DIRECTION_RIGHT:
                {
                    if(collision == Hitbox.COLLISION_WITH_LEFT_SIDE && !collidingHitbox.getPermeability())
                    {
                        entity.setVelX(0);
                        tick = 0;
                    }
                } break;
            }
        }
    }
    
    /**
     * Diese Methode ist verantwortlich dafür, dass ein Entity springen kann.
     * <br>
     * <br>
     * Der Fall wird mithilfe der Formel des freien Falls berechnet: s(t) = 1/2 * a * t^2.
     * <br>
     * Diese Formel gibt die bis zu einem Zeitpunkt t zurück gelegte Strecke zurück.
     * Die Formel ist eine nach oben geöffnete Parabel, welche bei (0 | 0) ihren Scheitelpunkt hat.
     * <br>
     * Diese Eigenschaft macht sich der Sprung zum Vorteil. Denn er kalkuliert die Zeit, die es benötigt
     * eine bestimmte Strecke s zu fallen. Die tick Variable wird dann dem negativen Wert dieser benötigten Zeit gesetzt.
     * Da die tick Variable stets hoch zählt, größer wird, springt das Entity zuerst bis es anschließend wieder 
     * herunterfällt.
     * 
     * @param height Höhe des Sprungs im Weltmaßstab (darf nicht negativ sein).
     * @see #fall(engine.object.Entity) 
     * @see engine.object.Entity
     * 
     */
    public void jump(float height)
    {
        if(height < 0) throw new noValueException("Die Sprunghöhe ist negativ", -1);
        
        //Berechnet die Zeit, die ein Lebewesen braucht, um eine bestimmte Höhe hochzuspringen.
        float t = (float) Math.sqrt((2f * height) / acceleration);
    
        tick = -t;
    }
    
    /**
     * Setzt eine neue Gravitationskonstante.
     * Je höher die Gravitationskonstante ist, desto schneller fällt oder springt das Entity.
     * Die Beschleunigung darf dabei nicht negativ sein.
     *
     * @param acceleration neue Gravitationskonstante
     */
    public void setAcceleration(float acceleration)
    {
        if(acceleration < 0) throw new noValueException("Die Beschleunigung ist negativ", -1);
        
        this.acceleration = acceleration;
    }
    
    /**
     * Gibt die derzeitige Gravitationskonstante zurück.
     *
     * @return derzeitige Gravitationskonstante
     */
    public float getAcceleration()
    {
        return acceleration;
    }
    
    /**
     * Setzt eine neue Richtung, in die die Gravitation läuft.
     * Mögliche Richtungen sind:
     *
     * <li> 0, Richtung nach unten
     * <li> 1, Richtung nach links
     * <li> 2, Richtung nach unten
     * <li> 3, Richtung nach rechts
     * <br>
     * <br>
     * Wenn die übergebene Richtung invalide sind, wird eine OutOfBoundsException geworfen.
     *
     * @param direction die Richtung der Gravitation
     */
    public void setDirection(int direction)
    {
        if(direction < 0 || direction > 3) throw new OutOfBoundsException("Die übergebene Richtung ist invalide (nicht im Bereich 0-3)", -1);
        this.direction = direction;
    }
    
    /**
     * Gibt die derzeitige Richtung der Gravitation zurück.
     * Mögliche Richtungen sind:
     *
     * <li> 0, Richtung nach unten
     * <li> 1, Richtung nach links
     * <li> 2, Richtung nach unten
     * <li> 3, Richtung nach rechts
     *
     * @return derzeitige Richtung
     */
    public int getDirection()
    {
        return direction;
    }
}
