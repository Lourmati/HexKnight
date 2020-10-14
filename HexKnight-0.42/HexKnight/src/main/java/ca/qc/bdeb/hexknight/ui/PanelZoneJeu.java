/*
 * Copyright (C) 2015 Éric Wenaas
 *
 */
package ca.qc.bdeb.hexknight.ui;

import ca.qc.bdeb.graphics.UtilitairesGraphique;
import ca.qc.bdeb.hexknight.model.CarteJeu;
import ca.qc.bdeb.hexknight.model.Partie;
import ca.qc.bdeb.hexknight.commun.Observateur;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Ce panel contient les cartes qui ont été jouées par le joueur actif. Il y également un bouton pour
 * passer à la prochaine phase.
 * 
 * @author Éric Wenaas
 * @version 0.40
 */
public class PanelZoneJeu extends JPanel implements Observateur {

    private Partie modele;
    private JButton boutonProchainePhase;
    private Rectangle boundsEtat;
    private Rectangle boundsValeur;
    private Rectangle boundsBoutonConfirmer;

    public PanelZoneJeu(Rectangle bounds, AbstractAction action) {
        super();
        setSize(bounds.width, bounds.height);
        setLocation(bounds.x, bounds.y);
        setBackground(CouleursInterface.ZONE_JEU);
        setLayout(null);
        initializeComponents();
        boutonProchainePhase.setAction(action);
    }

    private void initializeComponents() {
        boundsEtat = new Rectangle(getWidth() - 175, 0, 175, getHeight() / 3);
        boundsValeur = new Rectangle(boundsEtat.x, (int) boundsEtat.getMaxY(),
                boundsEtat.width, boundsEtat.height);
        boundsBoutonConfirmer = new Rectangle(boundsValeur.x, (int) boundsValeur.getMaxY(),
                boundsValeur.width, boundsValeur.height);
        boutonProchainePhase = new JButton("Prochaine phase");
        boutonProchainePhase.setBounds(boundsBoutonConfirmer);
        add(boutonProchainePhase);
        boutonProchainePhase.setVisible(false);
    }
    
    public void setPartie(Partie modele) {
        if (this.modele != null) {
            this.modele.retirerObservateur(this);
        }
        this.modele = modele;
        this.modele.ajouterObservateur(this);
    }

    @Override
    public void changementEtat() {
        boutonProchainePhase.setEnabled(!modele.getJoueurActif().estJoueurAutomatise());
        List<CarteJeu> cartes = modele.getJoueurActif().getCartesEnJeu();
        for (Component c : getComponents()) {
            if (c instanceof SpriteCarteJeu) {
                remove(c);
            }
        }
        Dimension dimensionImages = new Dimension(70, 100);
        int x = 90;
        int y = (getHeight() - dimensionImages.height) / 2; // Centre sur la hauteur
        for (CarteJeu carte : cartes) {
            BufferedImage image = RepertoireImages.getImageCarte(carte.getCouleur());
            SpriteCarteJeu sp = new SpriteCarteJeu(image, carte, modele);
            sp.setLocation(x, y);
            sp.setSize(dimensionImages);
            add(sp);
            x += 80;
        }
        boutonProchainePhase.setVisible(true);
        repaint();
    }

    @Override
    public void changementEtat(Enum<?> property, Object o) {
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (modele != null) {
            Graphics2D g2d = (Graphics2D) g;
            String phase = modele.getEtatRound();
            UtilitairesGraphique.centrerTexte(g2d, phase,
                    PolicesInterface.policeNormale, boundsEtat);
            StringBuilder valeur = new StringBuilder();
            valeur.append(Integer.toString(modele.getValeurAccumulee()));
            if (phase.equals("Attaque") || phase.equals("Defense")) {
                valeur.append(" / ");
                valeur.append(modele.getValeurCible());
            }
            
            g2d.setColor(Color.black);
            UtilitairesGraphique.centrerTexte(g2d, valeur.toString(),
                    PolicesInterface.policeGrande, boundsValeur);
        }
    }
}
