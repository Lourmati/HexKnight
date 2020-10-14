/*
 * 
 * Copyright (C) 2015 Éric Wenaas
 * 
 */
package ca.qc.bdeb.hexknight.model;

import ca.qc.bdeb.tableaujeu.TableauHexagonal;
import ca.qc.bdeb.tableaujeu.DirectionHexagonale;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Éric Wenaas
 */
public abstract class EvaluateurDeplacementNormal implements EvaluateurDeplacement {

    private final List<Joueur> adversaires;
    private final int deplacementMaximal;

    public EvaluateurDeplacementNormal(List<Joueur> adversaires, int deplacementMaximal) {
        this.adversaires = adversaires;
        this.deplacementMaximal = deplacementMaximal;
    }

    @Override
    public boolean peutAller(TableauHexagonal<TuileJeu> carte, TuileJeu depart, TuileJeu destination, int coutDeplacementTotal) {
        return coutDeplacementTotal <= deplacementMaximal && deplacementPossible(carte, depart, destination, adversaires);
    }

    @Override
    public abstract int obtenirCout(TuileJeu destination);

    private boolean deplacementPossible(TableauHexagonal<TuileJeu> carte, TuileJeu tuileActuelle, TuileJeu tuileVoisine, List<Joueur> joueurs) {

        if (tuileActuelle.contientMonstre() || tuileVoisine.contientMonstre()) {
            return false;
        }

        List<TuileJeu> monstresAdjacents = trouverMonstresAdjacents(carte, tuileActuelle);
        List<TuileJeu> monstresAdjacentsVoisins = trouverMonstresAdjacents(carte, tuileVoisine);

        monstresAdjacents.retainAll(monstresAdjacentsVoisins);

        // Un monstre est adjacent aux deux tuiles, on ne peut pas passer.
        if (!monstresAdjacents.isEmpty()) {
            return false;
        }

        for (Joueur joueur : joueurs) {
            if (tuileVoisine.getPosition().equals(joueur.getPosition())) {
                return false;
            }
        }
        // On ne peut aller sur une tuile occupée par un autre joueur.

        return true;
    }

    private List<TuileJeu> trouverMonstresAdjacents(TableauHexagonal<TuileJeu> carte, TuileJeu actuelle) {
        List<TuileJeu> tuiles = new LinkedList<>();
        for (DirectionHexagonale dir : DirectionHexagonale.values()) {
            TuileJeu adjacente = carte.getAdjacentElement(actuelle, dir);
            if (adjacente.contientMonstre()) {
                tuiles.add(adjacente);
            }
        }
        return tuiles;
    }

}
