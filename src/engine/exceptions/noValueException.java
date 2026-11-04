package engine.exceptions;

/**
 * Diese Fehlernachricht wird ausgegeben, wenn ein Wert 'null' ist oder 'null' übergeben wird.
 *
 * @see RuntimeException
 */
public class noValueException extends RuntimeException
{
    public noValueException(String message, int uID)
    {
        super(new ExceptionFormatter(message, uID).getFormattedMessage());
    }
}
