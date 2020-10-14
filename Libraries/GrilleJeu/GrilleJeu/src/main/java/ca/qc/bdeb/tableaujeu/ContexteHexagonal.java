package ca.qc.bdeb.tableaujeu;

import ca.qc.bdeb.graphics.FormeAbstraite;
import ca.qc.bdeb.graphics.Hexagone;
import java.awt.Point;

/**
 *
 * @author Eric Wenaas
 */
class ContexteHexagonal implements ContexteTableau {
    
    
    private final double taille;
    
    /**
     * Il faut spécifier la taille à ce contexte car les formes ne possèdent pas de taille dans le sens où on
     * l'entend ici.
     * 
     * @param taille la taille des formes qu'il faut traiter
     */
    ContexteHexagonal(double taille) {
        this.taille = taille;
    }

    @Override
    public double calculerHauteurRequise(int nombreTuiles) {
        double hauteur = Hexagone.getHauteur(taille);
        double foreachHex = hauteur * ((double) nombreTuiles - 0.5) + 1; // Sans le +1 on ne voit pas la grille.
        return foreachHex + hauteur;
    }
        
    @Override
    public double calculerTaillePossibleHauteur(double hauteur, int nombreHexagone) {
        double h = hauteur / (2.0 * nombreHexagone + 1);
        double rayon = 2 * Hexagone.obtenirTaille(h);
        return rayon;
    }


    @Override
    public double calculerLargeurRequise(int nombreTuiles) {
        double taillePointe = Hexagone.calculerTaillePointe(taille);
        double largeurSansDernierePointe = calculerEspacementLargeur() * nombreTuiles;
        return largeurSansDernierePointe + taillePointe;
    }

    @Override
    public double calculerTaillePossibleLargeur(double largeur, int nombreHexagone) {
        double multiplicateur = Math.cos(Math.PI / 3.0);
        double diviseurLargeur = ((nombreHexagone + 1) * multiplicateur + nombreHexagone);
        return largeur / diviseurLargeur;
    }       

    @Override
    public FormeAbstraite getForme() {
        return new Hexagone(taille, 0, 0);
    }

    @Override
    public PositionGrille calculerPositionDansGrille(int x, int y) {
        double miHauteur = Hexagone.getHauteur(taille) / 2;
        double largeur = Hexagone.getLargeur(taille) - Hexagone.calculerTaillePointe(taille);
        int coteR = (int) (largeur / 3.0);  // Parce que l'hexagone est regulier

        int col = (int) (x / largeur);
        int line = (int) (y / (miHauteur * 2));

        // Calcule les positions en X et en Y relatives au coin haut et gauche
        // dans l'hexagone
        int relativeX = (int) (x - col * largeur);
        int relativeY = (int) (y - line * (miHauteur * 2));

        assert (relativeX < largeur);
        assert (relativeY < (miHauteur * 2));

        double pente = (double) miHauteur / (double) coteR;

        // Ce bout de code verifie dans quel hexagone on est en fonction de la pente
        if (col % 2 == 0) {
            if (relativeY <= (miHauteur - relativeX * pente)) {
                col--;
                line--;
            } else if (relativeY >= (miHauteur + relativeX * pente)) {
                col--;
            }
        } else {

            if (relativeY >= relativeX * pente && relativeY <= 2 * miHauteur - relativeX * pente) {
                col--;
            } else if (relativeY <= miHauteur) {
                line--;
            }
        }
        return new PositionGrille(col, line);
    }


    @Override
    public Point.Double calculerCoordonneesForme(PositionGrille p) {
        int x = p.getX();
        int y = p.getY();
        double posX = x * (Hexagone.getLargeur(taille) - Hexagone.calculerTaillePointe(taille));
        double posY = y * Hexagone.getHauteur(taille);
        if (x % 2 == 1) {
            posY += Hexagone.getHauteur(taille) / 2.0;
        }
        return new Point.Double(posX, posY);
    }

    @Override
    public double calculerEspacementLargeur() {
        return Hexagone.getLargeur(taille) * 3.0 / 4.0;
    }
    
    @Override
    public double calculerEspacementHauteur() {
        return Hexagone.getHauteur(taille);
    }
}
