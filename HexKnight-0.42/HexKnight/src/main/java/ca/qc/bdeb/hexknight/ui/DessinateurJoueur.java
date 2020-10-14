/*
 * 
 * Copyright (C) 2016 Éric Wenaas
 * 
 */

package ca.qc.bdeb.hexknight.ui;

import ca.qc.bdeb.graphics.FormeAbstraite;
import ca.qc.bdeb.hexknight.model.Joueur;
import ca.qc.bdeb.hexknight.model.Partie;
import ca.qc.bdeb.hexknight.model.TuileJeu;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Éric Wenaas <eric.wenaas@bdeb.qc.ca>
 * @version 0.1
 */

public class DessinateurJoueur implements DessinateurElementTuile {
    
    private Partie modelePartie;
    
    DessinateurJoueur(Partie modelePartie) {
        this.modelePartie = modelePartie;
    }

    @Override
    public void dessinerElement(Graphics2D g2d, TuileJeu tuile, FormeAbstraite forme) {
        int hauteur = (int) forme.getBounds().height;
        int largeur = (int) forme.getBounds().width;
        Joueur joueurActif = modelePartie.getJoueurActif();

        // On commence par afficher le joueur actif
        if (tuile.getPosition().equals(joueurActif.getPosition())) {
            BufferedImage image = RepertoireImages.getImageJoueur(joueurActif.getPersonnage());
            g2d.drawImage(image, forme.getX(), forme.getY(), largeur, hauteur, null);
        }

        // On affiche les autres joueurs
        for (Joueur unJoueur : modelePartie.getJoueurs()) {
            if (!unJoueur.getPosition().equals(joueurActif.getPosition())
                    && unJoueur.getPosition().equals(tuile.getPosition())) {
                BufferedImage image = RepertoireImages.getImageJoueur(unJoueur.getPersonnage());
                g2d.drawImage(image, forme.getX(), forme.getY(), largeur, hauteur, null);
            }
        }
    }
}
