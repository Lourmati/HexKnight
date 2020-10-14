/*
 * Copyright (C) 2015 Éric Wenaas
 *
 */

package ca.qc.bdeb.hexknight.ui;

import ca.qc.bdeb.graphics.Sprite;
import ca.qc.bdeb.graphics.UtilitairesGraphique;
import ca.qc.bdeb.hexknight.model.Joueur;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 *
 * @author Eric Wenaas <eric.wenaas@bdeb.qc.ca>
 * @version 0.1
 */

class SpritePaquetJoueur extends Sprite {
    
    private final Joueur leJoueur;

    public SpritePaquetJoueur(BufferedImage image, Joueur unJoueur) {
        super(image);
        this.leJoueur = unJoueur;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        Rectangle bounds = new Rectangle(0, 0, getWidth(), getHeight());
        String taillePaquet = Integer.toString(leJoueur.getTaillePaquet());
        g2d.setColor(Color.WHITE);
        UtilitairesGraphique.centrerTexte(g2d, taillePaquet, PolicesInterface.policeTresGrande, bounds);
    }
    

}
