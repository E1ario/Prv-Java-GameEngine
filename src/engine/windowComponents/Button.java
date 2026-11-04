package engine.windowComponents;

import engine.controller.*;
import engine.object.*;
import engine.ui.*;

import javax.swing.*;
import java.awt.event.*;

/**
 * Ein Knopf ist ein GameObject in der Welt, mit dem eine Aktion ausgeführt werden kann,
 * wenn der Maus-Zeiger ihn berührt und die linke Maustaste gedrückt wird.
 * <br>
 * <br>
 * Die Textur, sowie den Text des Knopfes muss selber gesetzt werden und muss unterteilt werden in drei
 * Kategorien:
 *
 * <li> 1. Normale Textur: Das ist die normale Textur, wenn der Knopf nicht gedrückt wird und der Maus-Zeiger über ihm ist.
 * <li> 2. Hovered Textur: Das ist die Textur, wenn der Maus-Zeiger über dem Knopf ist.
 * <li> 3. Pressed Textur: Das ist die Textur, wenn der Knopf gedrückt wurde.
 *
 * @see engine.object.GameObject
 */
public abstract class Button extends GameObject
{
    /**
     * Textur, wenn der Knopf werder gedrückt noch der Mauszeiger über ihm ist.
     */
    ImageIcon texture;
    
    /**
     * Textur, wenn der Mauszeiger über dem Knopf ist.
     */
    ImageIcon textureHovered;
    
    /**
     * Textur, wenn der Knopf gedrückt wird.
     */
    ImageIcon texturePressed;
    
    /**
     * Erstellt einen neuen Knopf
     *
     * @param x       horizontale Position in der Welt
     * @param y       vertikale Position in der Welt
     * @param width   Weite im Weltmaßstab
     * @param height  Höhe im Weltmaßstab
     * @param texture Textur
     */
    public Button(float x,
                  float y,
                  float width,
                  float height,
                  ImageIcon texture,
                  ImageIcon textureHovered,
                  ImageIcon texturePressed)
    {
        super(x, y, width, height, texture);
        
        this.texture = texture;
        this.textureHovered = textureHovered;
        this.texturePressed = texturePressed;
    }
    
    /**
     * Diese Methode wird jedes Update ausgeführt.
     * <br>
     * <br>
     * Sie überprüft, ob der Knopf gedrückt wird oder ob die Maus sich über ihm befindet
     * und setzt die Texturen entsprechend.
     * Wenn der Knopf gedrückt wird, wird die action() Methode ausgeführt.
     *
     * @see engine.object.GameObject
     */
    @Override
    public void onUpdate()
    {
        if(isButtonPressed())
        {
            setTexture(texturePressed);
            action();
        }
        else if(isButtonHovered()) setTexture(textureHovered);
        else setTexture(texture);
    }
    
    /**
     * Überschreibt die Aktion, die ausgeführt werden soll, wenn
     * der Knopf gedrückt wird.
     * <br>
     * <br>
     * Abstrakte Methoden werden automatisch beim Erstellen überschrieben.
     */
    public abstract void action();
    
    /**
     * Überprüft, ob der Knopf gedrückt wird oder nicht.
     * <br>
     * <br>
     * Bei einem gedrückten Knopf muss der Mauszeiger auf dem Knopf sein und die linke Maustaste muss gedrückt werden.
     *
     * @return true, falls gedrückt. false, wenn nicht
     */
    public boolean isButtonPressed()
    {
        Controller controller = new Controller();
    
        return isButtonHovered() && controller.getMouseButtonsPressed().contains(MouseEvent.BUTTON1);
    }
    
    /**
     * Überprüft, ob die Maus über dem Knopf ist.
     *
     * @return true, falls ja. false, wenn nicht
     */
    public boolean isButtonHovered()
    {
        Controller controller = new Controller();
        
        //Die Position des Knopfes, wenn er gerendert wird
        float renderPosX = 0;
        float renderPosY = 0;
        
        //Der Knopf ist auch ein GameObject. Das heist, dass die Position des Knopfes
        //relativ zur Welt ist, nicht zum Fenster.
        //Die Position des Mauszeigers ist jedoch relativ zum Fenster. Wenn man jetzt überprüfen will,
        //ob der Mauszeiger auf dem Knopf ist, so muss man eine der Positionen in das jeweils andere Bezugssystem
        //konvertieren. Hier wird die Postion des Knopfes in das Bezugssystem der Zeichenebene (Fenster) konvertiert.
        
        //1. Auf welcher Kamera ist der Maus-Zeiger.
        for(int i = 0; i < GameObject.getLoadedGameObjects().size(); i++)
        {
            if(GameObject.getLoadedGameObjects().get(i) instanceof Camera)
            {
                //Instanz der Kamera
                Camera cam = (Camera) GameObject.getLoadedGameObjects().get(i);
                
                if(getX() >= cam.getRenderPosX()
                && getY() >= cam.getRenderPosY()
                && getX() <= cam.getRenderPosX() + cam.getWidth()
                && getY() <= cam.getRenderPosY() + cam.getHeight())
                {
                    //2. Konvertiere Knopf Position auf Render Bezugssystem:
                    if(cam.getFocusedObj() == null)
                    {
                        renderPosX = Display.getRenderEngine().convertToRenderPos(getX(), cam.getX(), cam.getRenderPosX(), cam.getFocusX(), -1, -1, cam.getZoom());
                        renderPosY = Display.getRenderEngine().convertToRenderPos(getY(), cam.getY(), cam.getRenderPosY(), cam.getFocusY(), -1, -1, cam.getZoom());
                    }
                    else
                    {
                        renderPosX = Display.getRenderEngine().convertToRenderPos(getX(), cam.getX(), cam.getRenderPosX(), cam.getFocusX(), cam.getFocusedObj().getX(), cam.getFocusedObj().getWidth(), cam.getZoom());
                        renderPosY = Display.getRenderEngine().convertToRenderPos(getY(), cam.getY(), cam.getRenderPosY(), cam.getFocusY(), cam.getFocusedObj().getY(), cam.getFocusedObj().getHeight(), cam.getZoom());
                    }
    
                    //3. Prüfen ob Mauszeiger auf Knopf
                    if(controller.getMouseX() >= renderPosX
                    && controller.getMouseY() >= renderPosY
                    && controller.getMouseX() <= renderPosX + getWidth() * cam.getZoom()
                    && controller.getMouseY() <= renderPosY + getHeight() * cam.getZoom()) return true;
                }
            }
        }
        
        return false;
    }
}
