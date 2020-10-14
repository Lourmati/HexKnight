/*
 * Copyright (C) 2015 Éric Wenaas
 *
 */
package ca.qc.bdeb.hexknight.ui;

import ca.qc.bdeb.graphics.Sprite;
import ca.qc.bdeb.graphics.UtilitairesGraphique;
import ca.qc.bdeb.hexknight.commun.CouleurCarte;
import ca.qc.bdeb.hexknight.model.CarteJeu;
import ca.qc.bdeb.hexknight.model.Partie;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * Classe qui représente visuellement une carte.
 *
 * @author Éric Wenaas <eric.wenaas@bdeb.qc.ca>
 */
public class SpriteCarteJeu extends Sprite {

    private final Partie modele;
    private final CarteJeu carte;
    private static final int OFFSET_X = 3;
    private static final int OFFSET_Y = 5;
    private static final int ESPACEMENT_Y = 12;

    public SpriteCarteJeu(BufferedImage image, CarteJeu carte, Partie modele) {
        super(image);
        this.carte = carte;
        this.modele = modele;
        this.addMouseListener(new EcouteurClick());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if (!carte.getCouleur().equals(CouleurCarte.BLESSURE)) {
            ajouterTexte(g2d);
        }
    }

    private void ajouterTexte(Graphics2D g2d) {
        String titre = carte.getTexte();
        String tabTitre[] = titre.split(" ");
        ajouterTitre(g2d, tabTitre);
        ajouterAttaque(g2d);
        ajouterDefense(g2d);
        ajouterVitesse(g2d);
        ajouterMeditation(g2d);
    }

    private void ajouterTitre(Graphics2D g2d, String[] mots) {
        Rectangle boundsTexte = new Rectangle(OFFSET_X, OFFSET_Y, getWidth() - 2 * OFFSET_X, getHeight() / 6);
        g2d.setColor(Color.BLACK);
        for (String mot : mots) {
            UtilitairesGraphique.centrerTexte(g2d, mot,
                    PolicesInterface.policePetite, boundsTexte);
            boundsTexte.setBounds(3, boundsTexte.y += 10, getWidth() - 6, getHeight() / 6);
        }
    }

    private void ajouterAttaque(Graphics2D g2d) {
        Rectangle bornes = obtenirBorneTexte("Attaque");
        if (carte.getAttaque() > 0) {
            String attaque = "Attaque " + Integer.toString(carte.getAttaque());
            g2d.setColor(Color.BLACK);
            UtilitairesGraphique.centrerTexte(g2d, attaque,
                    PolicesInterface.policePetite, bornes);
        }
    }

    private void ajouterDefense(Graphics2D g2d) {
        Rectangle bornes = obtenirBorneTexte("Défense");
        if (carte.getDefense() > 0) {
            String attaque = "Défense " + Integer.toString(carte.getDefense());
            g2d.setColor(Color.BLACK);
            UtilitairesGraphique.centrerTexte(g2d, attaque,
                    PolicesInterface.policePetite, bornes);
        }
    }

    private void ajouterVitesse(Graphics2D g2d) {
        Rectangle bornes = obtenirBorneTexte("Vitesse");
        if (carte.getVitesse() > 0) {
            String attaque = "Vitesse " + Integer.toString(carte.getVitesse());
            g2d.setColor(Color.BLACK);
            UtilitairesGraphique.centrerTexte(g2d, attaque,
                    PolicesInterface.policePetite, bornes);
        }
    }

    private void ajouterMeditation(Graphics2D g2d) {
        Rectangle bornes = obtenirBorneTexte("Méditation");
        if (carte.getMeditation() > 0) {
            String attaque = "Méditation " + Integer.toString(carte.getMeditation());
            g2d.setColor(Color.BLACK);
            UtilitairesGraphique.centrerTexte(g2d, attaque,
                    PolicesInterface.policePetite, bornes);
        }
    }

    private Rectangle obtenirBorneTexte(String descriptionPouvoir) {
        int facteurEspacement = 0;
        switch (descriptionPouvoir) {
            case "Attaque":
                facteurEspacement = 0;
                break;
            case "Défense":
                facteurEspacement = 1;
                break;
            case "Vitesse":
                facteurEspacement = 2;
                break;
            case "Méditation":
                facteurEspacement = 3;
                break;
        }
        return new Rectangle(OFFSET_X, (int) (getHeight() / 2.5) + facteurEspacement * ESPACEMENT_Y,
                getWidth() - 2 * OFFSET_X, getHeight() / 8);

    }

    private class EcouteurClick extends MouseAdapter {

        EcouteurClick() {

        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (! modele.getJoueurActif().estJoueurAutomatise()) {
                modele.selectionnerCarte(carte);
                if (modele.estPretAChanger()) {
                    modele.prochainePhase();
                }
            }
        }
    }

}
