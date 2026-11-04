package testProgramm.entities;

import engine.controller.*;
import engine.exceptions.*;
import engine.object.*;
import engine.physics.*;
import engine.sound.*;
import engine.ui.*;
import testProgramm.entities.items.*;
import testProgramm.io.Properties;

import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class Player extends Entity
{
    /**
     * Ein neues Properties Objekt, damit man Properties Dateien Lesen kann.
     */
    private static Properties conf = new Properties("ressource/config.properties");
    
    /**
     * Zustand des Spielers, wenn er weder eine Aktion ausführt, noch sich bewegt und nicht in der Luft ist.
     */
    public static final int STATE_IDLE = 0;
    
    /**
     * Zustand des Spielers, wenn er läuft.
     */
    public static final int STATE_WALKING = 1;
    
    /**
     * Zustand des Spielers, wenn er in der Luft ist bzw. den Boden nicht berührt.
     */
    public static final int STATE_IN_AIR = 2;
    
    /**
     * Zustand, wenn er schlägt.
     */
    public static final int STATE_ATTACKING = 3;
    
    /**
     * Richtung des Spielers, wenn er nach rechts schaut.
     */
    public static final int DIRECTION_RIGHT = 0;
    
    /**
     * Richtung des Spielers, wenn er nach links schaut.
     */
    public static final int DIRECTION_LEFT = 1;
    
    /**
     * Das Inventar des Spielers. Diese Liste speichert alle
     * aufgehobenen Items.
     */
    private ArrayList<Item> collectedItems = new ArrayList<>();
    
    private final Sound walkingSound = new Sound("ressource/audio/entities/player/step.wav");
    
    
    /**
     * Laufgeschwindigkeit des Spielers.
     */
    private float walkingSpeed = conf.getFloat("object.entity.player.walking_speed");
    
    /**
     * Der derzeitige Zustand des Spielers.
     */
    private int state = conf.getInt("object.entity.player.default_state");
    
    /**
     * Die derzeitige Richtung in die der Spieler schaut.
     */
    private int direction = conf.getInt("object.entity.player.default_direction");
    
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
    private int animation_stop_duration = conf.getInt("object.entity.player.animation_pause_duration");
    
    /**
     * Die Sprunghöhe des Spielers im Weltmaßstab.
     */
    private float jumpHeight = conf.getFloat("object.entity.player.jump_height");
    
    /**
     * Erstellt einen neuen Spieler.
     *
     * @param x       horizontale Position in der Welt
     * @param y       vertikale Position in der Welt
     * @param width   Weite im Weltmaßstab
     * @param height  Höhe im Weltmaßstab
     */
    public Player(float x, float y, float width, float height)
    {
        super(x, y, width, height, new ImageIcon());
        
        setHitbox(new Hitbox(x, y, width, height));
        setGravitation(new Gravitation(conf.getFloat("object.entity.player.gravitation_acceleration"), Gravitation.DIRECTION_DOWN));
    }
    
    /**
     * Diese Methode sorgt für die Interaktion und die "Belebung" des Spielers.
     * Hier werden Tasteninputs verarbeitet, Animiert und Items verarbeitet. Zudem wird auch Bewegt.
     */
    @Override
    public void onUpdate()
    {
        processKeyInputs();
        animate();
        processItems();
        processSound();
        
        super.onUpdate();
    }
    
    /**
     * Gibt je nach Aktion des Spielers einen Sound zurück.
     */
    private void processSound()
    {
        if(state == STATE_WALKING)
        {
            if(walkingSound.getAudioClip().getFramePosition() == walkingSound.getAudioClip().getFrameLength()) walkingSound.reset();
            walkingSound.getAudioClip().start();
        }
    }
    
    /**
     * Überprüft welche Items der Spieler aufgesammelt hat und gibt einen entsprechenden Effekt.
     * Beispielsweise das Geschwindigkeit-Powerup, welches die Geschwindigkeit des Spielers erhöht.
     */
    private void processItems()
    {
        //Item Effekte geben
        for(int i = 0; i < getCollectedItems().size(); i++)
        {
            if(getCollectedItems().get(i) instanceof SpeedBoost) walkingSpeed = conf.getFloat("object.entity.player.walking_speed") + SpeedBoost.boost;
        }
    }
    
    /**
     * Animiert den Spieler.
     * Je nach Aktion oder Zustand des Spielers wird eine entsprechende Animation ausgeführt.
     */
    public void animate()
    {
        //ZUSTAND: setzen
        if (state != STATE_ATTACKING)
        {
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
            if (!isPlayerOnGround()) state = STATE_IN_AIR;
        }
    
    
        //ZUSTAND: STEHEN
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
        
        //ZUSTAND: LAUFEN
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
        
        //ZUSTAND: SPRINGEN / FALLEN
        if (state == STATE_IN_AIR)
        {
            if (direction == DIRECTION_RIGHT)
                setTexture(new ImageIcon("ressource/textures/entities/player/jumping/jumping_right.png"));
            if (direction == DIRECTION_LEFT)
                setTexture(new ImageIcon("ressource/textures/entities/player/jumping/jumping_left.png"));
        }
        
        
        //ZUSTAND: SCHLAGEN
        if (state == STATE_ATTACKING)
        {
            if (direction == DIRECTION_RIGHT)
            {
                setTexture(new ImageIcon("ressource/textures/entities/player/attack/attack_right_frame" + frame + ".png"));
                setWidth(getTexture().getIconWidth());
            }
            if (direction == DIRECTION_LEFT)
            {
                float width = getTexture().getIconWidth();
                setTexture(new ImageIcon("ressource/textures/entities/player/attack/attack_left_frame" + frame + ".png"));
                setX(getX() - (getTexture().getIconWidth() - width));
                setWidth(getTexture().getIconWidth());
            }
            if (frame == 8)
                state = STATE_IDLE;
        }
        
        //Animation verlängern:
        if(pause_frame > animation_stop_duration)
            pause_frame = 0;
        else pause_frame++;
        
        //Animationsframe erhöhen
        if(pause_frame == 0)
        {
            if (frame >= 8)
                frame = 1;
            else
                frame++;
        }
    }
    
    /**
     * Sorgt dafür, dass Tastenschläge verarbeitet werden können.
     */
    private void processKeyInputs()
    {
        Controller c = new Controller();
    
        setVelX(0);
        
        if (c.getKeysPressed().contains(KeyEvent.VK_A))
            setVelX(-walkingSpeed);
        if (c.getKeysPressed().contains(KeyEvent.VK_D))
            setVelX(walkingSpeed);
        if (c.getKeysPressed().contains(KeyEvent.VK_SPACE))
            if(isPlayerOnGround()) jump(jumpHeight);
        
        if (c.getKeysPressed().contains(KeyEvent.VK_L))
        {
            state = STATE_ATTACKING;
            frame = 1;
        }
        if(c.getKeysPressed().contains(KeyEvent.VK_UP))
        {
            for(int i = 0; i < getLoadedGameObjects().size(); i++)
                if(getLoadedGameObjects().get(i) instanceof Camera)
                {
                    Camera cam = (Camera) getLoadedGameObjects().get(i);
                    cam.setZoom(cam.getZoom() + conf.getFloat("ui.camera.zoomFactor"));
                }
        }
        if(c.getKeysPressed().contains(KeyEvent.VK_DOWN))
        {
            for(int i = 0; i < getLoadedGameObjects().size(); i++)
                if(getLoadedGameObjects().get(i) instanceof Camera)
                {
                    Camera cam = (Camera) getLoadedGameObjects().get(i);
                    cam.setZoom(cam.getZoom() - conf.getFloat("ui.camera.zoomFactor"));
                }
        }
    }
    
    /**
     * Überprüft, ob der Spieler auf dem Boden ist.
     * @return true wenn ja, false wenn er in der Luft ist.
     */
    private boolean isPlayerOnGround()
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
     * Gibt das Inventar des Spielers zurück.
     *
     * @return Liste an gesammelten Items
     */
    public ArrayList<Item> getCollectedItems()
    {
        return collectedItems;
    }
}