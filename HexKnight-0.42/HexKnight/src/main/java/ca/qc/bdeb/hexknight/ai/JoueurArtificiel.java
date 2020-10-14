/*
 * 
 * Copyright (C) 2015 Éric Wenaas
 * 
 */

package ca.qc.bdeb.hexknight.ai;

import ca.qc.bdeb.hexknight.commun.Personnage;
import ca.qc.bdeb.hexknight.model.EtatAttente;
import ca.qc.bdeb.hexknight.model.Joueur;
import ca.qc.bdeb.hexknight.model.Partie;


/**
 *
 * @author Eric Wenaas <eric.wenaas@bdeb.qc.ca
 * @version 0.1
 */

public class JoueurArtificiel extends Joueur {
    
    private Analyse analyse;
    
    public JoueurArtificiel(Partie partie, Personnage personnage) {
        super(partie, personnage);
        setState(new EtatAttente(this));
        analyse = new Analyse(this);
    }
    
    @Override
    public boolean estJoueurAutomatise() {
        return true;
    }

    public Analyse getAnalyse() {
        return analyse;
    }
}
