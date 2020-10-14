/*
 * Copyright (C) 2015 Éric Wenaas
 *
 */
package ca.qc.bdeb.hexknight.model;

import ca.qc.bdeb.hexknight.commun.IDEtat;
import ca.qc.bdeb.tableaujeu.PositionGrille;
import ca.qc.bdeb.hexknight.commun.CouleurCarte;
import ca.qc.bdeb.hexknight.commun.Message;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Éric Wenaas
 */
public abstract class EtatRonde implements Serializable {

    private static final long serialVersionUID = 2L;
    private final Joueur joueur;
    private final Partie partie;
    private final IDEtat idEtat;
    private int valeurAccumulee;
    private List<CarteJeu> cartesEnJeu;

    protected EtatRonde(Joueur joueurActif, IDEtat id) {
        this.joueur = joueurActif;
        this.idEtat = id;
        partie = joueur.getPartie();
        valeurAccumulee = 0;
        cartesEnJeu = new LinkedList<>();
    }

    protected final Joueur getJoueur() {
        return joueur;
    }

    protected final Partie getPartie() {
        return partie;
    }

    protected void initialiser() {
        Logs.STATE_LOGGER.info(String.format("%s %s(%s)", getJoueur().getPersonnage(), getClass().getSimpleName(), "initialiser"));
    }

    protected void executer() {
        Logs.STATE_LOGGER.info(String.format("%s %s(%s)", getJoueur().getPersonnage(), getClass().getSimpleName(), "executer"));
    }

    protected abstract boolean etatPermetCarte(CarteJeu carte);

    protected abstract void actionJouerCarte(CarteJeu carte);

    protected abstract void actionRetirerCarte(CarteJeu carte);
        
    public List<CarteJeu> getCartesEnJeu() {
        List<CarteJeu> cartes = new LinkedList<>();
        for (CarteJeu carte : cartesEnJeu) {
            cartes.add(carte);
        }
        return cartes;
    }
    
    final void ajouterCarteEnJeu(CarteJeu carte) {
        cartesEnJeu.add(carte);
    }

    final void retirerCarteEnJeu(CarteJeu carte) {
        cartesEnJeu.remove(carte);
    }

    final void viderZoneCarteEnJeu(){
        // TODO: Ne semble pas logique du tout car les cartes ne sont plus dans
        // la main du joueur.
//        for (CarteJeu carte : cartesEnJeu) {
//            joueur.retirerCarteMain(carte);
//        }
        cartesEnJeu.clear();
    }
    
    void viderZoneJeu() {
        for (CarteJeu carte : cartesEnJeu) {
            if (!carte.getCouleur().equals(CouleurCarte.BLESSURE)) {
                joueur.discarter(carte);
            }
        }
        cartesEnJeu.clear();
    }



    HashMap<PositionGrille, Integer> actionCalculerDestinations() {
        return new HashMap<>();
    }

    void actionSelectionnerTuile(PositionGrille pos) {
//        throw new IllegalStateException("tuileSelectionee dans etat " + getNom());
    }

    protected abstract EtatRonde obtenirProchainEtat();

    final void setValeurAccumulee(int nouvelleValeur) {
        valeurAccumulee = nouvelleValeur;
    }

    public final int getValeurAccumulee() {
        return valeurAccumulee;
    }

    public final IDEtat getID() {
        return idEtat;
    }
    
    public final String getNom() {
        return idEtat.getNom();
    }

    protected abstract boolean pretAChanger();

    protected Message getMessage() {
        return Message.VIDE;
    }

    boolean peutRetirerCarteEnJeu(CarteJeu carte) {
        return true;
    }

    Integer getValeurCible() {
        return 0;
    }
}
