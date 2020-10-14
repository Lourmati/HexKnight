/*
 * 
 * Copyright (C) 2015 Éric Wenaas
 * 
 */
package ca.qc.bdeb.hexknight.model;

import ca.qc.bdeb.hexknight.commun.IDEtat;
import java.util.Iterator;

/**
 *
 * @author Éric Wenaas
 */
public class EtatEchange extends EtatRonde {

    EtatEchange(Joueur joueur) {
        super(joueur, IDEtat.ECHANGE);
    }

    @Override
    protected boolean etatPermetCarte(CarteJeu carte) {
        return getJoueur().getMain().contains(carte) && getCartesEnJeu().size() < 3;
    }

    @Override
    protected void actionJouerCarte(CarteJeu carte) {
            getJoueur().retirerCarteMain(carte);
            ajouterCarteEnJeu(carte);
    }


    @Override
    protected void actionRetirerCarte(CarteJeu carte) {
        retirerCarteEnJeu(carte);
        getJoueur().ajouterCarteMain(carte);
    }

    @Override
    protected void executer() {
        for (CarteJeu carte : getCartesEnJeu()) {
            getJoueur().getPaquet().ajouterDessous(carte);
            getJoueur().pigerDansMain();
        }
        viderZoneCarteEnJeu();
    }

    @Override
    protected EtatRonde obtenirProchainEtat() {
        EtatRonde etat;
        if (preparationEstPosible()) {
            etat = new EtatPreparation(getJoueur());
        } else {
            etat = new EtatDeplacement(getJoueur());
        }
        return etat;

    }

    @Override
    protected boolean pretAChanger() {
        return false;
    }


    private boolean preparationEstPosible() {
        Joueur joueurActif = getJoueur();
        Iterator<CarteJeu> iter = joueurActif.getMain().iterator();
        boolean pigerPossible = false;

        while (iter.hasNext() && !pigerPossible) {
            pigerPossible = iter.next().getMeditation() > 0 && joueurActif.getTaillePaquet() > 0;
        }
        return pigerPossible;
    }
}
