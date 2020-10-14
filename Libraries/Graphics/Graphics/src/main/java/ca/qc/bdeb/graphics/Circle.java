package ca.qc.bdeb.graphics;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * classe Circle
 *
 * @author Éric Wenaas <eric.wenaas@bdeb.qc.ca>
 */
public class Circle implements Shape {
    
    private final Ellipse2D.Double ellipse;
    private final Point2D.Double center;
    private final double diametre;
    
    public Circle(Point2D.Double center, double diametre) {
        ellipse = new Ellipse2D.Double(center.x - diametre / 2, center.y - diametre / 2, 
                diametre, diametre);
        this.center = center;
        this.diametre = diametre;
    }
    
    public Circle(double x, double y, double rayon) {
        this(new Point2D.Double(x, y), rayon);
    }
    
    public Point2D.Double getCenter() {
        return center;
    }
    
    public double getRayon() {
        return diametre / 2;
    }
    
    public static Line2D.Double trouverLigne(Circle s1, Circle s2) {
        Point2D.Double centre1 = s1.getCenter();
        Point2D.Double centre2 = s2.getCenter();
        Line2D.Double fullLine1 = new Line2D.Double(centre1, centre2);
        Line2D.Double fullLine2 = new Line2D.Double(centre2, centre1);
        Point2D.Double depart1 = Geometrie.convert(Geometrie.pointIntersection(s1, fullLine1));
        Point2D.Double depart2 = Geometrie.convert(Geometrie.pointIntersection(s2, fullLine2));
        return new Line2D.Double(depart1, depart2);
    }


    @Override
    public Rectangle getBounds() {
        return ellipse.getBounds();
    }

    @Override
    public Rectangle2D getBounds2D() {
        return ellipse.getBounds2D();
    }

    @Override
    public boolean contains(double x, double y) {
        return ellipse.contains(x, y);
    }

    @Override
    public boolean contains(Point2D p) {
        return ellipse.contains(p);
    }

    @Override
    public boolean intersects(double x, double y, double w, double h) {
        return ellipse.intersects(x, y, w, h);
    }

    @Override
    public boolean intersects(Rectangle2D r) {
        return ellipse.intersects(r);
    }

    @Override
    public boolean contains(double x, double y, double w, double h) {
        return ellipse.contains(x, y, w, h);
    }

    @Override
    public boolean contains(Rectangle2D r) {
        return ellipse.contains(r);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at) {
        return ellipse.getPathIterator(at);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return ellipse.getPathIterator(at, flatness);
    }
}
