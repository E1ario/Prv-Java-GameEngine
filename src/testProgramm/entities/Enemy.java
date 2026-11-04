package testProgramm.entities;

import engine.object.*;
import engine.physics.*;
import testProgramm.io.*;

import javax.swing.*;

/**
 * Der Gegner ist ein vom Computer gesteuerter Spieler.
 * Dieser hat lediglich die Aufgabe den Spieler zu verfolgen.
 */
public class Enemy extends Entity
{
    /**
     * Für den Zugriff auf Properties-Dateien.
     */
    Properties conf = new Properties("ressource/config.properties");
    
    /**
     * Zustand des Gegners, wenn er still steht.
     */
    private static final int STATE_IDLE = 0;
    
    /**
     * Zustand des Gegners, wenn er läuft.
     */
    private static final int STATE_WALKING = 1;
    
    /**
     * Zustand des Gegners, wenn er in der Luft ist, also entweder fällt oder springt.
     */
    private static final int STATE_IN_AIR = 2;
    
    /**
     * Richtung des Gegners, wenn er nach rechts schaut.
     */
    private static final int DIRECTION_RIGHT = 0;
    
    /**
     * Richtung des Gegeners, wenn er nach links schaut.
     */
    private static final int DIRECTION_LEFT = 1;
    
    /**
     * Laufgeschwindigkeit des Gegners
     */
    private  float walkingSpeed = conf.getFloat("object.entity.enemy.walking_speed");
    
    /**
     * Der derzeitige Zustand des Gegners, also ob er in der Luft ist oder auf dem Boden etc.
     */
    private int state = conf.getInt("object.entity.enemy.default_state");
    
    /**
     * Die derzeitige Richtung in die der Gegner schaut
     */
    private int direction = conf.getInt("object.entity.enemy.default_direction");
    
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
    private int animation_stop_duration = conf.getInt("object.entity.enemy.animation_pause_duration");
    
    /**
     * Sprunghöhe des Gegners im Weltmaßstab.
     */
    private float jumpHeight = conf.getFloat("object.entity.enemy.jump_height");
    
    /**
     * Der Radius, in dem der Gegner den Spieler nicht verfolgt.
     */
    private float stop_radius = conf.getFloat("object.entity.enemy.stop_radius");
    
    /**
     * Das Objekt, welches der Gegner verfolgen soll.
     */
    private GameObject toFollow;
    
    /**
     * Erstellt einen neuen Gegner.
     *
     * @param x       horizontale Position in der Welt
     * @param y       vertikale Position in der Welt
     * @param width   Weite im Weltmaßstab
     * @param height  Höhe im Weltmaßstab
     * @param toFollow Objekt, welches verfolgt wird
     */
    public Enemy(float x, float y, float width, float height, GameObject toFollow)
    {
        super(x, y, width, height, new ImageIcon());
        this.toFollow = toFollow;
        
        setHitbox(new Hitbox(x, y, width, height));
        setGravitation(new Gravitation(conf.getFloat("object.entity.enemy.gravitation_acceleration"), Gravitation.DIRECTION_DOWN));
    }
    
    /**
     * Updatet den Gegner.
     * Hier wird der Gegner zum "Leben" erweckt, in dem er läuft, Hindernisse überwindet
     * und animiert wird.
     */
    @Override
    public void onUpdate()
    {
        super.onUpdate();
        
        //Spieler verfolgen
        if(getX() - toFollow.getX() < stop_radius && toFollow.getX() - getX() < stop_radius) setVelX(0);
        else if(getX() > toFollow.getX()) setVelX(-walkingSpeed);
        else setVelX(walkingSpeed);
        
        //Hindernisse überwinden
        if(isWayBlocked() && isOnGround()) jump(jumpHeight);
        
        animate();
    }
    
