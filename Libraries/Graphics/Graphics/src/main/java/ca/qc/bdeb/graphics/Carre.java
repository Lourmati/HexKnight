package ca.qc.bdeb.graphics;

import java.awt.geom.GeneralPath;

/**
 * Un simple carré...
 * 
 * @author Eric Wenaas
 */
public class Carre extends FormeAbstraite {

    private double taille;

    /**
     * Constructeur du carré.
     * 
     * @param taille la taille d'un côté
     * @param x coin gauche en haut
     * @param y coin gauche en haut
     */
    public Carre(double taille, int x, int y) {
        super(x, y);
        this.taille = taille;
        setGeneralPath(construireGeneralPath());
    }

    @Override
    protected GeneralPath construireGeneralPath() {
        int start_x = getX();
        int start_y = getY();
        GeneralPath chemin = new GeneralPath();
        chemin.moveTo(start_x, start_y);
        chemin.lineTo(start_x + taille, start_y);
        chemin.lineTo(start_x + taille, start_y + taille);
        chemin.lineTo(start_x, start_y + taille);
        chemin.closePath();
        return chemin;
    }
}
