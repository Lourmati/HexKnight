package ca.qc.bdeb.graphics;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.GeneralPath;

/**
 * Classe abstraite implémentant l'interface Shape. La classe conserve un GeneralPath qui
 * doit être construit par la sous-classe. Toutes les opérations renvoient à ce GeneralPath.
 * 
 * Les sous-classes doivent appeler leur implémentation de setGeneralPath() avant toute utilisation.
 * 
 * @author Eric Wenaas
 * @version 0.8
 */
public abstract class FormeAbstraite implements Shape {
    
    private GeneralPath chemin;
    private int x;
    private int y;

    public FormeAbstraite(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Retourne la valeur de la postion du coin gauche en haut
     * 
     * @return la valeur sur l'axe des x
     */
    public final int getX() {
        return x;
    }
    
    /**
     * Retourne la valeur de la postion du coin gauche en haut
     * 
     * @return la valeur sur l'axe des y
     */
    public final int getY() {
        return y;
    }
    
    /**
     * Les classes concretes sont responsables de fournir un Generalth. Cette méthode doit être appelée
     * par la sous-classe avant toute utilisation de la classe.
     * 
     * @param chemin le chemin de la forme
     */
    protected final void setGeneralPath(GeneralPath chemin) {
        this.chemin = chemin;
    }
    
    /**
     * Fonction qui permet de construire le tracé de la forme
     * 
     * @return le tracé de la forme
     */
    protected abstract GeneralPath construireGeneralPath();
    
    /**
     * Permet de déplacer la forme. Elle utilise la fonction abstraite construireGeneralPath()
     * 
     * @param posX
     * @param posY 
     */
    public void setLocation(int posX, int posY) {
        this.x = posX;
        this.y = posY;
        setGeneralPath(construireGeneralPath());
    }

    @Override
    public final boolean contains(Point2D point) {
        return chemin.contains(point);
    }

    @Override
    public final boolean contains(Rectangle2D zone) {
        return chemin.contains(zone);
    }

    @Override
    public final boolean contains(double x, double y) {
        return chemin.contains(x, y);
    }

    @Override
    public final boolean contains(double x, double y, double largeur, double hauteur) {
        return chemin.contains(x, y, largeur, hauteur);
    }

    @Override
    public final Rectangle getBounds() {
        return chemin.getBounds();
    }

    @Override
    public final Rectangle2D getBounds2D() {
        return chemin.getBounds2D();
    }

    @Override
    public final PathIterator getPathIterator(AffineTransform transformation) {
        return chemin.getPathIterator(transformation);
    }

    @Override
    public final PathIterator getPathIterator(AffineTransform transformation, double arg1) {
        return chemin.getPathIterator(transformation, arg1);
    }

    @Override
    public final boolean intersects(Rectangle2D zone) {
        return chemin.intersects(zone);
    }

    @Override
    public final boolean intersects(double x, double y, double largeur, double hauteur) {
        return chemin.intersects(x, y, largeur, hauteur);
    }
}