    /**
     * Animiert den Gegner je nach Zustand oder Aktion die er macht.
     */
    public void animate()
    {
        //State setzen
        if (getVelX() == 0 && getVelY() == 0)
            state = STATE_IDLE;
        if (getVelX() > 0)
        {
            direction = DIRECTION_RIGHT;
            state = STATE_WALKING;
        }
        if (getVelX() < 0)
        {
            direction = DIRECTION_LEFT;
            state = STATE_WALKING;
        }
        if (!isOnGround()) state = STATE_IN_AIR;
        
        
        //Status: STEHEN
        if (state == STATE_IDLE)
        {
            if (direction == DIRECTION_RIGHT)
            {
                if (frame == 1)
                    setTexture(new ImageIcon("ressource/textures/entities/player/idle/idle_right_frame1.png"));
                if (frame == 4)
                    setTexture(new ImageIcon("ressource/textures/entities/player/idle/idle_right_frame2.png"));
            }
            if (direction == DIRECTION_LEFT)
            {
                if (frame == 1)
                    setTexture(new ImageIcon("ressource/textures/entities/player/idle/idle_left_frame1.png"));
                if (frame == 4)
                    setTexture(new ImageIcon("ressource/textures/entities/player/idle/idle_left_frame2.png"));
            }
        }
        
        //STATUS: LAUFEN
        if (state == STATE_WALKING)
        {
            if (direction == DIRECTION_RIGHT)
            {
                if (frame == 1)
                    setTexture(new ImageIcon("ressource/textures/entities/player/walking/walking_right_frame1.png"));
                if (frame == 2)
                    setTexture(new ImageIcon("ressource/textures/entities/player/walking/walking_right_frame2.png"));
                if (frame == 3)
                    setTexture(new ImageIcon("ressource/textures/entities/player/walking/walking_right_frame3.png"));
                if (frame == 4)
                    setTexture(new ImageIcon("ressource/textures/entities/player/walking/walking_right_frame4.png"));
                if (frame == 5)
                    setTexture(new ImageIcon("ressource/textures/entities/player/walking/walking_right_frame1.png"));
                if (frame == 6)
                    setTexture(new ImageIcon("ressource/textures/entities/player/walking/walking_right_frame2.png"));
                if (frame == 7)
                    setTexture(new ImageIcon("ressource/textures/entities/player/walking/walking_right_frame3.png"));
                if (frame == 8)
                    setTexture(new ImageIcon("ressource/textures/entities/player/walking/walking_right_frame4.png"));
            }
            if (direction == DIRECTION_LEFT)
            {
                if (frame == 1)
                    setTexture(new ImageIcon("ressource/textures/entities/player/walking/walking_left_frame1.png"));
                if (frame == 2)
                    setTexture(new ImageIcon("ressource/textures/entities/player/walking/walking_left_frame2.png"));
                if (frame == 3)
                    setTexture(new ImageIcon("ressource/textures/entities/player/walking/walking_left_frame3.png"));
                if (frame == 4)
                    setTexture(new ImageIcon("ressource/textures/entities/player/walking/walking_left_frame4.png"));
                if (frame == 5)
                    setTexture(new ImageIcon("ressource/textures/entities/player/walking/walking_left_frame1.png"));
                if (frame == 6)
                    setTexture(new ImageIcon("ressource/textures/entities/player/walking/walking_left_frame2.png"));
                if (frame == 7)
                    setTexture(new ImageIcon("ressource/textures/entities/player/walking/walking_left_frame3.png"));
                if (frame == 8)
                    setTexture(new ImageIcon("ressource/textures/entities/player/walking/walking_left_frame4.png"));
            }
        }
        
        //STATUS: SPRINGEN / FALLEN
        if (state == STATE_IN_AIR)
        {
            if (direction == DIRECTION_RIGHT)
                setTexture(new ImageIcon("ressource/textures/entities/player/jumping/jumping_right.png"));
            if (direction == DIRECTION_LEFT)
                setTexture(new ImageIcon("ressource/textures/entities/player/jumping/jumping_left.png"));
        }
    
        //Pause für die Animation
        if(pause_frame > animation_stop_duration)
            pause_frame = 0;
        else pause_frame++;
    
        //Frame erhöhen
        if(pause_frame == 0)
        {
            if (frame >= 8)
                frame = 1;
            else
                frame++;
        }
    }
    
    /**
     * Überprüft, ob der Gegner auf dem Boden ist.
     * @return true, wenn der Gegner auf dem Boden is, false wenn nicht.
     */
    private boolean isOnGround()
    {
        int collision = -1;
        
        getHitbox().setY(getHitbox().getY() + 1);
        
        for(int i = 0; i < getHitbox().getCollidingHitboxes().length; i++)
            if(!getHitbox().getCollidingHitboxes()[i].getPermeability())
                collision = getHitbox().getCollision(getHitbox().getCollidingHitboxes()[i]);
        
        getHitbox().setY(getHitbox().getY() - 1);
        
        return collision == Hitbox.COLLISION_WITH_TOP;
    }
    
    /**
     * Überprüft, ob unmittelbar vor dem Gegner ein Hindernis ist.
     * @return true, Weg ist blockiert; false Weg ist frei
     */
    private boolean isWayBlocked()
    {
        int collision = -1;
        
        if(direction == DIRECTION_LEFT)
        {
            getHitbox().setX(getHitbox().getX() - 1);
    
            for(int i = 0; i < getHitbox().getCollidingHitboxes().length; i++)
                if(!getHitbox().getCollidingHitboxes()[i].getPermeability())
                    collision = getHitbox().getCollision(getHitbox().getCollidingHitboxes()[i]);
    
            getHitbox().setX(getHitbox().getX() + 1);
    
            return collision == Hitbox.COLLISION_WITH_RIGHT_SIDE;
        }
        if(direction == DIRECTION_RIGHT)
        {
            getHitbox().setX(getHitbox().getX() + 1);
    
            for(int i = 0; i < getHitbox().getCollidingHitboxes().length; i++)
                if(!getHitbox().getCollidingHitboxes()[i].getPermeability())
                    collision = getHitbox().getCollision(getHitbox().getCollidingHitboxes()[i]);
    
            getHitbox().setX(getHitbox().getX() - 1);
    
            return collision == Hitbox.COLLISION_WITH_LEFT_SIDE;
        }
        
        return false;
    }
}