package ca.qc.bdeb.utilitaires;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Eric Wenaas
 */
public class UtilitairesFichiers {
    
    public static File[] getFichiers(URI location) {
        File dossier = null;
        dossier = new File(location);
        ArrayList<File> fichiers = new ArrayList<>();
        ajouterFichiers(fichiers, dossier);
        return fichiers.toArray(new File[0]);
    }

    public static File[] getFichiers(String folderPath) {
        File dossier = null;
        try {
            dossier = new File(UtilitairesFichiers.class.getResource(folderPath).toURI());
        } catch (URISyntaxException ex) {
            Logger.getLogger(UtilitairesFichiers.class.getName()).log(Level.SEVERE, null, ex);
        }
        ArrayList<File> fichiers = new ArrayList<>();
        ajouterFichiers(fichiers, dossier);
        return fichiers.toArray(new File[0]);
    }

    private static void ajouterFichiers(ArrayList<File> fichiers, File dossier) {
        for (File f : dossier.listFiles()) {
            if (f.isDirectory()) {
                ajouterFichiers(fichiers, f);
            } else {
                fichiers.add(f);
            }
        }
    }
}
