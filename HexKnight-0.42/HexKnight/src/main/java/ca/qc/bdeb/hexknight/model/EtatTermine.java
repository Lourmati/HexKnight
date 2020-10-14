/*
 * 
 * Copyright (C) 2015 Éric Wenaas
 * 
 */
package ca.qc.bdeb.hexknight.model;

import ca.qc.bdeb.hexknight.commun.IDEtat;
import ca.qc.bdeb.hexknight.commun.Message;

/**
 *
 * @author Éric Wenaas
 */
public class EtatTermine extends EtatRonde {

    public EtatTermine(Joueur joueurActif) {
        super(joueurActif, IDEtat.TERMINE);
    }

    @Override
    protected boolean pretAChanger() {
        return false;
    }
    
    @Override
    protected boolean etatPermetCarte(CarteJeu carte) {
        return false;
    }

    @Override
    protected void actionJouerCarte(CarteJeu carte) {
    }

    @Override
    protected void actionRetirerCarte(CarteJeu carte) {
    }

    @Override
    protected EtatRonde obtenirProchainEtat() {
        return this;
    }
    
    @Override
    public Message getMessage() {
        return Message.PARTIE_TERMINEE;
    }
}
