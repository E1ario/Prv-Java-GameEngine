package testProgramm.entities;

import engine.object.*;
import engine.physics.*;
import testProgramm.io.*;

import javax.swing.*;

/**
 * Ein Item ist ein Gegenstand mit Gravitation und Kollision, welches vom Spieler aufgehoben werden kann.
 * Dieses Item wird dem Spieler in ein Inventar hinzugefügt. Je nach Gegenstand kann das auch einen Einfluss auf das Spiel nehmen.
 * Beispielsweise das Geschwindigkeit Powerup.
 */
public class Item extends Entity
{
    /**
     * Eine neues Properties Objekt, damit man auf Properties Dateien zugreifen kann.
     */
    Properties conf = new Properties("ressource/config.properties");
    
    
    /**
     * Erstellt ein neues Item.
     *
     * @param x       horizontale Position in der Welt
     * @param y       vertikale Position in der Welt
     * @param width   Weite im Weltmaßstab
     * @param height  Höhe im Weltmaßstab
     * @param texture Textur des Items
     */
    public Item(float x, float y, float width, float height, ImageIcon texture)
    {
        super(x, y, width, height, texture);
        setHitbox(new Hitbox(x, y, width, height));
        getHitbox().setPermeability(true);
        setGravitation(new Gravitation(conf.getFloat("object.entity.item.gravitation_acceleration"), Gravitation.DIRECTION_DOWN));
    }
    
    /**
     * Sorgt dafür, dass jedes Item vom Spieler aufgehoben werden kann.
     * Die Methode steht hier beim Item, da somit das Item vom Spieler aufgehoben wird,
     * und nicht der Spieler das Item aufhebt.
     *
     * Wenn der Spieler das Item aufhebt (sprich der untere Code steht beim Spieler selber), dann resultiert das
     * in einem Wegschieben des Items vom Spieler, obwohl die Hitbox durchlässig ist. Das liegt daran, weil ein Item ein Lebewesen ist
     * und jedes Lebewesen sich bei einer Kollision mit einem anderen Objekt "wegdrückt". Somit würde der Spieler, da er später erstellt wurde
     * und demnach später bei der Kollisionsabfrage ist, das Item vor sich herschieben.
     *
     * Wenn jedoch das Item vom Spieler aufgehoben wird (der Code für das Aufheben beim Item steht), dann wird zuerst überprüft,
     * ob das Item aufgehoben werden kann und dann erst bei Kollision "weggedrückt". Wenn jetzt ein Item vom Spieler aufgehoben wird, dann
     * wird es zuerst aufgenommen und anschließend gelöscht und wird nicht mehr verschoben. Somit sieht man auch nicht, dass der Spieler das
     * Item vor sich herschiebt.
     */
    @Override
    public void onUpdate()
    {
        for(int i = 0; i < getHitbox().getCollidingHitboxes().length; i++)
            if(getHitbox().getCollidingHitboxes()[i].getObject() instanceof Player)
            {
                ((Player) getHitbox().getCollidingHitboxes()[i].getObject()).getCollectedItems().add(this);
                remove();
            }
        
        super.onUpdate();
    }
}
