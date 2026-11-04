package engine.object;

import engine.exceptions.*;
import engine.ui.*;

import javax.swing.*;
import java.util.*;

/**
 * Eine Camera ist ein GameObject, welches die Funktion hat das Spielgeschehen dem Endverbraucher sichtbar
 * zu machen.
 * <br>
 * <br>
 * Dabei überprüft sie welches der Spielobjekte im Kamerabereich zu sehen sind und gibt diese zum Laden und
 * zum Rendern weiter.
 * <br>
 * Bevor die Objekte jedoch final gerendert werden, wird ein Zoom darauf gerechnet. Für mehr Informationen siehe
 * engine.ui.RenderEngine.
 *
 * @see engine.ui.RenderEngine
 * @see GameObject
 */
public class Camera extends GameObject
{
    /**
     * Die horizontale Position auf dem Fenster, wo die im Kamerabereich liegenden GameObjects gerendert werden sollen.
     */
    float renderPosX;
    
    /**
     * Die vertikale Position auf dem Fenster, wo die im Kamerabereich liegenden GameObjects gerendert werden sollen.
     */
    float renderPosY;
    
    /**
     * Gibt die horizontale Position des Fokuspunktes in der Welt an.
     * Der Fokuspunkt gibt an, wo auf der Kamera das zu fokussierende Objekt sein soll.
     */
    float focusX;
    
    /**
     * Gibt die vertikale Position des Fokuspunktes in der Welt an.
     * Der Fokuspunkt gibt an, wo auf der Kamera das zu fokussierende Objekt sein soll.
     */
    float focusY;
    
    /**
     * Die aktuelle Zoom-stufe.
     * In der Renderengine wird dieser Zoom berechnet. Großer Zoom vergrößert das "Bild" der Kamera.
     */
    float zoom = 1;
    
    /**
     * Das zu fokussierende Objekt. Sobald ein Objekt fokussiert wird, verfolgt die Kamera dieses.
     */
    GameObject focusedObj;
    
    
    
    /**
     * Erstellt eine neue Kamera.
     * Eine Camera ist ein GameObject, welches die Funktion hat das Spielgeschehen dem Endverbraucher sichtbar
     * zu machen.
     * <br>
     * <br>
     * Dabei überprüft sie welches der Spielobjekte im Kamerabereich zu sehen sind und gibt diese zum Laden und
     * zum Rendern weiter.
     *
     * @param x       horizontale Position in der Welt.
     * @param y       vertikale Position in der Welt
     * @param width   Weite der Kamera (Kamerabereich)
     * @param height  Höhe der Kamera (Kamerabereich)
     * @param renderPosX die Position auf dem Fenster, wo die sichtbaren Spielobjekte gezeichnet werden sollen
     * @param renderPosY die vertikale Position auf dem Fenster, wo die sichtbaren Spielobjekte gezeichnet werden sollen
     */
    public Camera(float x, float y, float width, float height, float renderPosX, float renderPosY)
    {
        super(x, y, width, height, new ImageIcon());
        this.renderPosX = renderPosX;
        this.renderPosY = renderPosY;
        
        //Lädt die Kamera. Die Kamera lädt nur objekte, wenn sie selber geladen ist.
        load();
    }
    
    /**
     * Diese Methode updatet die Kamera, und wird jedes Update ausgeführt.
     * Sie wurde von der onUpdate() Methode der Eltern Klasse überschrieben.
     *
     * @see GameObject
     * @see engine.threads.UpdateThread
     */
    @Override
    public void onUpdate()
    {
        focusObj(getFocusedObj(), getFocusX(), getFocusY());
    }
    
