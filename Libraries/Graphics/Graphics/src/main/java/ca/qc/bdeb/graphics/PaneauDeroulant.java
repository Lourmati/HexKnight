package ca.qc.bdeb.graphics;

// import ca.qc.bdeb.utilitaires.FonctionsIntervalles;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import javax.swing.JPanel;

/**
 * Classe qui permet de faire un paneau déroulant. Le point gauche se déplace dans les axes négatifs, ce qui permet
 * d'afficher l'ensemble du paneau. Le paneau peut dérouler dans quatre directions.
 * 
 * @author Éric Wenaas
 */
public class PaneauDeroulant extends JPanel {

    /**
     * Les directions de déroulement
     */
    public enum Direction {
        HAUT, DROITE, BAS, GAUCHE
    };

    private int vitesseDeroulement;
    private Dimension tailleFenetre;
    private Point2D coinGauche;

    /**
     * La taille de la fenêtre représente l'espace visible du paneau. Si le coin gauche ne doit jamais comporter de 
     * coordonnées positives. Lorsqu'il est à 0,0 alors le paneau est complètement à gauche.
     * 
     * @param tailleFenetre La taille de la fenêtre
     * @param coinGauche La position du coin gauche de la fenêtre
     * @param vitesse La vitesse de déroulement en pixel
     */
    public PaneauDeroulant(Dimension tailleFenetre, Point2D coinGauche, int vitesse) {
        super();
        this.tailleFenetre = tailleFenetre;
        this.coinGauche = coinGauche;
        this.vitesseDeroulement = vitesse;
    }

    /**
     * La vitesse de déroulement est le nombre de pixels par action.
     * 
     * @return la vitesse de déroulement
     */
    public final int getVitesseDeroulement() {
        return vitesseDeroulement;
    }

    /**
     * Change la vitesse de déroulement
     * 
     * @param vDeroulement la nouvelle vitesse de déroulement
     */
    public final void setVitesseDeroulement(int vDeroulement) {
        this.vitesseDeroulement = vDeroulement;
    }

    /**
     * Le point gauche indique où se situe le paneau
     * 
     * @return Le coin gauche
     */
    public final Point2D getCoinGauche() {
        return coinGauche;
    }

    /**
     * Permet de modifier le coin gauche. Si on veut sauter à un endroit ou encore centre sur un objet, Cette fonction
     * devra être appelée.
     * 
     * @param coinGauche Le coin gauche
     */
    public final void setCoinGauche(Point2D coinGauche) {
        this.coinGauche = coinGauche;
    }

    /**
     * Fait dérouler le paneau à la vitesse déterminée.
     * 
     * @param d la direction de déroulement
     */
    public final void derouler(Direction d) {
        int x = getX();
        int y = getY();
        Dimension size = getSize();

        switch (d) {
        case HAUT:
            y -= vitesseDeroulement;
            break;
        case BAS:
            y += vitesseDeroulement;
            break;
        case DROITE:
            x -= vitesseDeroulement;
            break;
        case GAUCHE:
            x += vitesseDeroulement;
            break;
        }
        x = FonctionsIntervalles.faireDansIntervalle(tailleFenetre.width - size.width, (int) coinGauche.getX(), x);
        y = FonctionsIntervalles.faireDansIntervalle(tailleFenetre.height - size.height, (int) coinGauche.getY(), y);
        setLocation(x, y);
        repaint();
    }
}
