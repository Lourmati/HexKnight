/*
 * 
 * Copyright (C) 2016 Éric Wenaas
 * 
 */

package ca.qc.bdeb.hexknight.ui;

import ca.qc.bdeb.graphics.FormeAbstraite;
import ca.qc.bdeb.hexknight.model.TuileJeu;
import java.awt.Graphics2D;

/**
 *
 * @author Éric Wenaas <eric.wenaas@bdeb.qc.ca>
 * @version 0.1
 */
interface DessinateurElementTuile {
    public void dessinerElement(Graphics2D g2d, TuileJeu tuile, FormeAbstraite forme);
}