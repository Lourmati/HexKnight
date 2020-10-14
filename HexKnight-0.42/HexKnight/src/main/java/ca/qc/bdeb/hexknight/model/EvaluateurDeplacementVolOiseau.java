/*
 * 
 * Copyright (C) 2015 Éric Wenaas
 * 
 */
package ca.qc.bdeb.hexknight.model;

import ca.qc.bdeb.tableaujeu.TableauHexagonal;
import ca.qc.bdeb.hexknight.commun.TypeTuile;

/**
 *
 * @author Éric Wenaas
 */
public class EvaluateurDeplacementVolOiseau implements EvaluateurDeplacement {

    @Override
    public boolean peutAller(TableauHexagonal<TuileJeu> carte, TuileJeu depart, TuileJeu destination, int coutDeplacementTotal) {
        return ! destination.getType().equals(TypeTuile.COTE);
    }

    @Override
    public int obtenirCout(TuileJeu destination) {
        return 1;
    }
    
}
