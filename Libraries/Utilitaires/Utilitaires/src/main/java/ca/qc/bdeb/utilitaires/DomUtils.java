package ca.qc.bdeb.utilitaires;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Classe qui comprend quelques méthodes utilitaires pour manipuler des fichier XML.
 * 
 * @author Éric Wenaas <eric.wenaas@bdeb.qc.ca>
 * @version 1.0
 * 
 */
public class DomUtils {
   
    /**
     * Méthode qui crée un DocumentBuilder. Les exceptions sont affichées dans le Logger.
     * 
     * @return une instance de DocumentBuilder
     */
    public static DocumentBuilder createDocumentBuilder() {
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(DomUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return docBuilder;
    }
 
    /**
     * Méthode qui ouvre un fichier XML, l'interprète et retourne le document DOM correspondant.
     * Une exception de type RuntimeException est lancée en cas d'erreur.
     * 
     * @param path Le chemin d'accès vers le fichier xml.
     * @return Le document
     */
    public static Document openDocument(String path) {
        Document answer = null;
        try {
            DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            File f = new File(path);
            answer = parser.parse(f);
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            throw new RuntimeException(ex.toString());
        }

        return answer;
    }

    /**
     * Méthode qui sauvegarde le contenu d'un document DOM dans un fichier XML. Si une erreur
     * survient, une RuntimeException est lancée.
     * 
     * @param fichierDestination Le fichier où on écrit
     * @param contenuDOM LA source
     */
    public static void sauvegarderFichier(File fichierDestination, Document contenuDOM) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fichierDestination));
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            DOMSource source = new DOMSource(contenuDOM);
            StreamResult result = new StreamResult(writer);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
        } catch (IOException | TransformerException ex) {
            throw new RuntimeException(ex.toString());

        }
    }
}
