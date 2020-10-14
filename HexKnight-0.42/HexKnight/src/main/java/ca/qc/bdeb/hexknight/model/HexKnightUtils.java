/*
 * 
 * Copyright (C) 2015 Éric Wenaas
 * 
 */

package ca.qc.bdeb.hexknight.model;

import ca.qc.bdeb.tableaujeu.TableauHexagonal;
import ca.qc.bdeb.tableaujeu.DirectionHexagonale;
import ca.qc.bdeb.tableaujeu.PositionGrille;
import ca.qc.bdeb.hexknight.commun.MomentJour;
import ca.qc.bdeb.hexknight.commun.TypeAjoutTuile;
import java.util.Iterator;

import java.util.List;

/**
 *
 * @author Eric Wenaas <eric.wenaas@bdeb.qc.ca
 * @version 0.1
 */

public class HexKnightUtils {
    
    public static int compterBlessures(List<CarteJeu> cartes) {
        int nombre = 0;
        for (CarteJeu carte: cartes) {
            if (carte.getType().equals(TypeCarte.BLESSURE)) {
                nombre++;
            }
        }
        return nombre;
    }
    
    public static int compterBlessures(Iterator<CarteJeu> iter) {
        int nombre = 0;
        while (iter.hasNext()) {
            CarteJeu carte = iter.next();
            if (carte.getType().equals(TypeCarte.BLESSURE)) {
                nombre++;
            }
        }
        return nombre;
    }

    public static void revelerMonstreChateauAdjacent(Partie partie) {
        if (partie.getPhaseJour().equals(MomentJour.NUIT)) {
            return;
        }
        
        List<Joueur> joueurs = partie.getJoueurs();
        TableauHexagonal<TuileJeu> carte  = partie.getGrille();
        for (Joueur joueur : joueurs) {
            PositionGrille pos  = joueur.getPosition();
            for (DirectionHexagonale d : DirectionHexagonale.values()) {
                TuileJeu adj = carte.getAdjacentElement(pos, d);
                if (adj.contientMonstre() && adj.getAjout().equals(TypeAjoutTuile.CHATEAU)) {
                    adj.getMonstre().revelerMonstre();
                }
            }
        }
    }
}
