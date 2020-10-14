package ca.qc.bdeb.utilitaires;

import java.io.*;

/**
 * This class holds an instance to a file. The file is made according to a
 * serialized object. It supports operation like save as, save or load file.
 * 
 * @author Eric Wenaas
 * @version 1.0
 * @param T The type that will be serialized
 */
final public class SerializableFileManager<T extends Serializable> extends
        AbstractFileManager<T>
{

    public SerializableFileManager(String path)
    {
        super(path);
    }

    /**
     * Serializes the Object in the Active File, thus overwiting the file.
     * 
     * @param object The object
     * @throws FileNotFoundException
     * @throws IOException
     */
    @Override
    public void saveFile(T object) throws FileNotFoundException, IOException
    {
        FileOutputStream fileOut = new FileOutputStream(_targetFile);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(object);
    }

    /**
     * Serializes the object. Creates a new file.
     * 
     * @param object The object
     * @param fileName The File Name
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void saveFileAs(T object, String fileName)
            throws FileNotFoundException, IOException
    {
        _targetFile = new File(_directory, fileName);
        FileOutputStream fileOut = new FileOutputStream(_targetFile);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(object);
    }

    /**
     * 
     * @param fileName The File name which should be in the active directory
     * 
     * @return The object loaded
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    public T loadFile(String fileName) throws FileNotFoundException,
            IOException, ClassNotFoundException
    {
        _targetFile = new File(_directory, fileName);
        FileInputStream fileIn = new FileInputStream(_targetFile);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        return (T) in.readObject();
    }
}
