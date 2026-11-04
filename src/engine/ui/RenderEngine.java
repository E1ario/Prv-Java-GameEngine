package engine.ui;

import engine.object.*;
import testProgramm.io.*;

import javax.swing.*;
import java.awt.*;

/**
 * Die Render-Engine sorgt dafür, dass die Textur der Game-Objekte gezeichnet werden.
 * Dabei werden nur Objekte gezeichnet, die geladen sind. Diese werden durch die Kamera geladen.
 * <br>
 * <br>
 * Das Zeichnen erfolg über ein JLabel, welches auf dem Fenster gespannt wird und die Texturen der Game-Objekte zeichnet.
 * Diese Texturen haben eine Position wie auch Maße, welche relativ zu dem JLabel sind.
 * Damit eine Konversion zwischen der Spielwelt und dem JLabel stattfinden kann, nutzt man eine Kamera, welche die Objekte relativ
 * zu ihrem Sichtfeld zeichnet, unabhängig davon wo die Objekte in der Spielwelt tatsächlich sind.
 * <br>
 * Die Texturen werden mit antialiasing gezeichnet.
 * <br>
 * Beim Zeichnen der Objekte werden diese skaliert. Daraus folgt, dass deren eigentliche Position in der Spielwelt nicht unbedingt
 * mit der Position auf dem JLabel gleich sein muss. Diese Skalierung ist notwendig, damit auch auf unterschiedlich großen
 * Bildschirmen die Größe der GameObjects angepasst werden kann.
 */
public class RenderEngine extends JLabel
{
    Properties config = new Properties("ressource/config.properties");
    
    /**
     * Zeichnet jedes GameObject.
     * <br>
     * <br>
     * Das Zeichnen erfolgt über eine zeichenebene, ein JLabel, welches über das gesammte Fenster gespannt wird.
     * Dieses JLabel besitzt eine Funktion paintComponent(), welche ein Graphics Object bietet, damit man auf diesem JLabel Bilder oder Figuren
     * zeichnen kann.
     * <br>
     * Die zeichnung erfolgt mit antialiasing.
     *
     * @param g Das Graphics object, mit dem man zeichnen kann
     */
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        activateAntialiasing(g);
        
