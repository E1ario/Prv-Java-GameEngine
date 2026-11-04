package engine.controller;

import engine.ui.*;

import java.util.*;

/**
 * Die Controller Klasse ermöglicht einen leichten Umgang mit der Tastatur und der Maus.
 * Dabei gibt sie nicht nur gedrückte Tasten aus, sondern auch beispielsweise die Position der Maus. <br>
 * <br>
 * Die Klasse verwendet dabei die engine.controller.KeyHandler Klasse, engine.controller.MouseHandler Klasse
 * und die engine.controller.MouseMotionHandler Klasse. Sie kümmern sich um das Erkennen und Aufzeichnen von Tasten.
 *
 * @see KeyHandler
 * @see MouseHandler
 * @see MouseMotionHandler
 */
public class Controller
{
    
    /**
     * Gibt eine ArrayList mit allen derzeitig gedrückten Tasten (nur Tastatur) zurück.<br>
     * <br>
     * Wenn eine Taste gedrückt wird, wird sie in eine Liste von allen gedrückten Tasten aufgenommen.
     * Wird diese Taste losgelassen, so wird die entsprechende Taste aus der Liste entfernt. <br>
     * <br>
     * Die Tasten werden dabei mit dem KeyCode gespeichert. Dies ist eine Nummer, die jede Taste vorab
     * zugewiesen bekommen hat damit man sie identifizieren kann.<br>
     * Welche Taste welchen Code hat, kann man durch die Klasse KeyEvent herausfinden.
     * <br>
     * <br>
     * <code>
     *     ...
     *     if(controller.getKeysPressed().contains(KeyEvent.VK_A))
     *     ...
     * </code>
     * <br>
     * <br>
     * Mit diesem Code, kann man herausfinden, ob die Taste 'A' gedrückt wird.
     * <br>
     * <br>
     * Die Liste mit allen gedrückten Tasten wird automatisch aktualisiert.
     *
     * @return Liste mit allen derzeitig gedrückten Tasten.
     * @see KeyHandler
     */
    public ArrayList<Integer> getKeysPressed()
    {
        return KeyHandler.keyBuffer;
    }
    
    /**
     * Gibt eine ArrayList mit allen derzeitig gedrückten Tasten (nur Maus) zurück.<br>
     * <br>
     * Wenn eine Taste gedrückt wird, wird sie in eine Liste von allen gedrückten Tasten aufgenommen.
     * Wird diese Taste losgelassen, so wird die entsprechende Taste aus der Liste entfernt. <br>
     * <br>
     * Die Tasten werden dabei mit dem KeyCode gespeichert. Dies ist eine Nummer, die jede Taste vorab
     * zugewiesen bekommen hat damit man sie identifizieren kann.<br>
     * Welche Taste welchen Code hat, kann man durch die Klasse MouseEvent herausfinden.
     * <br>
     * <br>
     * <code>
     *     ...
     *     if(controller.getMouseButtonsPressed().contains(MouseEvent.BUTTON_1))
     *     ...
     * </code>
     * <br>
     * <br>
     * Mit diesem Code, kann man herausfinden, ob die Linke Maustaste gedrückt wird.
     * <br>
     * <br>
     * Die Liste mit allen gedrückten Tasten wird automatisch aktualisiert.
     *
     * @return Liste mit allen derzeitig gedrückten Maustasten.
     * @see MouseHandler
     */
    public ArrayList<Integer> getMouseButtonsPressed()
    {
        return MouseHandler.keyBuffer;
    }
    
    /**
     * Gibt die horizontale Position der Maus zurück.
     * <br>
     * <br>
     * In vielen Fällen kann es hilfreich sein die Position des Mauszeigers zu wissen.
     * Diese Position ist relativ zum Fenster. Das heist, das in der oberen linken Ecke die
     * Position (0 | 0) ist. Die Koordinate vergrößert sich jedes Pixel, wenn der Mauszeiger nach rechts geht.
     * <br>
     * <br>
     * Bevor die Position herausgegeben wird, werden die Einrückungen des Fensters einmal abgezogen.
     * Das hat den Vorteil, damit man die Mauszeiger Position nicht auf dem Fenster, sondern auf dem standard errichteten
     * JPanel bekommt.
     * <br>
     * Die Position wird als Float ausgegeben, obwohl es ein Integer ist, damit man beim Vergleichen mit Spielobjekten
     * keine konvertierung vornehmen muss.
     * <br>
     * <br>
     * Die Position wird automatisch von dem engine.controller.MouseMotionHandler ermittelt.
     * <br>
     * Behalte im Hinterkopf: Anfangs, bevor die Maus bewegt wird ist die Position der Maus bei (0 | 0).
     *
     * @return horizontale Position der Maus.
     * @see MouseMotionHandler
     */
    public float getMouseX()
    {
        return MouseMotionHandler.mouseX - Display.getDisplay().getInsets().left;
    }
    
    /**
     * Gibt die vertikale Position der Maus zurück.
     * <br>
     * <br>
     * In vielen Fällen kann es hilfreich sein die Position des Mauszeigers zu wissen.
     * Diese Position ist relativ zum Fenster. Das heist, das in der oberen linken Ecke die
     * Position (0 | 0) ist. Die Koordinate vergrößert sich jedes Pixel, wenn der Mauszeiger nach unten geht.
     * <br>
     * <br>
     * Bevor die Position herausgegeben wird, werden die Einrückungen des Fensters einmal abgezogen.
     * Das hat den Vorteil, damit man die Mauszeiger Position nicht auf dem Fenster, sondern auf dem standard errichteten
     * JPanel bekommt.
     * <br>
     * Die Position wird als Float ausgegeben, obwohl es ein Integer ist, damit man beim Vergleichen mit Spielobjekten
     * keine konvertierung vornehmen muss.
     * <br>
     * <br>
     * Die Position wird automatisch von dem engine.controller.MouseMotionHandler ermittelt.
     * <br>
     * Behalte im Hinterkopf: Anfangs, bevor die Maus bewegt wird ist die Position der Maus bei (0 | 0).
     *
     * @return vertikale Position der Maus.
     * @see MouseMotionHandler
     */
    public float getMouseY()
    {
        return MouseMotionHandler.mouseY - Display.getDisplay().getInsets().top;
    }
}
