package ca.qc.bdeb.utilitaires;

import java.io.File;

/**
 * 
 * @version 1.0
 */
public abstract class AbstractFileManager<T> implements FileManager<T>
{

    // The file
    protected File _targetFile;

    // The directory containing the file
    final protected File _directory;

    /**
     * Constructor
     * 
     * @param path The path pointing to the directory that will contain the file
     */
    public AbstractFileManager(String path)
    {
        _targetFile = null;
        _directory = findDirectory(path);
    }

    /**
     * Creates the file in the directory
     * 
     * @param name The name of the file
     */
    @Override
    public final void createFile(String name)
    {
        _targetFile = new File(_directory + name);
    }

    /**
     * 
     * @return the active file
     */
    @Override
    public final File getActiveFile()
    {
        return _targetFile;
    }

    /**
     * Returns the directory containing the active file.
     * 
     * @return the directory
     */
    @Override
    public final File getDirectory()
    {
        return _directory;
    }

    /**
     * The class will forget about the active file thus a call to save will act
     * as a save as
     */
    @Override
    public final void resetActiveFile()
    {
        _targetFile = null;
    }

    /*
     * This method returns a directory according to the operating system. It
     * creates The directory if it does not exist.
     */
    private File findDirectory(String path)
    {
        File answer = new File(path);
        answer.mkdirs();
        return answer;
    }
}
