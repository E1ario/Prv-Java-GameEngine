package testProgramm.windowComponents;

import engine.sound.*;

import javax.swing.*;

/**
 * Diese Klasse überschreibt die Button Klasse,
 * damit ein Sound abgespielt werden kann. Dieser Sound
 * wird abgespielt, sobald der Knopf ausgewählt wird.
 */
public abstract class Button extends engine.windowComponents.Button
{
    private final Sound hoveredSound = new Sound("ressource/audio/windowComponents/button/click.wav");
    
    public Button(float x, float y, float width, float height, ImageIcon texture, ImageIcon textureHovered, ImageIcon texturePressed)
    {
        super(x, y, width, height, texture, textureHovered, texturePressed);
    }
    
    /**
     * Spielt einen Sound ab, sobald der Knopf ausgewählt wird.
     */
    @Override
    public void onUpdate()
    {
        super.onUpdate();
        
        if(isButtonHovered()) hoveredSound.getAudioClip().start();
        else hoveredSound.reset();
    }
    
    @Override
    public abstract void action();
}
