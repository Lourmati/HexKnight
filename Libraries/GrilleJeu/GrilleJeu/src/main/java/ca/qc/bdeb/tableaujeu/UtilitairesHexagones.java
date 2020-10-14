package ca.qc.bdeb.tableaujeu;

import java.util.LinkedList;
import java.util.List;

/**
 * Classe de fonctions utilitaires pour faire le calcul des grilles avec des tuiles hexagonales.
 * 
 * @author Eric Wenaas
 */
public final class UtilitairesHexagones {

    /**
     * Calcule la distance entre deux hexagones. La distance est en nombre de tuiles.
     * 
     * @param depart
     * @param arrivee
     * @return La distance entre les deux
     */
    public static int computeDistance(PositionGrille depart, PositionGrille arrivee) {
        int ux = array2hexX(depart.getX(), depart.getY());
        int uy = array2hexY(depart.getX(), depart.getY());
        int vx = array2hexX(arrivee.getX(), arrivee.getY());
        int vy = array2hexY(arrivee.getX(), arrivee.getY());

        int deltaX = vx - ux;
        int deltaY = vy - uy;

        return (Math.abs (deltaX) + Math.abs(deltaY) + Math.abs(deltaX - deltaY)) / 2;
    }

    private static int array2hexX(int x, int y) {
        return y - (int) Math.floor(x / 2.0);
    }

    private static int array2hexY(int x, int y) {
        return y + (int) Math.ceil(x / 2.0);
    }

    /**
     * Fonction qui permet de trouver toutes les Position voisines d'une 
     * position passée en paramètre dans un contexte de grille hesagonale.
     * 
     * @param largeur Largeur de la grille hexagonale
     * @param hauteur Hauteur de la grille hexagonale
     * @param x Valeur x de la position
     * @param y Valeur y de la position
     * @return 
     */
    public static List<PositionGrille> getVoisins(int largeur, int hauteur,
        int x, int y) {
        List<PositionGrille> answer = new LinkedList<PositionGrille>();
        int new_x = x;
        int new_y = y;

        // NORD
        new_y = y - 1;
        new_x = x;
        if (new_y >= 0 && new_y < hauteur && new_x >= 0 && new_x < largeur) {
            answer.add(new PositionGrille(new_x, new_y));
        }

        // NORD_EST
        new_y = x % 2 == 0 ? y - 1 : y;
        new_x = x + 1;
        if (new_y >= 0 && new_y < hauteur && new_x >= 0 && new_x < largeur) {
            answer.add(new PositionGrille(new_x, new_y));
        }

        // SUD_EST
        new_y = x % 2 == 1 ? y + 1 : y;
        new_x = x + 1;
        if (new_y >= 0 && new_y < hauteur && new_x >= 0 && new_x < largeur) {
            answer.add(new PositionGrille(new_x, new_y));
        }

        // SUD
        new_y = y + 1;
        new_x = x;
        if (new_y >= 0 && new_y < hauteur && new_x >= 0 && new_x < largeur) {
            answer.add(new PositionGrille(new_x, new_y));
        }

        // SUD_OUEST
        new_y = x % 2 == 1 ? y + 1 : y;
        new_x = x - 1;
        if (new_y >= 0 && new_y < hauteur && new_x >= 0 && new_x < largeur) {
            answer.add(new PositionGrille(new_x, new_y));
        }

        // NORD_OUEST
        new_y = x % 2 == 0 ? y - 1 : y;
        new_x = x - 1;
        if (new_y >= 0 && new_y < hauteur && new_x >= 0 && new_x < largeur) {
            answer.add(new PositionGrille(new_x, new_y));
        }

        return answer;
    }
}
