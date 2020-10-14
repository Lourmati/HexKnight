package ca.qc.bdeb.utilitaires;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Eric Wenaas <eric.wenaas@bdeb.qc.ca
 * @version 0.1
 */

public class IOUtils {
    
    public static String getURLPath(String resourceName) {
        URL resourceURL = IOUtils.class.getResource(resourceName);
        return resourceURL.getPath();
    }
    
    public static String getURIPath(String resourceName) {
        URL resourceURL = IOUtils.class.getResource(resourceName);
        String path = null;
        try {
           path = Paths.get(resourceURL.toURI()).toString();
        } catch (URISyntaxException ex) {
            Logger.getLogger(IOUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return  path;
    }
}
