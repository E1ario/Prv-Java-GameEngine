package testProgramm.blocks;

import javax.swing.*;

/**
 * Erstellt einen durchlässigen Block. Dieser Block hat eine etwas dunklere Textur, als normale Blöcke.
 */
public class TransparentBlock extends Block
{
    public TransparentBlock(float x, float y, float width, float height, ImageIcon texture)
    {
        super(x, y, width, height, texture);
        getHitbox().setPermeability(true);
    }
}