        //Zeichne GameObjects
        for(int i = 0; i < GameObject.getLoadedGameObjects().size(); i++)
        {
            if(GameObject.getLoadedGameObjects().get(i) instanceof Camera)
            {
                Camera cam = (Camera) GameObject.getLoadedGameObjects().get(i); //Instanz einer Kamera
                GameObject[] loadedObj = GameObject.getLoadedGameObjects().toArray(new GameObject[0]); //Alle Objekte, die von der Kamera gesichtet wurden
                
                for(int j = 0; j < loadedObj.length; j++)
                {
                    //Rendert nur Objekte, die eine Textur haben
                    if(loadedObj[j].getTexture() == new ImageIcon()) return;
                    
                    float renderX, renderY;
                    
                    if(cam.getFocusedObj() == null)
                    {
                        renderX = convertToRenderPos(loadedObj[j].getX(), cam.getX(), cam.getRenderPosX(), cam.getFocusX(), -1, -1, cam.getZoom());
                        renderY = convertToRenderPos(loadedObj[j].getY(), cam.getY(), cam.getRenderPosY(), cam.getFocusY(), -1, -1, cam.getZoom());
                    }
                    else
                    {
                        renderX = convertToRenderPos(loadedObj[j].getX(), cam.getX(), cam.getRenderPosX(), cam.getFocusX(), cam.getFocusedObj().getX(), cam.getFocusedObj().getWidth(), cam.getZoom());
                        renderY = convertToRenderPos(loadedObj[j].getY(), cam.getY(), cam.getRenderPosY(), cam.getFocusY(), cam.getFocusedObj().getY(), cam.getFocusedObj().getHeight(), cam.getZoom());
                    }
                    
                    if(config.getBoolean("debug.drawTextures"))
                        drawImage(
                                g,
                                loadedObj[j].getTexture(),
                                renderX,
                                renderY,
                                loadedObj[j].getWidth() * cam.getZoom(),
                                loadedObj[j].getHeight() * cam.getZoom()
                        );
                    
                    if(config.getBoolean("debug.drawMenu")) drawDebugMenu(loadedObj[j], g, cam, renderX, renderY);
                }
            }
        }
    }
    
    private void drawDebugMenu(GameObject obj, Graphics g, Camera cam, float renderX, float renderY)
    {
        if(config.getBoolean("debug.drawAtRealPos"))
        {
            g.setColor(Color.ORANGE);
            if(config.getBoolean("debug.drawObj"))
                g.drawRect(
                        Math.round(obj.getX()),
                        Math.round(obj.getY()),
                        Math.round(obj.getWidth()),
                        Math.round(obj.getHeight())
                );
            
            g.setColor(Color.RED);
            if(config.getBoolean("debug.drawCam"))
                g.drawRect(
                        Math.round(cam.getX()),
                        Math.round(cam.getY()),
                        Math.round(cam.getWidth()),
                        Math.round(cam.getHeight())
                );
    
        }
        else
        {
            g.setColor(Color.ORANGE);
            if(config.getBoolean("debug.drawObj"))
                g.drawRect(
                        Math.round(renderX),
                        Math.round(renderY),
                        Math.round(obj.getWidth() * cam.getZoom()),
                        Math.round(obj.getHeight() * cam.getZoom())
                );
        }
    }
    
    
    /**
     * Konvertiert eine Weltposition in eine Fensterposition.
     * <br>
     * <br>
     * Jedes GameObject hat eine Position. Diese ist in einer Welt (siehe Grundprinzip der Engine in engine.object.GameObject).
     * Diese Position wird mithilfe von Kameras auf eine Position auf dem JLabel konvertiert. So wird das "Bild" der kamera
     * angezeigt.
     * <br>
     * Diese Methode konvertiert eine Weltposition mithilfe der Kamera auf das Bezugssystem des Fensters.
     * Dabei wird auch der Zoom mit kalkuliert.
     * <br>
     * <br>
     * Die Methode verwendet positionen. Jedoch meint man hiermit entweder die horizontale oder die vertikale Positionen
     * der Objekte, je nachdem welche Koordinate man berechnen will. Wenn man die horizontale Position berechnen möchte,
     * dann ist die Weite des fokussierten Objektes verlangt, bei der vertikalen Position ist es die Höhe.
     * Wenn es kein fokussiertes Objekt gibt bzw. dieses null ist, so muss man als größe -1 eingeben.
     *
     * @param pos Weltposition welche konvertiert werden soll.
     * @param camPos Weltposition der camera, welche das Objekt aufnimmt.
     * @param camRenderPos Renderpostion (Fenster) der Kamera, welche das Objekt aufnimmt.
     * @param fokusPointPos Weltposition des Fokuspunktes der Kamera, welche das Objekt aufnimmt.
     * @param focusedObjPos Weltposition des objektes, welches von der Kamera fokussiert wird. Gibt es kein Objekt, welches die Kamera fokussiert, so muss man diesen Wert auf -1 setzten.
     * @param focusedObjSize Größe im Weltmaßstab des Objektes, welche von der Kamera fokussiert wird. Gibt es kein Objekt, welches die Kamera fokussiert, so muss man diesen Wert auf -1 setzten.
     *
     * @return eine konvertierte Weltposition zur Renderposition.
     */
    public static float convertToRenderPos(float pos,
                                    float camPos,
                                    float camRenderPos,
                                    float fokusPointPos,
                                    float focusedObjPos,
                                    float focusedObjSize,
                                    float zoom)
    {
        if(focusedObjSize < 0)
        {
            return ((camRenderPos + (pos - camPos)) * zoom);
        }
        else
        {
            return ((camRenderPos + (pos - camPos)) * zoom) - (((camRenderPos + ((focusedObjPos + (focusedObjSize / 2f)) - camPos)) * zoom) - (camRenderPos + (fokusPointPos - camPos)));
        }
    }
    
    /**
     * Konvertiert eine Fensterposition in eine Weltposition.
     * <br>
     * <br>
     * Jedes GameObject hat eine Position. Diese ist in einer Welt (siehe Grundprinzip der Engine in engine.object.GameObject).
     * Diese Position wird mithilfe von Kameras auf eine Position auf dem JLabel konvertiert. So wird das "Bild" der kamera
     * angezeigt.
     * <br>
     * Diese Methode konvertiert eine Fensterposition mithilfe der Kamera auf das Bezugssystem der Welt.
     * Dabei wird auch der Zoom mit kalkuliert.
     * <br>
     * <br>
     * Die Methode verwendet positionen. Jedoch meint man hiermit entweder die horizontale oder die vertikale Positionen
     * der Objekte, je nachdem welche Koordinate man berechnen will. Wenn man die horizontale Position berechnen möchte,
     * dann ist die Weite des fokussierten Objektes verlangt, bei der vertikalen Position ist es die Höhe.
     * Wenn es kein fokussiertes Objekt gibt bzw. dieses null ist, so muss man als größe -1 eingeben.
     *
     * @param pos Fensterposition welche konvertiert werden soll.
     * @param camPos Weltposition der camera, welche das Objekt aufnimmt.
     * @param camRenderPos Renderpostion (Fenster) der Kamera, welche das Objekt aufnimmt.
     * @param fokusPointPos Weltposition des Fokuspunktes der Kamera, welche das Objekt aufnimmt.
     * @param focusedObjPos Weltposition des objektes, welches von der Kamera fokussiert wird. Gibt es kein Objekt, welches die Kamera fokussiert, so muss man diesen Wert auf -1 setzten.
     * @param focusedObjSize Größe im Weltmaßstab des Objektes, welche von der Kamera fokussiert wird. Gibt es kein Objekt, welches die Kamera fokussiert, so muss man diesen Wert auf -1 setzten.
     *
     * @return eine konvertierte Fensterposition zur Weltposition.
     */
    public static float convertToWorldPos(float pos,
                                           float camPos,
                                           float camRenderPos,
                                           float fokusPointPos,
                                           float focusedObjPos,
                                           float focusedObjSize,
                                           float zoom)
    {
        if(focusedObjSize < 0)
        {
            return (pos + camPos * zoom - camRenderPos * zoom) / zoom;
        }
        else
        {
            return (pos + camPos - camRenderPos + zoom * focusedObjPos - fokusPointPos + (zoom * focusedObjSize) / 2f) / zoom;
        }
    }
    
    /**
     * Aktiviert antialiasing.
     * <br>
     * <br>
     * Antialiasing rundet scharfe Ecken und Kanten ab, und kann zu einem glatten Bild führen.
     * Das Antialiasing wird auf das übergebene Graphics Objekt übertragen.
     *
     * @param g Graphics Objekt, auf dem antialiasing aktiviert wird.
     */
    private void activateAntialiasing(Graphics g)
    {
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }
    
    /**
     * Zeichnet ein neues Objekt auf dem JLabel.
     * <br>
     * <br>
     * Das Zeichnen eines Objektes erfolgt auf einem JLabel. Jedoch sind die Positionen relativ zum Fenster.
     * Das bedeutet, dass die Position (0, 0) nicht wie gewollt in der Ecke des JLabels, sondern auf der Fensterleiste ist.
     * <br>
     * <br>
     * Diese Methode behebt dieses Problem und sorgt dafür, dass die Position relativ zum JLabel ist.
     *
     * @param g Graphics Objekt mit dem gemalt wird
     * @param texture Textur die gemalt werden soll
     * @param x horizontale Position auf dem JLabel
     * @param y vertikale Position auf dem JLabel
     * @param width Weite in Pixeln
     * @param height Höhe in Pixeln
     */
    public void drawImage(Graphics g, ImageIcon texture, float x, float y, float width, float height)
    {
        g.drawImage(
                texture.getImage(),
                Math.round(this.getX() + x), //X Position
                Math.round(this.getY() + y), //Y Position
                Math.round(width),
                Math.round(height),
                null
        );
    }
}
