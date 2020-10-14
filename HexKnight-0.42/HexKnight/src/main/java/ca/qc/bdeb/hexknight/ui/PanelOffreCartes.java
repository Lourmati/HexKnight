/*
 * Copyright (C) 2015 Éric Wenaas
 *
 */
package ca.qc.bdeb.hexknight.ui;

import ca.qc.bdeb.hexknight.model.Partie;
import ca.qc.bdeb.hexknight.model.CarteJeu;
import ca.qc.bdeb.hexknight.commun.Observateur;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Éric Wenaas
 */
public class PanelOffreCartes extends JPanel implements Observateur {
    
    private JLabel labelTitre;
    private Partie partie;

    public PanelOffreCartes(Rectangle bounds) {
        super();
        setSize(bounds.width, bounds.height);
        setLocation(bounds.x, bounds.y);
        setBackground(CouleursInterface.ZONE_INFO);
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 15));
        initializeComponents();
    }

    private void initializeComponents() {
        labelTitre = new JLabel("OFFRE");
        add(labelTitre);
        labelTitre.setBackground(CouleursInterface.ZONE_INFO);
        labelTitre.setForeground(Color.WHITE);
    }
    
    void setPartie(Partie unePartie) {
        if (partie != null) {
            partie.retirerObservateur(this);
        }
        partie = unePartie;
        unePartie.ajouterObservateur(this);
    }

    @Override
    public void changementEtat() {
        for (Component c : getComponents()) {
            remove(c);
        }
        add(labelTitre);
        List<CarteJeu> cartes = partie.getOffre();
        for (int indice = cartes.size() - 1; indice>=0; indice--) {
            CarteJeu carte = cartes.get(indice);
            BufferedImage image = RepertoireImages.getImageCarte(carte.getCouleur());
            SpriteCarteJeu spcarte = new SpriteCarteJeu(image, carte, partie);
            spcarte.setPreferredSize(new Dimension(70, 100));
            add(spcarte);
        }
        revalidate();
    }

    @Override
    public void changementEtat(Enum<?> property, Object o) {
    }
}
