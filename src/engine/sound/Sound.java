package engine.sound;

import javax.sound.sampled.*;
import java.io.*;

/**
 * Mit der Soundklasse kann man eine Audiodatei importieren und abspielen.
 * <br>
 * <br>
 * Um eine Audiodatei abzuspielen, kann man ein neues Soundobjekt im Konstruktor erstellen.
 * Über dieses Objekt kann man dann mithilfe der getAudioClip() Methode den Sound abspielen.
 * <br>
 * Wenn ein sound abgespielt wurde, dann kann man ihn nicht ohne weiteres erneut abspielen.
 * Damit man den Sound erneut abspielen kann, muss man die derzeitige Position der Audiodatei vorerst zurücksetzten.
 *
 * @see javax.sound.sampled.Clip
 */
public class Sound
{
    /**
     * Der Soundclip, welcher die Audiodatei abspielt.
     */
    Clip clip;
    
    /**
     * Erstellt ein neues Soundobjekt, und importiert eine Audiodatei.
     * Diese Audiodatei kann anschließend über das Soundobjekt, bzw. dessen
     * clips abgespielt werden.
     *
     * @param soundFileUrl Dateipfad der Audiodatei
     */
    public Sound(String soundFileUrl)
    {
        try
        {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundFileUrl));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());
        }
    }
    
    /**
     * Setzt die Soundposition zurück.
     * <br>
     * <br>
     * Jedes Mal, wenn ein Audioclip abspielt wird, speichert intern eine Variable ab wo in dem Audioclip
     * der Computer ist. Wenn der Sound fertig abgespielt ist, ist diese Variable am Ende des Audioclips angekommen.
     * <br>
     * Will man diesen Audioclip nun erneut starten, so ertönt kein Ton. Das liegt daran, dass die Variable am Ende
     * angekommen ist und am Ende keine Daten für einen Sound abgespeichert sind.
     * <br>
     * Diese Methode setzt diese interne Variable wieder auf den Anfang, damit der sound erneut abgespielt werden kann.
     */
    public void reset()
    {
        getAudioClip().setFramePosition(0);
    }
    
    /**
     * Gibt den Audioclip zurück.
     * <br>
     * <br>
     * Über diesen Audioclip kann das Geräusch gestartet, gestoppt oder auch überprüft werden,
     * ob der Sound abgespielt wird.
     *
     * @return Audio Clip
     */
    public Clip getAudioClip()
    {
        return this.clip;
    }
}
