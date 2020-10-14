/*
 * Copyright (C) 2015 Éric Wenaas
 *
 */
package ca.qc.bdeb.hexknight.model;

import ca.qc.bdeb.hexknight.commun.IDEtat;
import ca.qc.bdeb.hexknight.commun.Evenement;
import ca.qc.bdeb.hexknight.commun.Message;

/**
 *
 * @author Éric Wenaas
 * @version 0.10
 */
public class EtatSelectionCarte extends EtatRonde {

    private boolean carteSelectionnee;

    public EtatSelectionCarte(Joueur unJoueur) {
        super(unJoueur, IDEtat.SELECTION);
        carteSelectionnee = false;
    }

    @Override
    protected void executer() {
        super.executer();
     }
    
    @Override
    protected Message getMessage() {
        Message message = Message.VIDE;
        if (! carteSelectionnee) {
            message = Message.SELECTION_REQUISE;
        }
        
        return message;
    }

    @Override
    protected EtatRonde obtenirProchainEtat() {
        EtatRonde nouvelEtat;
        if (carteSelectionnee) {
            if (getJoueur().reposPossible()) {
                nouvelEtat = new EtatRepos(getJoueur()); 
            } else {
                nouvelEtat = new EtatAttente(getJoueur());
            }        
        } else {
            nouvelEtat = this;
        }
        return nouvelEtat;
    }
    
    @Override
    protected boolean pretAChanger() {
        return carteSelectionnee;
    }

    @Override
    protected boolean etatPermetCarte(CarteJeu carte) {
        return getPartie().carteDansOffre(carte) && !carteSelectionnee;
    }

    @Override
    protected void actionJouerCarte(CarteJeu carte) {
        Partie partie = getPartie();
        if (partie.carteDansOffre(carte) && !carteSelectionnee) {
            Joueur joueurActif = getJoueur();
            joueurActif.ajouterCarteSommetPaquet(carte);
            joueurActif.setSelectionOffreRequise(false);
            partie.retirerCarteOffre(carte);
            carteSelectionnee = true;
        }
    }

    @Override
    protected void actionRetirerCarte(CarteJeu carte) {
        throw new IllegalStateException();
    }
}
