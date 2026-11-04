package engine.exceptions;

/**
 * Diese Fehlernachricht wird ausgegeben, wenn ein Wert nicht mehr innerhalb seiner
 * Grenzen ist.
 * <br>
 * <br>
 * Das kann beispielsweise eine Zahl sein die negativ oder zu groß ist.
 * Was es für Grenzen gibt muss jedoch bei der Methode selbst definiert werden.
 * <br>
 * <br>
 * Beispiel:
 * <br>
 * if(a < 0) throw new OutOfBoundsException("Die zahl a ist negativ" + getUID);
 * <br>
 * <br>
 * In diesem Beispiel wird eine Fehlermeldung ausgegeben, wenn die Variable a negativ ist.
 * Die uID ist die vom GameObject, welches die Fehlermeldung ausgibt. Das ist für das debuggen sehr hilfreich.
 */
public class OutOfBoundsException extends RuntimeException
{
    public OutOfBoundsException(String message, int uID)
    {
        super(new ExceptionFormatter(message, uID).getFormattedMessage());
    }
}
