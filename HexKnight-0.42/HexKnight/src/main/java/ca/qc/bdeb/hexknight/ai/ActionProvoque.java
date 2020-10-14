package ca.qc.bdeb.hexknight.ai;

import ca.qc.bdeb.hexknight.model.EtatRonde;
import ca.qc.bdeb.hexknight.model.TuileJeu;
import ca.qc.bdeb.tableaujeu.PositionGrille;

/**
 *
 * @author ewenaas
 */
class ActionProvoque extends ActionAI {

    public ActionProvoque(JoueurArtificiel ai) {
        super(ai);
    }

    @Override
    void appliquerAction(EtatRonde etat, ObjectifAI objectif) {
        TuileJeu tuileCombat = objectif.tuileCombat; 
        if (tuileCombat != null) {
            PositionGrille pos = tuileCombat.getPosition();
            getAI().getPartie().selectionnerTuile(pos);
        }
    }
}
