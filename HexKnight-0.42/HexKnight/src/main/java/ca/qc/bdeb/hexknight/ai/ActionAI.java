package ca.qc.bdeb.hexknight.ai;

import ca.qc.bdeb.hexknight.model.EtatRonde;

/**
 *
 * @author ewenaas
 */
abstract class ActionAI {
    
    private JoueurArtificiel ai;
    
    ActionAI(JoueurArtificiel ai) {
        this.ai = ai;
    }
    
    final JoueurArtificiel getAI() {
        return ai;
    }

    abstract void appliquerAction(EtatRonde etat, ObjectifAI ai);
    
}
