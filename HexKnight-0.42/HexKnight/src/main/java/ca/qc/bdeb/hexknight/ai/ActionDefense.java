package ca.qc.bdeb.hexknight.ai;

import ca.qc.bdeb.hexknight.model.CarteJeu;
import ca.qc.bdeb.hexknight.model.EtatRonde;
import ca.qc.bdeb.hexknight.model.Logs;
import java.util.List;

/**
 *
 * @author ewenaas
 */
class ActionDefense extends ActionAI {

    public ActionDefense(JoueurArtificiel ai) {
        super(ai);
    }

    @Override
    void appliquerAction(EtatRonde etat, ObjectifAI objectif) {
        List<CarteJeu> cartesAJouer = objectif.meilleurePermutation;
        int cout = objectif.defense;
        StringBuilder builder = new StringBuilder();
        builder.append("Defense: ");
        builder.append(objectif.defense);
        Logs.AI_LOGGER.info(builder.toString());
        while (cout > 0) {
            CarteJeu carte = cartesAJouer.remove(0);
            cout -= carte.getDefense();
            getAI().getPartie().selectionnerCarte(carte);
        }
    }
}
