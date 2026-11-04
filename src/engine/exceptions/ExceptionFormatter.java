package engine.exceptions;

/**
 * Der ExceptionFormatter formatiert eine Exception.
 * <br>
 * <br>
 * Dabei werden unter anderem Daten wie die uID der Fehlernachricht angehängt.
 * Diese formatierte Nachricht geht dann wieder an die Entsprechende Exception zurück und wird ggf. ausgegeben.
 */
public class ExceptionFormatter
{
    String message;
    int uid;
    
    public ExceptionFormatter(String message, int uid)
    {
        this.message = message;
        this.uid = uid;
    }
    
    /**
     * Gibt eine formatierte Fehlermeldung zurück.
     * Das Pattern welches hier verwendet wird, sieht wie folgt aus:
     * <br>
     * <br>
     * <code>
     * Exception in 'class Name' 'exception': <br>
     * Detail: message <br>
     * uID: UID <br>
     * 	at ...
     * </code>
     * @return eine formatierte Fehlermeldung
     */
    public String getFormattedMessage()
    {
        return  "\n" + "Detail: " + message +
                "\n" + "uID: " + uid;
    }
}
