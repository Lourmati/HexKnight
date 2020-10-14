package ca.qc.bdeb.hexknight.ai;

import ca.qc.bdeb.hexknight.model.CarteJeu;
import ca.qc.bdeb.hexknight.model.EtatRonde;

/**
 *
 * @author ewenaas
 */
class ActionSelection extends ActionAI {

    public ActionSelection(JoueurArtificiel ai) {
        super(ai);
    }

    @Override
    void appliquerAction(EtatRonde etat, ObjectifAI objectif) {
        CarteJeu carte = getAI().getPartie().getOffre().get(0);
        getAI().getPartie().selectionnerCarte(carte);
    }
}
