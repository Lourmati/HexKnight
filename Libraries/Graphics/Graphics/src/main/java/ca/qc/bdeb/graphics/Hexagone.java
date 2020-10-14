package ca.qc.bdeb.graphics;

import ca.qc.bdeb.graphics.FormeAbstraite;
import java.awt.geom.GeneralPath;

/**
 * Cette classe représente un hexagone. Le côté plat est sur le dessus et la pointe sur les côtés.
 * 
 * 
 * @author Eric Wenaas
 * @version 0.9
 */
final public class Hexagone extends FormeAbstraite {

    private final double miTaille;
    private final double taille;

    /**
     * Permet de construire un hexagone en position x.y.  La taille d'un hexagone représente la taille des côtés.
     * 
     * @param taille La taille de l'hexagone
     * @param x coordonnée x du point à gauche en haut
     * @param y coordonnée x du point à gauche en haut
     */
    public Hexagone(double taille, int x, int y) {
        super(x, y);
        this.miTaille = taille / 2;
        this.taille = taille;
        setGeneralPath(construireGeneralPath());
    }

    /**
     * Permet d'obtenir la hauteur d'un hexagone à partir d'une taille 
     * 
     * @param taille la taille d'un hexagone
     * @return la hateur en pixels
     */
    public static double getHauteur(double taille) {
        Hexagone bidon = new Hexagone(taille, 0, 0);
        return bidon.getBounds().height;

    }

    /**
     * Permet d'obtenir la largeur de l'hexagone en fonction de sa taille. Les pointes sont considérées.
     * 
     * @param taille
     * @return la largeur de l'hexagone
     */
    public static double getLargeur(double taille) {
        Hexagone bidon = new Hexagone(taille, 0, 0);
        return bidon.getBounds().width;
    }

    /**
     * Calcule la taille de la pointe de l'hexagone. La pointe est les triangle dépassant
     * les bases de l'hexagone.
     * 
     * @param taille Le rayon de l'hexagone
     * @return la taille de la pointe
     */
    public static double calculerTaillePointe(double taille) {
        return taille / 2 * Math.sin(Math.PI / 6.0);
    }
   
    /**
     * Calcule la taille de l'hexagone à partir de la hauteur. C'est l'inverse de la fonction
     * getHauteur().
     * 
     * @return la hauteur
     * @param hauteur la taille d'un côté de l'hexagone
     */
    public static double obtenirTaille(double hauteur) {
        double taille = hauteur / Math.sin(Math.PI/3.0);
        return taille;
    } 

    @Override
    protected GeneralPath construireGeneralPath() {
        int taillePointe = (int) Math.ceil (calculerTaillePointe(taille));
        double hauteur = taille * Math.sin(Math.PI / 3.0);
        int miHauteur = (int) Math.ceil (hauteur / 2);
        int start_x = getX() + taillePointe;
        int start_y = getY();

        // Trace l'hexagone. Il est intéressant de noter que getX() et getY() ne sont pas dans l'hexagone.
        GeneralPath chemin = new GeneralPath();
        chemin.moveTo(start_x, start_y);
        chemin.lineTo(start_x + miTaille, start_y);
        chemin.lineTo(start_x + miTaille + taillePointe, start_y + miHauteur);
        chemin.lineTo(start_x + miTaille, start_y + 2 * miHauteur);
        chemin.lineTo(start_x, start_y + 2 * miHauteur);
        chemin.lineTo(start_x - taillePointe, start_y + miHauteur);
        chemin.closePath();
        return chemin;
    }
}
