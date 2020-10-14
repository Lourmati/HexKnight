/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ca.qc.bdeb.utilitaires;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 
 * @author Eric Wenaas
 * @version 1.0
 */
public interface FileManager<T>
{

    /**
     * Creates the file in the directory
     * 
     * @param name The name of the file
     */
    void createFile(String name);

    /**
     * 
     * @return the active file
     */
    File getActiveFile();

    /**
     * Returns the directory containing the active file.
     * 
     * @return the directory
     */
    File getDirectory();

    /**
     * 
     * @param fileName The File name which should be in the active directory
     * 
     * @return The object loaded
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    T loadFile(String fileName) throws FileNotFoundException, IOException,
            ClassNotFoundException;

    /**
     * The class will forget about the active file thus a call to save will act
     * as a save as
     */
    void resetActiveFile();

    /**
     * Serializes the Object in the Active File, thus overwriting the file.
     * 
     * @param object The object
     * @throws FileNotFoundException
     * @throws IOException
     */
    void saveFile(T object) throws FileNotFoundException, IOException;

    /**
     * Serializes the object. Creates a new file.
     * 
     * @param object The object
     * @param fileName The File Name
     * @throws FileNotFoundException
     * @throws IOException
     */
    void saveFileAs(T object, String fileName) throws FileNotFoundException,
            IOException;

}
