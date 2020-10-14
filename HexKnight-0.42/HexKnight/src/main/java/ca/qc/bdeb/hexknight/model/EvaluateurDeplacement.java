/*
 * 
 * Copyright (C) 2015 Éric Wenaas
 * 
 */
package ca.qc.bdeb.hexknight.model;

import ca.qc.bdeb.tableaujeu.TableauHexagonal;

/**
 *
 * @author Éric Wenaas
 */
public interface EvaluateurDeplacement {

    boolean peutAller(TableauHexagonal<TuileJeu> carte, TuileJeu depart, TuileJeu destination, int coutDeplacementTotal);

    int obtenirCout(TuileJeu destination);
}
