package ca.qc.bdeb.hexknight.ai;

import ca.qc.bdeb.hexknight.model.EtatAttente;
import ca.qc.bdeb.hexknight.model.EtatRonde;

/**
 *
 * @author ewenaas
 */
class ActionAttente extends ActionAI {

    public ActionAttente(JoueurArtificiel ai) {
        super(ai);
    }

    @Override
    void appliquerAction(EtatRonde etat, ObjectifAI objectif) {
        getAI().getAnalyse().loggerCartes();
        getAI().getAnalyse().evaluerMain();
        getAI().getAnalyse().genererPermutations();
        getAI().getAnalyse().calculerDestinations();
        getAI().getAnalyse().analyserObjectifs(false);
    }
}