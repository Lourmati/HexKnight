/*
 * Copyright (C) 2015 Éric Wenaas
 */
package ca.qc.bdeb.hexknight.model;

import ca.qc.bdeb.hexknight.commun.Personnage;

/**
 *
 * @author Éric Wenaas <eric.wenaas@bdeb.qc.ca>
 * @version 0.40
 */
public class JoueurHumain extends Joueur {

    public JoueurHumain(Partie partie, Personnage personnage) {
        super(partie, personnage);
        setState(new EtatAttente(this));
    }

    @Override
    public boolean estJoueurAutomatise() {
        return false;
    }
}
