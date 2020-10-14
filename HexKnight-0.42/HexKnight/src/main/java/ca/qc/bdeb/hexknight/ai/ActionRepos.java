package ca.qc.bdeb.hexknight.ai;

import ca.qc.bdeb.hexknight.model.CarteJeu;
import ca.qc.bdeb.hexknight.model.EtatRonde;
import ca.qc.bdeb.hexknight.model.Logs;
import ca.qc.bdeb.hexknight.model.TuileJeu;
import ca.qc.bdeb.hexknight.model.TypeCarte;
import ca.qc.bdeb.tableaujeu.DirectionHexagonale;
import ca.qc.bdeb.tableaujeu.PositionGrille;
import ca.qc.bdeb.tableaujeu.TableauHexagonal;
import java.util.List;

/**
 *
 * @author ewenaas
 */
class ActionRepos extends ActionAI {

    public ActionRepos(JoueurArtificiel ai) {
        super(ai);
    }

    @Override
    void appliquerAction(EtatRonde etat, ObjectifAI objectif) {
        List<CarteJeu> cartesAJouer = objectif.meilleurePermutation;
        int cout = objectif.guerison;
        StringBuilder builder = new StringBuilder();
        builder.append("Guerison: ");
        builder.append(cout);
        Logs.AI_LOGGER.info(builder.toString());
        cout -= etat.getValeurAccumulee();
        while (cout > 0) {
            CarteJeu carte = cartesAJouer.remove(0);
            cout -= carte.getMeditation();
            getAI().getPartie().selectionnerCarte(carte);
        }
        int nombreBlessuresGueries = objectif.guerison;
        for (int i = 0; i < nombreBlessuresGueries; i++) {
            boolean trouve = false;
            List<CarteJeu> cartes = getAI().getMain();
            int nombreCartes = cartes.size();
            for (int indiceBlessure = 0; indiceBlessure < nombreCartes && !trouve; indiceBlessure++) {
                CarteJeu carte = cartes.get(indiceBlessure);
                if (carte.getType().equals(TypeCarte.BLESSURE)) {
                    getAI().getPartie().selectionnerCarte(carte);
                    trouve = true;
                }
            }
        }

        if (aucunChangement(objectif, getAI().getFaitAction())) {
            discarterCartes();
        }
    }

    private boolean aucunChangement(ObjectifAI objectif, boolean actionPosee) {
        boolean reponse;
        TuileJeu tuileDestination = objectif.tuileDestination;
        TuileJeu tuileDepart = objectif.tuileDepart;
        //Si on a pas bougé, rien tué et rien guérit... on a rien fait
        reponse = tuileDestination == tuileDepart && objectif.tuileCombat == null && objectif.guerison == 0 && !actionPosee;
        return reponse;
    }
    
    // Fonction qui discarte des cartes dans la phase repos si rien n'a été fait.
    // Si on est adjacent à un monstre, on discarte les cartes qui ont 1 d'attaque
    // Sinon, on discarte les cartes qui ont 1 de mouvement... devra être amélioré
    private void discarterCartes() {
        TableauHexagonal<TuileJeu> grille = getAI().getPartie().getGrille();
        boolean monstreAdjacent  = false;
        PositionGrille pos = getAI().getPosition();
        for (DirectionHexagonale dir : DirectionHexagonale.values()) {
            TuileJeu tuileAdjacente = grille.getAdjacentElement(pos, dir);
            if (tuileAdjacente.contientMonstre())  {
                monstreAdjacent = true;
                break;
            }
        }
        List<CarteJeu> main = getAI().getMain();
        int nombreDiscartees = 0;
        int nombreMax = getAI().getTaillePaquet();
        for (CarteJeu carte : main) {
            if (carte.getType().equals(TypeCarte.BLESSURE)) {
                continue;
            } else if (monstreAdjacent && carte.getAttaque() < 2) {
                nombreDiscartees++;
                getAI().getPartie().selectionnerCarte(carte);
            } else if (! monstreAdjacent && carte.getVitesse() < 2) {
                nombreDiscartees++;
                getAI().getPartie().selectionnerCarte(carte);
            }
            if (nombreDiscartees == nombreMax) {
                break;
            }
        }
        int indice = 0;
        while (nombreDiscartees == 0) {
            CarteJeu carte = main.get(indice); 
            if (! carte.getType().equals(TypeCarte.BLESSURE)) {
                getAI().getPartie().selectionnerCarte(carte);
                nombreDiscartees++;
            }
            // Ne peut pas y avoir que des blessures, sinon on est pas ici.
            indice++;
        }
    }

}
