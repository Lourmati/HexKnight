package ca.qc.bdeb.tableaujeu;

import java.io.Serializable;

/**
 * Représente une position en x, y dans une grille.
 * Cette classe est immuable. La classe java.awt.Point aurait
 * pu être utilisée mais cette classe est là pour distinguer une
 * position sur la grille et une position en pixels.
 *
 * @author Éric Wenaas
 */
public final class PositionGrille implements Serializable {
    private final int x;
    private final int y;

    /**
     * Une position doit absolument avoir un x et un y
     *
     * @param x Valeur pour x
     * @param y Valeur pour y
     */
    public PositionGrille(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Retourne la valeur de x
     *
     * @return valeur de x
     */
    public int getX() {
        return x;
    }

    /**
     * Retourne la valeur de y
     *
     * @return valeur de y
     */
    public int getY() {
        return y;
    }

    /**
     * Pour qu'une position soit égale à une autre, elle doivent avoir les mêmes
     * valeurs pour x et y.
     * 
     * @param o doit être une instance de <code>Position</code>
     * @return vrai si les position sont identiques
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || ! (o instanceof PositionGrille)) {
            return false;
        }
        PositionGrille autre = (PositionGrille) o;
        return x == autre.x && y == autre.y;
    }

    /**
     * Retourne le code de hachage de la position. La fonction de hachage n'est 
     * pas très recherchée et aucun test de performance n'a été fait pour 
     * l'évaluer.
     *
     * @return  le code de hachage
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash += 61 * hash + this.x;
        hash += 61 * hash + this.y;
        return hash;
    }
    
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(x);
        builder.append(",");
        builder.append(y);
        return builder.toString();
    }
}
