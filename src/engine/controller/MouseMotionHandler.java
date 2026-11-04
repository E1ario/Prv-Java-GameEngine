package engine.controller;

import java.awt.event.*;

/**
 * Diese Klasse kümmert sich ausschließlich um die Ermittlung der Mauszeigerposition.
 * <br>
 * <br>
 * Dabei erbt sie von dem java.awt.event.MouseMotionListener Interface und bietet somit eine
 * Schnittstelle zwischen Java und dem Endverbraucher.
 * Bevor diese Position jedoch verwendet werden kann wird sie erst von der Controller Klasse verarbeitet.
 *
 * @see java.awt.event.MouseMotionListener
 * @see Controller
 */
public class MouseMotionHandler implements MouseMotionListener
{
    /**
     * Speichert die Horizontale Mausposition relativ zum Fenster
     */
    static int mouseX = 0;
    
    /**
     * Speicher die vertikale Mausposition relativ zum Fenster
     */
    static int mouseY = 0;
    
    /**
     * Wird aufgerufen, sobald der Mauszeiger bewegt wird.
     * Kümmert sich um die Ermittlung der Mauszeigerposition.
     * <br>
     * <br>
     * Ein weg um die Position des Mauszeigers auf einer Komponente zu ermitteln, ist das man jedes Mal,
     * wenn der Mauszeiger bewegt wird, die Position abspeichert.
     * Diese kann man über das entsprechende MouseEvent e bekommen.
     * <br>
     * <br>
     * Diese Position wird anschließend von der engine.controller.Controller Klasse weiterverarbeitet,
     * sodass am Ende eine Schnittstelle zwischen java und dem Endentwickler erstellt wird, die die korrekte
     * Mausposition ausgibt.
     *
     * @param e stellt einen Zugang zur Maus her.
     * @see Controller
     */
    @Override
    public void mouseMoved(MouseEvent e)
    {
        mouseX = e.getX();
        mouseY = e.getY();
    }
    
    /**
     * Wird ausgeführt, sobald eine Maustaste gehalten und der Mauszeiger bewegt wird.
     *
     * @param e Das MouseEvent, worüber die Maus aufrufbar ist.
     */
    @Override
    public void mouseDragged(MouseEvent e)
    {
        mouseX = e.getX();
        mouseY = e.getY();
    }
}
