/*
 * Copyright (C) 2015 Éric Wenaas
 *
 */
package ca.qc.bdeb.hexknight.model;

import ca.qc.bdeb.hexknight.commun.IDEtat;
import ca.qc.bdeb.hexknight.commun.Message;

/**
 *
 * @author Eric Wenaas <eric.wenaas@bdeb.qc.ca @version 0.1
 */
public class EtatDefense extends EtatRonde {

    private static final long serialVersionUID = 2L;

    private final TuileJeu tuileCombat;
    private int nombreBlessures;

    public EtatDefense(Joueur unJoueur, TuileJeu tuileCombat) {
        super(unJoueur, IDEtat.DEFENSE);
        this.tuileCombat = tuileCombat;
    }

    @Override
    public void executer() {
        Joueur joueurActif = getJoueur();
        int attaqueMonstre = tuileCombat.getMonstre().getAttaque();
        nombreBlessures = (int) Math.ceil((double) attaqueMonstre / joueurActif.getNiveauArmure());
        if (getValeurAccumulee() < attaqueMonstre) {
            retournerToutesLesCartes();
            for (int i = 0; i < nombreBlessures; i++) {
                joueurActif.ajouterCarteMain(CarteJeu.construireCarteJeu(TypeCarte.BLESSURE));
            }
        } else {
            retournerCartesInutilisees(attaqueMonstre);
        }
        viderZoneJeu();
        super.executer();
    }

    @Override
    protected Message getMessage() {
        int attaqueMonstre = tuileCombat.getMonstre().getAttaque();
        Message message = Message.VIDE;
        if (getValeurAccumulee() < attaqueMonstre && peutDefendre(attaqueMonstre)) {
            Joueur joueurActif = getJoueur();
            int blessures = (int) Math.ceil((double) attaqueMonstre / joueurActif.getNiveauArmure());
            message = Message.BLESSURES;
            message.setValeur(blessures);
        }
        return message;
    }
    
    @Override
    protected boolean pretAChanger() {
        return false;
    }


    @Override
    protected EtatRonde obtenirProchainEtat() {
        EtatRonde prochainEtat;
        prochainEtat = new EtatAttaque(getJoueur(), tuileCombat);
        return prochainEtat;
    }

    @Override
    protected boolean etatPermetCarte(CarteJeu carte) {
        return carte.getDefense() > 0;
    }

    @Override
    protected void actionJouerCarte(CarteJeu carte) {
        if (!getPartie().carteDansOffre(carte)) {
            int valeur = getValeurAccumulee();
            valeur += carte.getDefense();
            setValeurAccumulee(valeur);
            getJoueur().retirerCarteMain(carte);
            ajouterCarteEnJeu(carte);
        }
    }

    @Override
    protected void actionRetirerCarte(CarteJeu carte) {
        if (!getPartie().carteDansOffre(carte)) {
            int valeur = getValeurAccumulee();
            valeur -= carte.getDefense();
            setValeurAccumulee(valeur);
            retirerCarteEnJeu(carte);
            getJoueur().ajouterCarteMain(carte);
        }
    }

    private boolean peutDefendre(int attaqueMonstre) {
        int defense = getValeurAccumulee();
        for (CarteJeu carte : getJoueur().getMain()) {
            defense += carte.getDefense();
        }
        return defense >= attaqueMonstre;
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
    private void discarterCartes(int coutDefense) {
        int sommeAccumulee = 0;
        while (sommeAccumulee < coutDefense) {
            CarteJeu carte = getCartesEnJeu().get(0);
            sommeAccumulee += carte.getDefense();
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
        // Puisque que c'est le joueur qui se défend... on retourne l'attaque du monstre
        return tuileCombat.getMonstre().getAttaque();
    }

}
