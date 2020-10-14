/*
 * Copyright (C) 2015 Éric Wenaas
 */
package ca.qc.bdeb.hexknight.model;

import ca.qc.bdeb.hexknight.commun.IDEtat;


/**
 *
 * @author Éric Wenaas
 */
public class EtatPreparation extends EtatRonde {

    private static final long serialVersionUID = 2L;
    public EtatPreparation(Joueur unJoueur) {
        super(unJoueur, IDEtat.PREPARATION);
    }
    

    @Override
    protected void executer() {
        super.executer();
        Joueur joueurActif = getJoueur();
        int valeur = getValeurAccumulee();
        int taillePaquet = joueurActif.getTaillePaquet();
        int nombreAPiger = valeur <= taillePaquet ? valeur : taillePaquet;
        for (int i = 0; i < nombreAPiger; i++) {
            joueurActif.pigerDansMain();
        }
        viderZoneJeu();
        setValeurAccumulee(0);
    }

    @Override
    protected EtatRonde obtenirProchainEtat() {
        return new EtatDeplacement(getJoueur());
    }
    
    @Override
    protected boolean pretAChanger() {
        return false;
    }

    @Override
    public boolean etatPermetCarte(CarteJeu carte) {
        return carte.getMeditation() >= 1;
    }

    @Override
    protected void actionRetirerCarte(CarteJeu carte) {
        if (! getPartie().carteDansOffre(carte)) {
            int valeurInitiale = getValeurAccumulee();
            valeurInitiale -= carte.getMeditation();
            setValeurAccumulee(valeurInitiale);
            getJoueur().ajouterCarteMain(carte);
            retirerCarteEnJeu(carte);
        }
    }

    @Override
    public void actionJouerCarte(CarteJeu carte) {
        if (! getPartie().carteDansOffre(carte)) {
            int valeurInitiale = getValeurAccumulee();
            valeurInitiale += carte.getMeditation();
            setValeurAccumulee(valeurInitiale);
            getJoueur().retirerCarteMain(carte);
            ajouterCarteEnJeu(carte);
        }
    }
}
