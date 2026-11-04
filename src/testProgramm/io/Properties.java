package testProgramm.io;

import java.io.*;

/**
 * Die Klasse Properties sorgt für einen leichten Umgang mit Properties-Dateien.
 * Sie erweitert die Klasse java.util.Properties
 */
public class Properties extends java.util.Properties
{
    /**
     * Bereitet den Austausch vor.
     * @param path Der Properties Datei Pfad.
     */
    public Properties(String path)
    {
        try {
            load(new FileInputStream(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gibt ein abgespeicherter Integer zurück.
     */
    public int getInt(String key)
    {
        return Integer.parseInt(super.getProperty(key));
    }

    /**
     * Gibt eine abgespeicherte Long zurück.
     */
    public long getLong(String key)
    {
        return Long.parseLong(super.getProperty(key));
    }
    
    /**
     * Gibt ein abgespeicherter Float zurück.
     */
    public float getFloat(String key)
    {
        return Float.parseFloat(super.getProperty(key));
    }

    /**
     * Gibt einen abgespeicherten boolean zurück.
     */
    public boolean getBoolean(String key)
    {
        return Boolean.parseBoolean(super.getProperty(key));
    }
}