    /**
     * Fokussiert ein GameObject.
     * <br>
     * <br>
     * Wenn ein GameObjekt fokussiert wird, dann bleibt die Kamera immer gleich auf diesem Objekt und verfolgt es ggf.
     * Dabei kann man eine einfache Kamerabewegung erstellen. Zudem wird bei einem Zoom, aus dem fokussierten Objekt gezoomt.
     * Wo das fokussierte Objekt auf der Kamera zu sehen ist, gibt der Fokuspunkt an.
     * <br>
     * Die Kamera versucht also die Kamera so zu verschieben, dass der Fokuspunkt auf der Position des zu fokussierenden Objektes
     * ist.
     * <br>
     * <br>
     * Gibt es kein zu fokussierendes Objekt, so "filmt" die Kamera nur den Bereich, wie sie erstellt wurde und der Zoom geht aus
     * der oberen linken Ecke der Kamera heraus.
     *
     * @param obj Das zu fokussierende Objekt
     */
    private void focusObj(GameObject obj, float focusX, float focusY)
    {
        //Notwendige Bedingung: obj != null
        if(obj == null) return;
        
        //Fokussiere das Objekt
        //Abstand berechnen
        float horizontalOffset = focusX - (obj.getX() + obj.getWidth() / 2);
        float verticalOffset = focusY - (obj.getY() + obj.getHeight() / 2);
    
        //Kamera verschieben
        setX(getX() - horizontalOffset);
        setY(getY() - verticalOffset);
        
        //Fokuspunkt neu ausrichten
        setFocusX(getFocusX() - horizontalOffset);
        setFocusY(getFocusY() - verticalOffset);
    }
    
    /**
     * Lädt alle Game Objects aus einem Array, wenn diese in render Position zu "sehen" sind.
     * <br>
     * <br>
     * Diese Methode geht ein Array an GameObjects durch und überprüft, ob diese, würden sie gerendert werden, in dem Kamerabereich zu "sehen" sind.
     * Sind sie das, so werden diese geladen.
     * Der UpdateThread updatet diese im anschluss.
     *
     * @param objects Objekte die geladen werden, wenn sie auf der Kamera zu "sehen" sind.
     */
    public void loadObjectsOnCamera(GameObject[] objects)
    {
        //Notwendige Bedingung: objects != null
        if(objects == null) throw new noValueException("Das übergebene Array ist 'null', bzw leer", getUID());
    
        for(int i = 0; i < objects.length; i++)
        {
            GameObject obj = objects[i]; //Instanz des Objektes, für saubereren Code
        
            //Konvertiere Weltposition des Objektes zu Renderposition
            float objRenderX;
            float objRenderY;
        
            if(getFocusedObj() == null)
            {
                objRenderX = RenderEngine.convertToRenderPos(obj.getX(), getX(), getRenderPosX(), getFocusX(), -1, -1, getZoom());
                objRenderY = RenderEngine.convertToRenderPos(obj.getY(), getY(), getRenderPosY(), getFocusY(), -1, -1, getZoom());
            }
            else
            {
                objRenderX = RenderEngine.convertToRenderPos(obj.getX(), getX(), getRenderPosX(), getFocusX(), getFocusedObj().getX(), getFocusedObj().getWidth(), getZoom());
                objRenderY = RenderEngine.convertToRenderPos(obj.getY(), getY(), getRenderPosY(), getFocusY(), getFocusedObj().getY(), getFocusedObj().getHeight(), getZoom());
            }
            
            if(objRenderX > getRenderPosX() - obj.getWidth() * getZoom()
            && objRenderY > getRenderPosY()- obj.getHeight() * getZoom()
            && objRenderX < getRenderPosX() + getWidth()
            && objRenderY < getRenderPosY() + getHeight())
                obj.load();
        }
    }
    
    /**
     * Setzt das zu fokussierende Objekt.
     * <br>
     * <br>
     * Wenn ein GameObjekt fokussiert wird, dann bleibt die Kamera immer gleich auf diesem Objekt und verfolgt es ggf.
     * Dabei kann man eine einfache Kamerabewegung erstellen. Zudem wird bei einem Zoom, aus dem fokussierten Objekt gezoomt.
     * Wo das fokussierte Objekt auf der Kamera zu sehen ist, gibt der Fokuspunkt an.
     * Wenn das übergebene Objekt 'null' ist, so wird eine noValueException geworfen.
     *
     * @param obj GameObject, welches fokussiert werden soll.
     */
    public void setFocusedObj(GameObject obj)
    {
        if(obj != null) this.focusedObj = obj;
        else throw new noValueException("Das zu fokussierende Objekt ist 'null'", getUID());
    }
    
