    /*
 * Copyright (C) 2015 Éric Wenaas
 *
 */
package ca.qc.bdeb.hexknight.ui;

import ca.qc.bdeb.graphics.UtilitairesGraphique;
import ca.qc.bdeb.hexknight.model.GestionnairePartie;
import ca.qc.bdeb.hexknight.model.Logs;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.logging.Level;

/**
 * Classe contenant la fonction main. La classe ne fait qu'initialiser la fenêtre principale.
 *
 * @author Eric Wenaas <eric.wenaas@bdeb.qc.ca>
 * @version 0.40
 */
public class HexKnight {

    /**
     * Point d'entrée du programme.
     *
     * @param args aucun argument n'est présentement traité.
     */
    public static void main (String args[]) {
        Logs.initializeLoggers();
        Logs.STATE_LOGGER.setLevel(Level.INFO);
        Logs.ACTION_LOGGER.setLevel(Level.INFO);
        Logs.AI_LOGGER.setLevel(Level.INFO);
        Dimension bounds = UtilitairesGraphique.determinerEspaceDisponible();
        FenetrePrincipale fenetre = null;
        fenetre = new FenetrePrincipale(new GestionnairePartie(), new Rectangle(bounds));
        fenetre.setVisible(true);
    }
}
