package ca.qc.bdeb.graphics;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * Classe comprenant diverses fonctions utilitaires en java 2D
 *
 * @author Eric Wenaas
 * @version 1.0
 */
public class UtilitairesGraphique {

    /**
     * Fonction qui charge une ressource et la transforme en un image suportant la transparence, donc codée sur 4
     * octets.
     *
     * @param resourceID Le chemin vers la ressource
     * @return L'image codé sous 4 octets
     */
    public static BufferedImage chargerImage(URL resourceID) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(resourceID);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return image;
    }

    /**
     * Ajoute de la transparence à une image.
     *
     * @param image L'image initiale
     * @param alpha La transparence qu'il faut ajouter
     * @return L'image avec de la transparence
     */
    public static BufferedImage ajouterTransparence(BufferedImage image, char alpha) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage nouvelleImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int couleur = image.getRGB(x, y);
                if (! dejaTransparent(couleur)) {
                    couleur |= 0xff000000;
                    int mc = (alpha << 24) | 0x00ffffff;
                    int nouvelle = couleur & mc;
                    nouvelleImage.setRGB(x, y, nouvelle);
                }
            }
        }
        return nouvelleImage;
    }
    
    public static void centrerFenetreSurEcran(JFrame frame) {
        Dimension tailleEcran = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension tailleFenetre = frame.getSize();
        int x = (tailleEcran.width - tailleFenetre.width) / 2;
        int y = (tailleEcran.height - tailleFenetre.height) / 2;
        frame.setLocation(x, y);
    }

    private static boolean dejaTransparent(int couleur) {
        return couleur >> 24 << 24 == 0;
    }

    /**
     * Centre une chaîne de caractères dans un rectangle
     *
     * @param g2d L'objet graphique
     * @param chaine la chaîne à centrer
     * @param police La police de caractère
     * @param zone Le rectangle où on centre la chaine
     */
    public static void centrerTexte(Graphics2D g2d, String chaine, Font police, Rectangle2D zone) {
        double largeur = zone.getWidth();
        double hauteur = zone.getHeight();
        FontMetrics metrique = g2d.getFontMetrics(police);
        g2d.setFont(police);
        Rectangle2D espaceCaractere = metrique.getStringBounds(chaine, g2d);
        double xPos = zone.getX() + (largeur - espaceCaractere.getWidth()) / 2;
        double yPos = zone.getY() + (hauteur - espaceCaractere.getHeight()) / 2 + metrique.getAscent();
        g2d.drawString(chaine, (int) xPos, (int) yPos);
    }

    /**
     * Procège à la rotation d'une image. L'image initiale est remplacée par le résultat de la rotation.
     *
     * @param image L'image qui fait une rotation
     * @param angleEnDegres l'angle
     * @return L'image retournée
     */
    public static BufferedImage faireRotation(BufferedImage image, int angleEnDegres) {
        Graphics2D g = image.createGraphics();
        g.rotate(Math.toRadians(angleEnDegres));
        g.drawImage(image, null, null);
        return image;
    }

    /**
     * Détermine l'espace disponible à l'écran en tenant compte des barres de tâches. N'a pas été testée sur tous les
     * environnements.
     *
     * @return la taille maximale qu'on peut utiliser sans cacher la barre des tâches.
     */
    public static Dimension determinerEspaceDisponible() {
        Rectangle zoneEcran = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        int width = zoneEcran.width;
        int height = zoneEcran.height;
        if (System.getProperty("os.name").toLowerCase().equals("linux")) {
            String desktopName = System.getenv().get("XDG_CURRENT_DESKTOP");
            if (desktopName != null && desktopName.equals("X-Cinnamon")) {
                // TODO: La méhode précédente ne retourne pas les bonnes valeurs sur Linux                  // Mint avec Cinammon. Il faut donc la patcher.
                height -= 25;
            } else {
                // System.err.println(desktopName);
            }
        }

        return new Dimension(width, height);
    }
	
    /**
     * Crée une BufferedImage encodée sur 32 bits à partir d'une ressource
     * spécifiée par le URL
     * 
     * @param resourceID la ressource contenant le fichier d'image
     * @return Une BufferedImage
     */
    public static BufferedImage creerImage4Octets(URL resourceID) {
        BufferedImage image = null;
        try {
            BufferedImage original = ImageIO.read(resourceID);
            image = new BufferedImage(original.getWidth(), original.getHeight(),
                    BufferedImage.TYPE_4BYTE_ABGR);
            for (int cx = 0; cx < original.getWidth(); cx++) {
                for (int cy = 0; cy < original.getHeight(); cy++) {
                    image.setRGB(cx, cy, original.getRGB(cx, cy));
                }
            }
        } catch (IOException ex) {
        }
        return image;
    }
    
    /**
     * Centers the String s written with Font f in Component c
     *
     * @param g2d
     * @param s
     * @param f
     * @param middlePoint
     */
    public static Point2D getPointOnMiddle(Graphics2D g2d, 
                                          String s, 
                                          Font f,
                                          Point2D.Double middlePoint) {
        double x = middlePoint.getX();
        double y = middlePoint.getY();
        g2d.setFont(f);
        FontMetrics fm = g2d.getFontMetrics(f);
        Rectangle2D size = fm.getStringBounds(s, g2d);
        double xPos = (x - (size.getWidth()) / 2);
        double yPos = (y - (size.getHeight()) / 2) + fm.getAscent();
        return new Point2D.Double(xPos, yPos);
    }
}
