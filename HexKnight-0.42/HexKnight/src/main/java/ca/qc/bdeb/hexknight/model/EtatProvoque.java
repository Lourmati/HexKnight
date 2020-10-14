/**
 * Copyright (C) 2015 Eric Wenaas
 */
package ca.qc.bdeb.hexknight.model;

import ca.qc.bdeb.hexknight.commun.IDEtat;
import ca.qc.bdeb.tableaujeu.PositionGrille;

/**
 *
 * @author Eric Wenaas
 */
public class EtatProvoque extends EtatRonde {

    private static final long serialVersionUID = 1L;
    private boolean monstreAttaque;
    private TuileJeu tuile;

    public EtatProvoque(Joueur unJoueur) {
        super(unJoueur, IDEtat.PROVOQUE);
        monstreAttaque = false;
        tuile = null;
    }
     
    @Override
    protected boolean pretAChanger() {
        return monstreAttaque;
    }
    
    @Override
    public void executer() {
        super.executer();
    }

    @Override
    protected EtatRonde obtenirProchainEtat() {
        EtatRonde nouvelEtat;
        if (monstreAttaque) {
            getJoueur().setFaitAction(true);
            nouvelEtat =  new EtatDefense(getJoueur(), tuile);
        } else if (getJoueur().reposPossible()) {
            nouvelEtat = new EtatRepos(getJoueur()); 
        } else {
            nouvelEtat = new EtatAttente(getJoueur());
        }
        return nouvelEtat;
    }

    @Override
    protected boolean etatPermetCarte(CarteJeu carte) {
        return false;
    }

    @Override
    protected void actionJouerCarte(CarteJeu carte) {
        throw new IllegalStateException();
    }

    @Override
    protected void actionRetirerCarte(CarteJeu carte) {
        throw new IllegalStateException();
    }

    @Override
    void actionSelectionnerTuile(PositionGrille pos) {
        Partie partie = getPartie();
        tuile = partie.getGrille().getElementAt(pos);
        if (tuile.contientMonstre()) {
            monstreAttaque = true;
            tuile.getMonstre().revelerMonstre();
        }
    }
}
