/*
 * 
 * Copyright (C) 2016 Éric Wenaas
 * 
 */

package ca.qc.bdeb.hexknight.model;

import ca.qc.bdeb.tableaujeu.TableauHexagonal;

/**
 *
 * @author Éric Wenaas <eric.wenaas@bdeb.qc.ca>
 * @version 0.1
 */

public class EvaluateurVisibilite implements EvaluateurDeplacement {

    @Override
    public boolean peutAller(TableauHexagonal<TuileJeu> carte, TuileJeu depart, TuileJeu destination, int coutDeplacementTotal) {
        return true;
    }

    @Override
    public int obtenirCout(TuileJeu destination) {
        return destination.getHauteur();
    }

}
