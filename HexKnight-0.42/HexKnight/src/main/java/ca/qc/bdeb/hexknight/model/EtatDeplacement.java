/*
 * Copyright (C) 2015 Éric Wenaas
 *
 */
package ca.qc.bdeb.hexknight.model;

import ca.qc.bdeb.hexknight.commun.IDEtat;
import ca.qc.bdeb.hexknight.commun.Message;
import ca.qc.bdeb.tableaujeu.TableauHexagonal;
import ca.qc.bdeb.tableaujeu.DirectionHexagonale;
import ca.qc.bdeb.tableaujeu.PositionGrille;
import ca.qc.bdeb.hexknight.commun.MomentJour;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Éric Wenaas
 */
public class EtatDeplacement extends EtatRonde {

    private static final long serialVersionUID = 1L;
    HashMap<PositionGrille, Integer> tuilesDisponibles;
    private int valeurConservee;   // Pour corriger un bogue quand il reste des points de mouvements à la fin.

    public EtatDeplacement(Joueur unJoueur) {
        super(unJoueur, IDEtat.DEPLACEMENT);
        tuilesDisponibles = new HashMap<>();
        valeurConservee = 0;
    }

    @Override
    protected Message getMessage() {
        Message message = Message.VIDE;
        if (getValeurAccumulee() > 0 && peutBouger()) {
            message = Message.PERTE_MOUVEMENT;
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
        Joueur joueurActif = getPartie().getJoueurActif();
        PositionGrille position = joueurActif.getPosition();
        TuileJeu tuile = getPartie().getGrille().getElementAt(position);
        if (monstreExterieurAdjacent(tuile)) {
            nouvelEtat = new EtatProvoque(getJoueur());
        } else if (getJoueur().reposPossible()) {
            nouvelEtat = new EtatRepos(getJoueur());
        } else {
            nouvelEtat = new EtatAttente(getJoueur());
        }
        viderZoneJeu();
        return nouvelEtat;
    }

    @Override
    protected boolean etatPermetCarte(CarteJeu carte) {
        return carte.getVitesse() > 0;
    }

    @Override
    protected void actionJouerCarte(CarteJeu carte) {
        if (!getPartie().carteDansOffre(carte)) {
            int valeur = getValeurAccumulee();
            valeur += carte.getVitesse();
            setValeurAccumulee(valeur);
            reinitialiserTuilesAccessibles();
            getJoueur().retirerCarteMain(carte);
            ajouterCarteEnJeu(carte);
        }
    }

    private void reinitialiserTuilesAccessibles() {
        GestionnaireGrille gestionnaireCarte = getPartie().getGestionnaireGrille();
        Joueur joueurActif = getPartie().getJoueurActif();
        int valeur = getValeurAccumulee();
        MomentJour phase = getPartie().getPhaseJour();
        EvaluateurDeplacement evaluateur;
        if (phase.equals(MomentJour.JOUR)) {
            evaluateur = new EvaluateurDeplacementJour(getPartie().getAdversaires(joueurActif), valeur);
        } else {
            evaluateur = new EvaluateurDeplacementNuit(getPartie().getAdversaires(joueurActif), valeur);            
        }
        tuilesDisponibles = gestionnaireCarte.appliquerDijkstra(joueurActif.getPosition(), evaluateur).destinations;
    }

    @Override
    protected void actionRetirerCarte(CarteJeu carte) {
        if (!getPartie().carteDansOffre(carte)) {
            int valeur = getValeurAccumulee();
            valeur -= carte.getVitesse();
            setValeurAccumulee(valeur);
            getJoueur().ajouterCarteMain(carte);
            retirerCarteEnJeu(carte);
            reinitialiserTuilesAccessibles();
        }
    }

    private boolean monstreExterieurAdjacent(TuileJeu tuile) {
        TableauHexagonal<TuileJeu> carte = getPartie().getGrille();
        LinkedList<Monstre> list = new LinkedList<>();
        for (DirectionHexagonale d : DirectionHexagonale.values()) {
            TuileJeu adj = carte.getAdjacentElement(tuile.getPosition(), d);
            if (adj.contientMonstre()) {
                list.add(adj.getMonstre());
            }
        }

        return !list.isEmpty();
    }

    @Override
    HashMap<PositionGrille, Integer> actionCalculerDestinations() {
        return tuilesDisponibles;
    }

    @Override
    void actionSelectionnerTuile(PositionGrille pos) {
        Joueur joueurActif = getPartie().getJoueurActif();
        if (tuilesDisponibles.containsKey(pos) && !pos.equals(joueurActif.getPosition())) {
            Integer coutDeplacement = tuilesDisponibles.get(pos);
            if (coutDeplacement <= getValeurAccumulee()) {
                setValeurAccumulee(getValeurAccumulee() - coutDeplacement);
                joueurActif.setPosition(pos);
                reinitialiserTuilesAccessibles();
                getPartie().getGestionnaireGrille().appliquerVisibiliteGrille(getPartie(), pos);
                HexKnightUtils.revelerMonstreChateauAdjacent(getPartie());
                if (getValeurAccumulee() == 0) {
                    viderZoneJeu();
                } else {
                    discarterCartes(coutDeplacement);
                }
                ajusterValeurConservee();
            }
            joueurActif.setFaitAction(true);
        }

    }
    
    void ajusterValeurConservee() {
        int valeurZoneJeu = 0;
        for (CarteJeu carte : getCartesEnJeu()) {
            valeurZoneJeu += carte.getVitesse();
        }
        valeurConservee = getValeurAccumulee() - valeurZoneJeu;
    }

    // On enlève les cartes dans l'ordre où elles ont été ajoutées.
    private void discarterCartes(int coutDeplacement) {
        int sommeAccumulee = valeurConservee;
        while (sommeAccumulee < coutDeplacement) {
            CarteJeu carte = getCartesEnJeu().get(0);
            sommeAccumulee += carte.getVitesse();
            retirerCarteEnJeu(carte);
            getPartie().getJoueurActif().discarter(carte);
        }
    }

    private boolean peutBouger() {
        PositionGrille pos = getJoueur().getPosition();
        GestionnaireGrille gest = getPartie().getGestionnaireGrille();
        List<Joueur> joueurs = getPartie().getAdversaires(getJoueur());
        
        EvaluateurDeplacement evaluateur = null;
        if (getPartie().getPhaseJour().equals(MomentJour.JOUR)) {
            evaluateur = new EvaluateurDeplacementJour(joueurs, getValeurAccumulee());
        } else {
            evaluateur = new EvaluateurDeplacementNuit(joueurs, getValeurAccumulee());            
        }
        HashMap<PositionGrille, Integer> destinations = gest.appliquerDijkstra(pos, evaluateur).destinations;
        return destinations.size() > 1 && getCartesEnJeu().size() > 0;
    }
    

    @Override    
    boolean peutRetirerCarteEnJeu(CarteJeu carte) {
        int valeurActuelle = getValeurAccumulee();
        int valeurCarte = carte.getVitesse();
        return valeurActuelle - valeurCarte >= 0;
    }

}
