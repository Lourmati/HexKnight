/*
 *
 * Copyright (C) 2015 Éric Wenaas
 *
 */
package ca.qc.bdeb.hexknight.model;

import ca.qc.bdeb.hexknight.commun.IDEtat;
import ca.qc.bdeb.tableaujeu.TableauHexagonal;
import ca.qc.bdeb.tableaujeu.PositionGrille;
import ca.qc.bdeb.hexknight.commun.TypeAjoutTuile;
import ca.qc.bdeb.hexknight.commun.CouleurCarte;
import ca.qc.bdeb.hexknight.commun.Message;

/**
 *
 * @author Éric Wenaas <eric.wenaas@bdeb.qc.ca @version 0.1
 */
public class EtatRepos extends EtatRonde {

    private static final long serialVersionUID = 1L;

    public EtatRepos(Joueur unJoueur) {
        super(unJoueur, IDEtat.REPOS);
    }

    @Override
    public void initialiser() {
        super.initialiser();
        Joueur joueurActif = getPartie().getJoueurActif();
        PositionGrille position = joueurActif.getPosition();
        TableauHexagonal<TuileJeu> carte = getPartie().getGrille();
        TypeAjoutTuile ajout = carte.getElementAt(position).getAjout();
        int valeur = 0;
        valeur = carte.getElementAt(position).obtenirAjoutGuerison();
        
        // Si le joueur n'a pas fait d'action significative, alors on ajoute un point de guérison. 
        if (! getJoueur().getFaitAction()) {
            valeur++;
        }
        setValeurAccumulee(valeur);
    }

    @Override
    public void executer() {
        // TODO: Patch si l'utilisateur essaie de tricher en remettant une carte de guérison dans sa main.
        while (getValeurAccumulee() < 0) {
            for (CarteJeu carte : getJoueur().getMain()) {
                if (carte.getMeditation() > 0) {
                    getJoueur().retirerCarteMain(carte);
                    getJoueur().discarter(carte);
                    setValeurAccumulee(getValeurAccumulee() + carte.getMeditation());
                }
            }
        }
        viderZoneJeu();
        super.executer();
    }

    @Override
    public Message getMessage() {
        Message message = Message.VIDE; 
        if (peutGuerir(getValeurAccumulee())) {
            message = Message.PERTE_GUERISON;
        }
        return message;
    }
    
    @Override
    protected boolean pretAChanger() {
        return false;
    }
    
    @Override
    protected EtatRonde obtenirProchainEtat() {
        EtatRonde nouvelEtat = new EtatAttente(getJoueur());
        return nouvelEtat;
    }

    @Override
    protected boolean etatPermetCarte(CarteJeu carte) {
        boolean permis;

        if (getValeurAccumulee() > 0) {
            permis = true;
        } else {
            permis = !carte.getCouleur().equals(CouleurCarte.BLESSURE);
        }
        return permis;
    }

    @Override
    protected void actionJouerCarte(CarteJeu carte) {
        if (!getPartie().carteDansOffre(carte)) {
            int valeur = getPartie().getValeurAccumulee();
            if (carte.getCouleur().equals(CouleurCarte.BLESSURE) && valeur > 0) {
                valeur -= 1;
            } else {
                valeur += carte.getMeditation();
            }
            getJoueur().retirerCarteMain(carte);
            ajouterCarteEnJeu(carte);
            setValeurAccumulee(valeur);
        }
    }

    @Override
    protected void actionRetirerCarte(CarteJeu carte) {
        if (!getPartie().carteDansOffre(carte)) {
            int valeur = getPartie().getValeurAccumulee();
            if (carte.getCouleur().equals(CouleurCarte.BLESSURE)) {
                valeur += 1;
            } else {
                valeur -= carte.getMeditation();
            }
            getJoueur().ajouterCarteMain(carte);
            retirerCarteEnJeu(carte);
            setValeurAccumulee(valeur);
        }
    }
    
    private boolean peutGuerir(int valeur) {
        boolean blessureEnMain = HexKnightUtils.compterBlessures(getJoueur().getMain()) > 0;
        return blessureEnMain && valeur > 0;
    }
    
    @Override
    boolean peutRetirerCarteEnJeu(CarteJeu carte) {
        boolean possible = true;
        int prochaineValeur = getValeurAccumulee() - carte.getMeditation();
        if (carte.getMeditation() > 0 && prochaineValeur < 0) {
            possible = false;
        }
        return possible;
    }
}
