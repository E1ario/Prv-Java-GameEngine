package testProgramm.blocks;

import engine.object.*;
import engine.sound.*;
import testProgramm.entities.*;
import testProgramm.io.*;

import javax.swing.*;

/**
 * Die Spring ist ein Block, welches ein Lebewesen Federn kann.
 * <br>
 * <br>
 * Sobald ein Lebewesen auf diesen Block springt, wird dieses hoch gefedert.
 * Jede Feder kann dabei ihre eigene Sprunghöhe haben. Die Texturen für die Feder findet man unter
 * ressource/textures/blocks/spring.
 */
public class Spring extends Block
{
    /**
     * Ein neues Properties Objekt, damit man auf Properties-Dateien zugreifen kann,
     */
    private static Properties conf = new Properties("ressource/config.properties");
    
    private final Sound boingSound = new Sound("ressource/audio/blocks/spring/boing.wav");
    
    /**
     * Damit das Lebewesen hoch gefedert werden kann, muss die Spring ermitteln, ob das Lebewesen
     * auf der Feder ist.
     * <br>
     * Das ermittelt die trigger Area. Sie ist eine durchlässige Hitbox, welche über der Sprungfeder ist
     * und ermittelt, ob das Lebewesen mit dieser Hitbox kollidiert.
     */
    private Hitbox triggerArea;
    
    /**
     * Das ist die Höhe, die das Lebewesen gefedert wird im Weltmaßstab.
     */
    private float boostHeight;
    
    
    /**
     * Erstellt ein neues Game Object und eine entsprechende Hitbox dazu.
     *
     * @param x       horizontale Position in der Welt
     * @param y       vertikale Position in der Welt
     * @param width   Weite im Weltmaßstab
     * @param height  Höhe im Weltmaßstab
     */
    public Spring(float x, float y, float width, float height, float boostHeight)
    {
        super(x, y, width, height, new ImageIcon("ressource/textures/blocks/spring/spring_1.png"));
        
        this.boostHeight = boostHeight;
        
        //Setze Trigger Area
        triggerArea = new Hitbox(x, y - height, width, height);
        triggerArea.setPermeability(true);
    }
    
    
    /**
     * Jedes Update wird überprüft, ob ein Lebewesen auf der Spring ist
     * und federt es gegebenenfalls.
     * Sobald eine Federung zustande kommt, wird die Spring animiert.
     */
    @Override
    public void onUpdate()
    {
        //trigger Area und Spring zusammen halten, falls Spring verschoben wird
        triggerArea.setX(getX());
        triggerArea.setY(getY() - getHeight());
        
        setTexture(new ImageIcon("ressource/textures/blocks/spring/spring_2.png"));
        
        //Jedes Entity soll automatisch springen, wenn es die Hitbox der Feder berührt.
        for(int i = 0; i < triggerArea.getCollidingHitboxes().length; i++)
            if(triggerArea.getCollidingHitboxes()[i].getObject() instanceof Player)
            {
                ((Entity) triggerArea.getCollidingHitboxes()[i].getObject()).jump(boostHeight);
                setTexture(new ImageIcon("ressource/textures/blocks/spring_1.png"));
                boingSound.getAudioClip().start();
            }
        if(triggerArea.getCollidingHitboxes().length == 0) boingSound.reset();
    }
}
