package engine.controller;

import java.awt.event.*;
import java.util.*;

/**
 * Die KeyHandler Klasse, dient zur erkennung der Tastatur und deren Eingaben.
 * Sie speichert diese Daten in einem Buffer und gibt diesen, für eine leichtere Verarbeitung, an die engine.controller.Controller Klasse
 * weiter.
 * <br>
 * <br>
 * Die Klasse implementiert das java.awt.event.KeyListener Interface. Dieses Interface sorgt dafür,
 * dass die Klasse KeyHandler alle Methoden besitzt, die Java benötigt, um die Tasteninputs zu verarbeiten.
 * <br>
 * <br>
 * Durch das JDK (Java development Kit) bietet java eine Vielzahl an Möglichkeiten. Eine davon ist,
 * Tasteninputs auszulesen. Das funktioniert, indem man mehrere Methoden hat welche von java automatisch bei einem
 * Tastendruck aufgerufen werden. Die Klasse KeyHandler überschreibt diese Methoden damit die Tasteninputs ausgelesen/weiterverarbeitet
 * werden können. Die Methoden, welche überschrieben werden müssen, stellt das Interface java.awt.event.KeyListener bereit.
 * <br>
 * <br>
 * Damit die überschriebenen Methoden von java erkannt werden, muss man die Klasse KeyHandler zu einem Komponenten (JFrame) zufügen.
 * Das passiert automatisch beim Erstellen eines Fensters durch die implementInputHandler() Methode in der engine.ui.Display Klasse.
 *
 * @see java.awt.event.KeyListener
 * @see engine.ui.Display
 */
public class KeyHandler implements KeyListener
{
    /**
     * Speichert alle im Moment gedrückten Tasten.
     */
    static ArrayList<Integer> keyBuffer = new ArrayList<>();
    
    /**
     * Diese Methode verarbeitet alle kurz gedrückten Tasten.
     * <br>
     * <br>
     * Diese Methode wird jedoch in der Regel, vor allem dieser Engine nicht verwendet,
     * da die Engine jeden Tasteninput speichert bis diese Taste wieder losgelassen wird.
     * Ob nun eine Taste gedrückt wird oder nicht findet man mit der keyPressed() Methode heraus.
     * <br>
     * <br>
     * Für mehr Information warum die Engine jeden Tasteninput speichert bis sie wieder losgelassen wird,
     * siehe die keyPressed() Methode.
     *
     * @param e Das Key Event, über das man die Inputs auslesen/verarbeiten kann.
     */
    @Override
    public void keyTyped(KeyEvent e)
    {
        //NoOp
    }
    
    /**
     * Verarbeitet alle im Moment gedrückten oder gehaltenen Tasten.
     * <br>
     * <br>
     * Sobald eine Taste gedrückt/gehalten wird, erkennt Java diesen Input und führt diese Methode aus.
     * Das KeyEvent e speichert dieses Ereignis, also die Inputs die getätigt wurden, und ermöglichen das
     * Auslesen der Tasteninputs. Diese Tasteninputs werden anschließend in einem Zwischenspeicher gespeichert.
     * <br>
     * Diese Tasteninputs werden so lange gespeichert, bis sie wieder losgelassen werden.
     * Damit kann man beispielsweise eine flüssigere Bewegung erreichen:
     * <br>
     * <br>
     * Jedes Mal, wenn eine Taste gehalten wird, wie beispielsweise bei der Bewegung eines Objektes, gibt es ein
     * Moment, indem die Bewegung "stock". Das könnte an einer internen Umschaltung zwischen "kurz gedrückt" und "gehalten" ligen.
     * Um dieses Problem zu beheben, wird jeder Tasteninput zwischengespeichert. Eine Taste ist dann entweder gedrückt oder nicht.
     * mit einer unabhängigen Update-Loop (siehe UpdateThread) kann dies dann überprüft werden. Dadurch wird diese kleine Pause kompensiert.
     * Das hat aber auch zur Folge, dass man die KeyTyped() Methode nicht mehr braucht, da es nicht mehr drauf ankommt wie lange,
     * sondern ob die Taste gedrückt wurde.
     *
     * @param e Das Key Event, über das man die Inputs auslesen/verarbeiten kann.
     */
    @Override
    public void keyPressed(KeyEvent e)
    {
        if(!keyBuffer.contains(e.getKeyCode())) keyBuffer.add(e.getKeyCode());
    }
    
    /**
     * Verarbeitet alle Tasten, die gerade losgelassen werden.
     * <br>
     * <br>
     * Wenn eine Taste losgelassen wird, so führt Java diese Methode aus.
     * Sobald eine Taste losgelassen wird, wird sie aus der Liste an gedrückten Tasten entfernt.
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e)
    {
        keyBuffer.remove(Integer.valueOf(e.getKeyCode()));
        keyBuffer.trimToSize();
    }
}
