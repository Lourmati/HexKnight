/*
 * 
 * Copyright (C) 2016 Éric Wenaas
 * 
 */
package ca.qc.bdeb.hexknight.ui;

import ca.qc.bdeb.graphics.FormeAbstraite;
import ca.qc.bdeb.graphics.UtilitairesGraphique;
import ca.qc.bdeb.hexknight.model.TuileJeu;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

/**
 *
 * @author Éric Wenaas <eric.wenaas@bdeb.qc.ca>
 * @version 0.1
 */
public class DessinateurCoordonnees implements DessinateurElementTuile {

    @Override
    public void dessinerElement(Graphics2D g2d, TuileJeu tuile, FormeAbstraite forme) {
        g2d.setColor(Color.BLACK);
        String chaineCoordonnees = tuile.getPosition().toString();
        afficherCercleCoordonnees(g2d, chaineCoordonnees, PolicesInterface.policeNormale, forme.getBounds());
        UtilitairesGraphique.centrerTexte(g2d, chaineCoordonnees, PolicesInterface.policeNormale, forme.getBounds());
    }

    private void afficherCercleCoordonnees(Graphics2D g2d, String chaine, Font fonte, Rectangle bornes) {
        Image imageCercle = RepertoireImages.getImageCercle();
        int x = (int) bornes.getX() + (bornes.width - 54) / 2;
        int y = (int) bornes.getY() + (bornes.height - 54) / 2;
        g2d.drawImage(imageCercle, x, y, 54, 54, null);
    }

}
