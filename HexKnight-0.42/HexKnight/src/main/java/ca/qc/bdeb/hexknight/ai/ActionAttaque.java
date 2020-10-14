package ca.qc.bdeb.hexknight.ai;

import ca.qc.bdeb.hexknight.model.CarteJeu;
import ca.qc.bdeb.hexknight.model.EtatAttaque;
import ca.qc.bdeb.hexknight.model.EtatRonde;
import ca.qc.bdeb.hexknight.model.Logs;
import java.util.List;

/**
 *
 * @author ewenaas
 */
class ActionAttaque extends ActionAI {

    public ActionAttaque(JoueurArtificiel ai) {
        super(ai);
    }

    @Override
    void appliquerAction(EtatRonde etat, ObjectifAI objectif) {
        List<CarteJeu> cartesAJouer = objectif.meilleurePermutation;
        int cout = objectif.attaque;
        StringBuilder builder = new StringBuilder();
        builder.append("Attaque: ");
        builder.append(cout);
        Logs.AI_LOGGER.info(builder.toString());
        while (cout > 0) {
            CarteJeu carte = cartesAJouer.remove(0);
            cout -= carte.getAttaque();
            getAI().getPartie().selectionnerCarte(carte);
        }
    }
}
