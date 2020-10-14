/*
 * 
 * Copyright (C) 2016 Éric Wenaas
 * 
 */

package ca.qc.bdeb.hexknight.ui;

import ca.qc.bdeb.tableaujeu.PositionGrille;
import ca.qc.bdeb.graphics.FormeAbstraite;
import ca.qc.bdeb.graphics.UtilitairesGraphique;
import ca.qc.bdeb.hexknight.model.Partie;
import ca.qc.bdeb.hexknight.model.TuileJeu;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;

/**
 *
 * @author Éric Wenaas <eric.wenaas@bdeb.qc.ca>
 * @version 0.1
 */

public class DessinateurDistances implements DessinateurElementTuile {

    Partie modelePartie;
    
    public DessinateurDistances(Partie modelePartie) {
        this.modelePartie = modelePartie;
    }
    
    @Override
    public void dessinerElement(Graphics2D g2d, TuileJeu tuile, FormeAbstraite forme) {
        HashMap<PositionGrille, Integer> destinations = modelePartie.getDestinations();

        if (destinations.containsKey(tuile.getPosition())) {
            int distance = destinations.get(tuile.getPosition());
            if (distance != 0) {
                String chaine = Integer.toString(distance);
                g2d.setColor(Color.BLUE);
                UtilitairesGraphique.centrerTexte(g2d, chaine, PolicesInterface.policeGrande, forme.getBounds());
            }
        }
    }
}
