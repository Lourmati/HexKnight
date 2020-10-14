package ca.qc.bdeb.hexknight.ai;

import ca.qc.bdeb.hexknight.model.EtatPreparation;
import ca.qc.bdeb.hexknight.model.EtatRonde;
import ca.qc.bdeb.hexknight.model.Logs;

/**
 *
 * @author ewenaas
 */
class ActionPreparation extends ActionAI {

    public ActionPreparation(JoueurArtificiel ai) {
        super(ai);
    }

    @Override
    void appliquerAction(EtatRonde etat, ObjectifAI objectif) {
        Logs.AI_LOGGER.info("Valeur de l'objectif: " + objectif.valeurObjectif);
    }
}
