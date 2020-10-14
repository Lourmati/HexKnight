/*
 * 
 * Copyright (C) 2016 Éric Wenaas
 * 
 */

package ca.qc.bdeb.hexknight.model;

import java.util.List;

/**
 *
 * @author Éric Wenaas <eric.wenaas@bdeb.qc.ca>
 * @version 0.1
 */

public class EvaluateurDeplacementJour extends EvaluateurDeplacementNormal {
    
    public EvaluateurDeplacementJour(List<Joueur> adversaires, int deplacementMaximal) {
        super(adversaires, deplacementMaximal);
    }

    @Override
    public int obtenirCout(TuileJeu destination) {
 
        return destination.getMouvementJour();
    }
}
