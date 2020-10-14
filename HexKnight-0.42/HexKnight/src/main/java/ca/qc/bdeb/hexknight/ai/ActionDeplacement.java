package ca.qc.bdeb.hexknight.ai;

import ca.qc.bdeb.hexknight.commun.MomentJour;
import ca.qc.bdeb.hexknight.model.AnalyseGrille;
import ca.qc.bdeb.hexknight.model.CarteJeu;
import ca.qc.bdeb.hexknight.model.EtatRonde;
import ca.qc.bdeb.hexknight.model.EvaluateurDeplacement;
import ca.qc.bdeb.hexknight.model.EvaluateurDeplacementJour;
import ca.qc.bdeb.hexknight.model.EvaluateurDeplacementNuit;
import ca.qc.bdeb.hexknight.model.Logs;
import ca.qc.bdeb.hexknight.model.TuileJeu;
import ca.qc.bdeb.tableaujeu.PositionGrille;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author ewenaas
 */
class ActionDeplacement extends ActionAI {

    public ActionDeplacement(JoueurArtificiel ai) {
        super(ai);
    }

    @Override
    void appliquerAction(EtatRonde etat, ObjectifAI objectif) {
        TuileJeu destination = objectif.tuileDestination;
        List<CarteJeu> cartesAJouer;
//        int cout = getCoutDeplacement(destination);
        while (!getAI().getPosition().equals(objectif.tuileDestination.getPosition())) {
            cartesAJouer = objectif.meilleurePermutation;
            StringBuilder builder = new StringBuilder();
            builder.append("Destination: ");
            builder.append(destination.getPosition().toString());
            builder.append(" Cout: ");
            builder.append(objectif.coutDeplacement);
            Logs.AI_LOGGER.info(builder.toString());

            EvaluateurDeplacement evaluateur;
            if (getAI().getPartie().getPhaseJour().equals(MomentJour.JOUR)) {
                evaluateur = new EvaluateurDeplacementJour(getAI().getPartie().getAdversaires(getAI()), objectif.coutDeplacement);
            } else {
                evaluateur = new EvaluateurDeplacementNuit(getAI().getPartie().getAdversaires(getAI()), objectif.coutDeplacement);
            }

            CarteJeu carte = cartesAJouer.remove(0);
            PositionGrille pos = calculerProchainePosition(objectif.tuileDestination.getPosition(), evaluateur);
            getAI().getPartie().selectionnerCarte(carte);
            getAI().getPartie().selectionnerTuile(pos);

            // On réévalue le tout car on peut avoir découvert de nouvelles tuiles.
            getAI().getAnalyse().evaluerMain();
            getAI().getAnalyse().genererPermutations();
            if (!getAI().getAnalyse().permutations.isEmpty()) {
                getAI().getAnalyse().calculerDestinations();
                getAI().getAnalyse().analyserObjectifs(true);
                objectif = getAI().getAnalyse().getObjectifRetenu();
                destination = objectif.tuileDestination;

            }
        }
    }

    private PositionGrille calculerProchainePosition(PositionGrille destination, EvaluateurDeplacement evaluateur) {
        PositionGrille depart = getAI().getPosition();
        AnalyseGrille analyse = getAI().getPartie().getGestionnaireGrille().appliquerDijkstra(depart, evaluateur);
        PositionGrille cible = destination;
        HashMap<PositionGrille, PositionGrille> precedent = analyse.precedent;
        while (!precedent.get(cible).equals(depart)) {
            cible = precedent.get(cible);
        }
        return cible;
    }
}