    /**
     * Gibt das fokussierte Objekt zurück.
     * <br>
     * <br>
     * Wenn ein GameObjekt fokussiert wird, dann bleibt die Kamera immer gleich auf diesem Objekt und verfolgt es ggf.
     * Dabei kann man eine einfache Kamerabewegung erstellen. Zudem wird bei einem Zoom, aus dem fokussierten Objekt gezoomt.
     * Wo das fokussierte Objekt auf der Kamera zu sehen ist, gibt der Fokuspunkt an.
     *
     * @return GameObject, welches fokussiert wird.
     */
    public GameObject getFocusedObj()
    {
        return focusedObj;
    }
    
    /**
     * Setzt die horizontale Position des Fokuspunktes in der Welt.
     * <br>
     * Der Fokuspunkt gibt an, wo auf der Kamera das zu fokussierende Objekt sein soll.
     * Dementsprechend kann er nicht außerhalb der Kamera sein. Ist er das dennoch, so kommt eine OutOfBounds exception.
     *
     * @param focusX horizontale Position in der Welt des Fokuspunktes.
     * @see #focusX
     */
    public void setFocusX(float focusX)
    {
        if(focusX >= getX()
        && focusX <= getX() + getWidth()) this.focusX = focusX;
        else throw new OutOfBoundsException("Die horizontale Position des Fokuspunktes ist nicht innerhalb der Kamera", getUID());
    }
    /**
     * Gibt die horizontale Position des Fokuspunktes zurück.
     * @return horizontale Position des Fokuspunktes auf der Welt
     * @see #focusX
     */
    public float getFocusX()
    {
        return focusX;
    }
    
    /**
     * Setzt die vertikale Position des Fokuspunktes in der Welt.
     * <br>
     * Der Fokuspunkt gibt an, wo auf der Kamera das zu fokussierende Objekt sein soll.
     * Dementsprechend kann er nicht außerhalb der Kamera sein. Ist er das dennoch, so kommt eine OutOfBounds exception.
     *
     * @param focusY horizontale Position in der Welt des Fokuspunktes.
     * @see #focusY
     */
    public void setFocusY(float focusY)
    {
        if(focusY >= getY()
        && focusY <= getY() + getHeight()) this.focusY = focusY;
        else throw new OutOfBoundsException("Die vertikale Position des Fokuspunktes ist nicht innerhalb der Kamera", getUID());
    }
    
    /**
     * Gibt die vertikale Position des Fokuspunktes zurück
     * @return vertikale Position des Fokuspunktes auf der Welt
     * @see #focusY
     */
    public float getFocusY()
    {
        return focusY;
    }
    
    /**
     * Gibt die horizontale Render Position auf dem Fenster zurück.
     * <br>
     * Diese bestimmt, wo das "Bild" der Kamera auf dem Fenster zu sehen ist.
     * @return horizontale Render Position
     * @see #renderPosX
     */
    public float getRenderPosX()
    {
        return renderPosX;
    }
    
    /**
     * Gibt die vertikale Render Position auf dem Fenster zurück.
     * <br>
     * Diese bestimmt, wo das "Bild" der Kamera auf dem Fenster zu sehen ist.
     * @return vertikale Render Position
     * @see #renderPosY
     */
    public float getRenderPosY()
    {
        return renderPosY;
    }
    
    /**
     * Gibt die aktuelle Zoom-Stufe zurück.
     * <br>
     * <br>
     * Dieser Zoom wird beim Rendern auf alle Objekte, die die Kamera sieht angewendet.
     *
     * @return aktuelle Zoom-stufe
     * @see engine.ui.RenderEngine
     */
    public float getZoom()
    {
        return zoom;
    }
    
    /**
     * Setzt den Zoom.
     * <br>
     * <br>
     * Je höher die Zoomstufe, desto größer werden die Objekte.
     * Der Zoom, darf dabei nicht kleiner als 0 sein. Ist das dennoch der Fall, dann wird eine
     * OutOfBoundsException geworfen.
     * Dieser Zoom wird beim Rendern auf alle Objekte die gerendert werden angewendet.
     *
     * @param zoom Der neue zoom.
     * @see engine.ui.RenderEngine
     */
    public void setZoom(float zoom)
    {
        if(zoom >= 0) this.zoom = zoom;
        else throw new OutOfBoundsException("Der übergebene Zoom ist negativ", getUID());
    }
}
