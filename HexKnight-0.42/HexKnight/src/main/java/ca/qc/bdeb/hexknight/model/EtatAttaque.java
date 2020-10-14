/*
 * Copyright (C) 2015 Éric Wenaas
 *
 */
package ca.qc.bdeb.hexknight.model;

import ca.qc.bdeb.hexknight.commun.IDEtat;
import ca.qc.bdeb.hexknight.commun.Evenement;
import ca.qc.bdeb.hexknight.commun.Message;
import ca.qc.bdeb.hexknight.commun.TypeAjoutTuile;

/**
 * L'état dans lequel le joueur est mis quand il attaque un monstre. Cet état
 * arrive après l'état défense. Le joueur doit accumuler une force d'attaque
 * supérieure ou égale à la défense du monstre.
 * 
 * @author Eric Wenaas <eric.wenaas@bdeb.qc.ca>
 * @version 0.40
 */
public class EtatAttaque extends EtatRonde {

    private static final long serialVersionUID = 2L;
    private final TuileJeu tuileCombat;

    public EtatAttaque(Joueur unJoueur, TuileJeu tuileCombat) {
        super(unJoueur, IDEtat.ATTAQUE);
        this.tuileCombat = tuileCombat;
    }
    
    @Override
    protected void initialiser() {
        super.initialiser();
    }

    @Override
    protected void executer() {
        Joueur joueurActif = getPartie().getJoueurActif();
        Monstre monstre = tuileCombat.getMonstre();
        int defenseMonstre = monstre.getDefense();
        if (getValeurAccumulee() >= defenseMonstre) {
            int experience = monstre.getExperience();
            int niveauAvant = joueurActif.getNiveauExperience();
            joueurActif.ajouterExperience(experience);
            int niveauApres = joueurActif.getNiveauExperience();
            if (niveauApres > niveauAvant) {
                getPartie().aviserObservateurs(Evenement.NIVEAU, joueurActif);
            }
            tuileCombat.setMonstre(null);
            retournerCartesInutilisees(defenseMonstre);
            viderZoneJeu();
            if (tuileCombat.getAjout().equals(TypeAjoutTuile.RUINES)) {
                getJoueur().incrementerNombreRuines();
            } else if (tuileCombat.getAjout().equals(TypeAjoutTuile.CHATEAU)) {
                getJoueur().setPosition(tuileCombat.getPosition());
                getPartie().getGestionnaireGrille().appliquerVisibiliteGrille(getPartie(), tuileCombat.getPosition());
            }
        } else if (!peutBattre(defenseMonstre)) {
            retournerToutesLesCartes();
        }
        super.executer();
    }

    @Override
    protected Message getMessage() {
        Message message = Message.VIDE;
        int defenseMonstre = tuileCombat.getMonstre().getDefense();
        if (getValeurAccumulee() < defenseMonstre && peutBattre(defenseMonstre)) {
            message = Message.VICTOIRE_POSSIBLE;
        }
        return message;
    }

    @Override
    protected boolean pretAChanger() {
        return false;
    }

    @Override
    protected EtatRonde obtenirProchainEtat() {
        EtatRonde nouvelEtat;
        if (getJoueur().doitChoisirOffre()) {
            nouvelEtat = new EtatSelectionCarte(getJoueur());
        } else if (getJoueur().reposPossible()) {
            nouvelEtat = new EtatRepos(getJoueur());
        } else {
            nouvelEtat = new EtatAttente(getJoueur());
        }
        return nouvelEtat;
    }

    @Override
    protected boolean etatPermetCarte(CarteJeu carte) {
        return carte.getAttaque() > 0;
    }

    @Override
    protected void actionJouerCarte(CarteJeu carte) {
        if (!getPartie().carteDansOffre(carte)) {
            int valeur = getValeurAccumulee();
            valeur += carte.getAttaque();
            setValeurAccumulee(valeur);
            getJoueur().retirerCarteMain(carte);
            ajouterCarteEnJeu(carte);
        }
    }

    @Override
    protected void actionRetirerCarte(CarteJeu carte) {
        if (!getPartie().carteDansOffre(carte)) {
            int valeur = getValeurAccumulee();
            valeur -= carte.getAttaque();
            setValeurAccumulee(valeur);
            retirerCarteEnJeu(carte);
            getJoueur().ajouterCarteMain(carte);
        }
    }

    private boolean peutBattre(int defenseMonstre) {
        int attaque = getValeurAccumulee();
        for (CarteJeu carte : getPartie().getJoueurActif().getMain()) {
            attaque += carte.getAttaque();
        }
        return attaque >= defenseMonstre;
    }

    private void retournerCartesInutilisees(int attaqueMonstre) {
        Partie partie = getPartie();
        Joueur joueurActif = partie.getJoueurActif();
        discarterCartes(attaqueMonstre);
        for (CarteJeu carte : getCartesEnJeu()) {
            retirerCarteEnJeu(carte);
            joueurActif.ajouterCarteMain(carte);
        }
    }

    // On enlève les cartes dans l'ordre où elles ont été ajoutées.
    private void discarterCartes(int defenseMonstre) {
        int sommeAccumulee = 0;
        while (sommeAccumulee < defenseMonstre) {
            CarteJeu carte = getCartesEnJeu().get(0);
            sommeAccumulee += carte.getAttaque();
            retirerCarteEnJeu(carte);
            getPartie().getJoueurActif().discarter(carte);
        }
    }

    private void retournerToutesLesCartes() {
        Joueur joueurActif = getJoueur();
        for (CarteJeu carte : getCartesEnJeu()) {
            retirerCarteEnJeu(carte);
            joueurActif.ajouterCarteMain(carte);
        }
    }

    public TuileJeu getTuileCombat() {
        return tuileCombat;
    }
    
    @Override
    Integer getValeurCible() {
        // Puisque c'est le joueur qui attaque, on retourne la défense du monstre
        return tuileCombat.getMonstre().getDefense();
    }
}
