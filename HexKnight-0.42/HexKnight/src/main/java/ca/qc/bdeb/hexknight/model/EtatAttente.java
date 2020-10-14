/*
 * Copyright (C) 2015 Éric Wenaas
 *
 */
package ca.qc.bdeb.hexknight.model;

import ca.qc.bdeb.hexknight.commun.IDEtat;
import ca.qc.bdeb.hexknight.commun.Message;
import ca.qc.bdeb.tableaujeu.TableauHexagonal;
import ca.qc.bdeb.tableaujeu.PositionGrille;
import ca.qc.bdeb.hexknight.commun.TypeAjoutTuile;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Éric Wenaas <eric.wenaas@bdeb.qc.ca>
 * @version 0.1
 */
public class EtatAttente extends EtatRonde {

    private static final long serialVersionUID = 1L;

    public EtatAttente(Joueur unJoueur) {
        super(unJoueur, IDEtat.ATTENTE);
    }

    @Override
    protected void initialiser() {
        super.initialiser();
        ajouterBlessureTombe();
        pigerCartes();
        getJoueur().setActif(false);
        getJoueur().setFaitAction(false);
    }
    
    private void ajouterBlessureTombe() {
        PositionGrille positionJoueur = getJoueur().getPosition();
        GestionnaireGrille gest = getPartie().getGestionnaireGrille();
        TypeAjoutTuile ajout = gest.getCarte().getElementAt(positionJoueur).getAjout();
        if (ajout.equals(TypeAjoutTuile.TOMBEAU)) {
            getJoueur().ajouterCarteSommetPaquet(CarteJeu.construireCarteJeu(TypeCarte.BLESSURE));
        }
    }

    @Override
    protected void executer() {
        super.executer();
        getJoueur().setActif(true);        
    }

    @Override
    protected Message getMessage() {
        return Message.VIDE;
    }
    
    @Override
    protected boolean pretAChanger() {
        return true;
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
    protected EtatRonde obtenirProchainEtat() {
        EtatRonde etat;
        TableauHexagonal<TuileJeu> carte = getPartie().getGrille();
        TypeAjoutTuile ajout = carte.getElementAt(getJoueur().getPosition()).getAjout();
        
        if (ajout.equals(TypeAjoutTuile.CERCLE_MAGIQUE)) {
            etat = new EtatEchange(getJoueur());
        } else if (preparationEstPosible()) {
            etat = new EtatPreparation(getJoueur());
        } else {
            etat = new EtatDeplacement(getJoueur());
        }
        return etat;
    }

    private void pigerCartes() {
        Joueur joueurActif = getJoueur();
        TableauHexagonal<TuileJeu> carte = getPartie().getGrille();
        PositionGrille posJoueur = joueurActif.getPosition();
        TuileJeu tuile = carte.getElementAt(posJoueur);
        int tailleMain = joueurActif.getTailleMain();
        tailleMain += tuile.obtenirAjoutMain();

        if (joueurActif.getTaillePaquet() == 0) {
            getPartie().declarerDerniereRonde();
        }

        while (joueurActif.getNombreCartesEnMain() < tailleMain && joueurActif.getTaillePaquet() > 0) {
            joueurActif.pigerDansMain();
        }
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
