package ca.qc.bdeb.graphics;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;

/**
 * Une classe qui comprend diverses méthodes statiques pour faciliter le positionnement des objets en 2D.
 * 
 * @author Eric Wenaas
 */
public class Geometrie {

    /**
     * Permet de placer le composant fils dans le père à la position spécifiée. Le composant fils n'est pas ajouté au fils
     * par la fonction, ce doit être fait à l'extérieur de la fonction.
     * 
     * @param pere Le composant père
     * @param fils Le composant fils
     * @param position La position dans le père
     */
    public static void placerSurPere(Component pere, Component fils, Position position) {
        Point endroit = calculerPosition(fils, pere, position);
        fils.setLocation(endroit.x, endroit.y);
    }

    /**
     * Place un composant en fonction d'un autre en une certaine position. Il n'y a pas de relation père-fils entre les compo
     * sants. Il ne doit pas y en avoir non plus. Dans ce cas, il faudrait utiliser la fonction placerFilsSurPere.
     * 
     * @param cible
     * @param sujet
     * @param position 
     */
    public static void placerSurComposant(Component cible, Component sujet, Position position) {
        Point endroit = calculerPosition(cible, sujet, position);
        Rectangle bounds = cible.getBounds();
        sujet.setLocation(endroit.x + bounds.x, endroit.y + bounds.y);
    }
    
    private static Point calculerPosition(Component cible, Component sujet, Position position) {
        Dimension taillePere = cible.getSize();
        Dimension tailleFils = sujet.getSize();

        int x = 0;
        int y = 0;

        switch (position) {
        case CENTRE:
            x = (taillePere.width - tailleFils.width) / 2;
            y = (taillePere.height - tailleFils.height) / 2;
            break;
        default:
            throw new RuntimeException("Pas implémenté");
        }
        return new Point(x, y);
    }
    
        public static Point2D pointIntersection(Circle s, Line2D.Double ligne) {
        if (! s.contains(ligne.getP1())) {
            ligne = new Line2D.Double(ligne.getP2(), ligne.getP2());
        }
        double x1 = ligne.getX1();
        double x2 = ligne.getX2();
        double y1 = ligne.getY1();
        double y2 = ligne.getY2();
        Point2D P1 = new Point2D(x1, y1);
        Point2D P2 = new Point2D(x2, y2);
        double distance = s.getRayon();
        Vector2D vectorLine = new Vector2D(P1, P2);
        double angle = vectorLine.angle();
        Point2D newPoint = Point2D.createPolar(P1, distance, angle);

        return newPoint;
    }
    
    public static java.awt.geom.Point2D.Double convert(Point2D point) {
        return new java.awt.geom.Point2D.Double(point.x(), point.y());
    }
}
