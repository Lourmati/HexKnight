/*
 * Copyright (C) 2015 Éric Wenaas
 *
 */

package ca.qc.bdeb.hexknight.ui;

import ca.qc.bdeb.tableaujeu.TableauHexagonal;
import ca.qc.bdeb.tableaujeu.PanelTableau;
import ca.qc.bdeb.tableaujeu.DessinateurTuile;
import ca.qc.bdeb.hexknight.model.TuileJeu;

/**
 *
 * @author Eric Wenaas <eric.wenaas@bdeb.qc.ca
 * @version 0.1
 */

public class PanelGrilleJeu extends PanelTableau<TuileJeu>{
    public PanelGrilleJeu(TableauHexagonal<TuileJeu> carte, DessinateurTuile dessinateur, int[] taillesTuiles, int indiceTailleInitiale) {
        super(carte, dessinateur, taillesTuiles, indiceTailleInitiale);
        setBackground(CouleursInterface.FOND_GRILLE);
    }
}