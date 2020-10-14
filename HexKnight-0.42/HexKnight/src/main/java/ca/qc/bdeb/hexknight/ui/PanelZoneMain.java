/*
 * Copyright (C) 2015 Éric Wenaas
 *
 */
package ca.qc.bdeb.hexknight.ui;

import ca.qc.bdeb.graphics.Sprite;
import ca.qc.bdeb.hexknight.model.Partie;
import ca.qc.bdeb.hexknight.model.CarteJeu;
import ca.qc.bdeb.hexknight.model.Joueur;
import ca.qc.bdeb.hexknight.commun.Observateur;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 * Classe qui contient la main du joueur. Contient également un représentation du paquet de cartes ainsi
 * que les cartes qui ont été défaussées.
 * 
 * @author Éric Wenaas
 * @version 0.40
 */
public class PanelZoneMain extends JPanel implements Observateur {

    private Joueur joueurObserve;
    private Partie partie;

    public PanelZoneMain() {
        super();
        joueurObserve = null;
    }

    public void setJoueur(Joueur joueur) {
        if (joueurObserve != null) {
            joueurObserve.retirerObservateur(this);
        }
        this.joueurObserve = joueur;
        joueurObserve.ajouterObservateur(this);
    }
    
    void setPartie(Partie unePartie) {
        this.partie  = unePartie;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    public void changementEtat() {
        for (Component comp : getComponents()) {
            remove(comp);
        }
        
        if (joueurObserve == null) {
            return;
        }
        // TODO: Faire en sorte qu'on ne voit pas la main du joueur artificiel.
        
        Dimension dimensionImagesCartes = new Dimension(70, 100);
        
        // Ce point se déplace afin de placer les cartes aux bons endroits.
        Point positionCarteActuelle = new Point(10, (getHeight() - dimensionImagesCartes.height) / 2);
        ajouterImagePaquet(positionCarteActuelle, dimensionImagesCartes);
        
        positionCarteActuelle.translate(dimensionImagesCartes.width + 10, 0);
        ajouterCartesMain(positionCarteActuelle, dimensionImagesCartes);
        
        positionCarteActuelle.setLocation(getWidth() - 110, positionCarteActuelle.y);
        ajouterImageDefausse(positionCarteActuelle, dimensionImagesCartes);
        
        repaint();
    }

    private void ajouterImagePaquet(Point p, Dimension d) {
        if (joueurObserve.peutPiger()) {
            BufferedImage image = RepertoireImages.getImagePaquet();
            Sprite sp = new SpritePaquetJoueur(image, joueurObserve);
            sp.setLocation(p);
            sp.setSize(d);
            add(sp);
        }        
    }    

    private void ajouterCartesMain(Point p, Dimension d) {
        Point pointCarte = new Point(p);
        for (CarteJeu carte : joueurObserve.getMain()) {
            BufferedImage image = RepertoireImages.getImageCarte(carte.getCouleur());
            Sprite sp = new SpriteCarteJeu(image, carte, partie);
            sp.setLocation(pointCarte);
            sp.setSize(d);
            add(sp);
            pointCarte.translate(d.width + 10, 0);
        }
    }

    private void ajouterImageDefausse(Point p, Dimension d) {
        BufferedImage image = RepertoireImages.getImageDefausse();
        Sprite sp = new SpriteDefausseJoueur(image, joueurObserve);
        sp.setLocation(p);
        sp.setSize(d);
        add(sp);
    }

    @Override
    public void changementEtat(Enum<?> property, Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
