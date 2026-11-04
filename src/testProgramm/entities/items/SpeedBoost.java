package testProgramm.entities.items;

import testProgramm.entities.*;
import testProgramm.io.*;

import javax.swing.*;

/**
 * Das Speedboost Item ist ein Powerup. Sobald der Spieler es einsammelt, erhöht sich seine Laufgeschwindigkeit.
 * Der Betrag der Geschwindigkeit ist in ressource/config.properties zu finden. Die Textur für dieses Powerup ist unter
 * ressource/textures/entities/items/speedBoost.png abgespeichert.
 */
public class SpeedBoost extends Item
{
    /**
     * Ein neues Properties Objekt, damit man auf Properties Dateien zugreifen kann.
     */
    private static Properties config = new Properties("ressource/config.properties");
    
    /**
     * Die zusätzliche Geschwindigkeit, die der Spieler bekommt.
     */
    public static final float boost = config.getFloat("object.entity.item.powerup.speedBoost.boost");
    
    /**
     * Erstellt ein neues Geschwindigkeit-Powerup.
     *
     * @param x       horizontale Position in der Welt
     * @param y       vertikale Position in der Welt
     * @param width   Weite im Weltmaßstab
     * @param height  Höhe im Weltmaßstab
     */
    public SpeedBoost(float x, float y, float width, float height)
    {
        super(x, y, width, height, new ImageIcon("ressource/textures/entities/items/speedBoost.png"));
    }
}
