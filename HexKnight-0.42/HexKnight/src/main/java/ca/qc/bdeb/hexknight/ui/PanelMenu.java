/*
 * 
 * Copyright (C) 2018 Éric Wenaas
 * 
 */
package ca.qc.bdeb.hexknight.ui;

import java.awt.GridLayout;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Contient le menu. La classe n'a rien de particulier. Le menu est transparent.
 *
 * @author Eric Wenaas <eric.wenaas@bdeb.qc.ca>
 * @version 0.40
 */
public class PanelMenu extends JPanel {

    /**
     * Le constructeur. Rien de particulier à spécifier.
     *
     * @param largeur la largeur du menu
     * @param hauteur la hauteur du menu
     */
    PanelMenu (int largeur, int hauteur) {
        initialize(largeur, hauteur);
    }

    private void initialize(int largeur, int hauteur) {
        setSize(largeur, hauteur);
        setOpaque(false);
        setLayout(new GridLayout(7, 1, 5, 15));
    }

    /**
     * Permet d'ajouter une action au menu. Ce qui a pour effet de créer un bouton permettant de déclencher cette
     * action
     *
     * @param action l'action qui est ajoutée
     */
    void ajouterAction(AbstractAction action) {
        add(new JButton(action));
    }
}
