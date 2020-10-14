/*
 * 
 * Copyright (C) 2016 Éric Wenaas
 * 
 */

package ca.qc.bdeb.hexknight.ui;

import ca.qc.bdeb.graphics.FormeAbstraite;
import ca.qc.bdeb.hexknight.commun.TypeTuile;
import ca.qc.bdeb.hexknight.model.TuileJeu;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Éric Wenaas <eric.wenaas@bdeb.qc.ca>
 * @version 0.1
 */

public class DessinateurTuileNoire implements DessinateurElementTuile {

    @Override
    public void dessinerElement(Graphics2D g2d, TuileJeu tuile, FormeAbstraite forme) {
        if (! tuile.estRevelee()) {
            TypeTuile type = TypeTuile.VIDE;
            BufferedImage image = RepertoireImages.getImageTuiles(type);
            int hauteur = (int) forme.getBounds().height;
            int largeur = (int) forme.getBounds().width;
            g2d.drawImage(image, forme.getX(), forme.getY(), largeur, hauteur, null);
        }
    }
}
