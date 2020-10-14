package ca.qc.bdeb.tableaujeu;

/**
 * Petite classe immuable qui permet de transférer une zone en pourcentage.
 * Permet ensuite de calculer les coordonneés.
 * 
 * @author Eric Wenaas
 */
public class RectangleEnPourcentage {
    public final double POURC_X1;
    public final double POURC_Y1;
    public final double POURC_X2;
    public final double POURC_Y2;
    
    /**
     * Création de l'objet, les valeurs doivent être passés en pourcentage d'une
     * taille
     * @param x1 Coordonnée x du coin supérieur gauche
     * @param x2 Coordonnée x du coin inférieur droit
     * @param y1 Coordonnée y du coin supérieur gauche
     * @param y2 Coordonnée y du coin inférieur droit
     */
    public RectangleEnPourcentage(double x1, double x2, double y1, double y2) {
        this.POURC_X1 = x1;
        this.POURC_X2 = x2;
        this.POURC_Y1 = y1;
        this.POURC_Y2 = y2;
    } 

    @Override
    public String toString() {
        return "x1: " + POURC_X1 + " x2: " + POURC_X2 + "\n" +
               "y1: " + POURC_Y1 + " y2: " + POURC_Y2;
     }
}
