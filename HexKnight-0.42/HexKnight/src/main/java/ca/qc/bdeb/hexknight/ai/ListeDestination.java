/*
 * 
 * Copyright (C) 2015 Éric Wenaas
 * 
 */
package ca.qc.bdeb.hexknight.ai;

import ca.qc.bdeb.tableaujeu.TableauHexagonal;
import ca.qc.bdeb.tableaujeu.PositionGrille;
import ca.qc.bdeb.hexknight.commun.MomentJour;
import ca.qc.bdeb.hexknight.model.CarteJeu;
import ca.qc.bdeb.hexknight.model.EvaluateurDeplacementJour;
import ca.qc.bdeb.hexknight.model.EvaluateurDeplacementNormal;
import ca.qc.bdeb.hexknight.model.EvaluateurDeplacementNuit;
import ca.qc.bdeb.hexknight.model.GestionnaireGrille;
import ca.qc.bdeb.hexknight.model.Partie;
import ca.qc.bdeb.hexknight.model.TuileJeu;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

class ListeDestination implements Iterable<TuileJeu> {

    private JoueurArtificiel joueur;
    private HashMap<TuileJeu, Integer> coutDeplacement;
    private List<TuileJeu> tuilesAccessibles;

    ListeDestination(JoueurArtificiel joueur) {
        this.joueur = joueur;
        coutDeplacement = new HashMap<>();
        tuilesAccessibles = new ArrayList<>();
        trouverDestinations();
    }

    private void trouverDestinations() {
        int maxMouvement = calculerMaximumMouvement();
        Partie partie = joueur.getPartie();
        TableauHexagonal<TuileJeu> carteHex = partie.getGrille();
        GestionnaireGrille gestionnaire = partie.getGestionnaireGrille();
        boolean jour = joueur.getPartie().getPhaseJour().equals(MomentJour.JOUR);
        EvaluateurDeplacementNormal evaluateur = null;
        if (jour) {
            evaluateur = new EvaluateurDeplacementJour(joueur.getPartie().getAdversaires(joueur), maxMouvement);
        } else {
            evaluateur = new EvaluateurDeplacementNuit(joueur.getPartie().getAdversaires(joueur), maxMouvement);
        }

        HashMap<PositionGrille, Integer> map = gestionnaire.appliquerDijkstra(joueur.getPosition(), evaluateur).destinations;

        for (PositionGrille pos : map.keySet()) {
            Integer cout = map.get(pos);
            TuileJeu tuile = carteHex.getElementAt(pos);

            if (tuile.estRevelee()) {
                tuilesAccessibles.add(tuile);
                coutDeplacement.put(tuile, cout);
            }
        }
    }

    private int calculerMaximumMouvement() {
        List<CarteJeu> main = joueur.getMain();
        int maximum = 0;
        for (CarteJeu carte : main) {
            maximum += carte.getVitesse();
        }
        return maximum;
    }

    @Override
    public Iterator<TuileJeu> iterator() {
        return tuilesAccessibles.iterator();
    }

    int getCoutDeplacement(TuileJeu tuile) {
        return coutDeplacement.get(tuile);
    }
}
