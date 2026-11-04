package testProgramm.blocks;

import engine.object.*;

import javax.swing.*;

/**
 * Ein Block, ist ein GameObject, mit einer Hitbox.
 * Jedes Lebewesen kann darauf laufen, sie jedoch nicht zerstören oder mit ihnen Interagieren.
 * <br>
 * <br>
 * Die Texturen für die Blöcke sind unter ressource/textures/blocks abgespeichert.
 */
public class Block extends GameObject
{
    
    /**
     * Erstellt ein neues Game Object und eine entsprechende Hitbox dazu.
     *
     * @param x       horizontale Position in der Welt
     * @param y       vertikale Position in der Welt
     * @param width   Weite im Weltmaßstab
     * @param height  Höhe im Weltmaßstab
     * @param texture Textur des Blocks
     */
    public Block(float x, float y, float width, float height, ImageIcon texture)
    {
        super(x, y, width, height, texture);
        setHitbox(new Hitbox(x, y, width, height));
    }
}
