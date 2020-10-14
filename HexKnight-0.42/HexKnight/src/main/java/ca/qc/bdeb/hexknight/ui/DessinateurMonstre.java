/*
 * 
 * Copyright (C) 2016 Éric Wenaas
 * 
 */
package ca.qc.bdeb.hexknight.ui;

import ca.qc.bdeb.graphics.FormeAbstraite;
import ca.qc.bdeb.graphics.UtilitairesGraphique;
import ca.qc.bdeb.hexknight.model.Monstre;
import ca.qc.bdeb.hexknight.model.TuileJeu;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 *
 * @author Éric Wenaas <eric.wenaas@bdeb.qc.ca>
 * @version 0.1
 */
public class DessinateurMonstre implements DessinateurElementTuile {

    @Override
    public void dessinerElement(Graphics2D g2d, TuileJeu tuile, FormeAbstraite forme) {
        Monstre monstre = tuile.getMonstre();
        int hauteur = (int) forme.getBounds().height;
        int largeur = (int) forme.getBounds().width;
        if (tuile.contientMonstre() && monstre.estRevele()) {
            BufferedImage image = RepertoireImages.getImageMonstre(monstre.getType());
            g2d.drawImage(image, forme.getX(), forme.getY(), largeur, hauteur, null);
            afficherStatsMonstre(g2d, forme, monstre);
        } else if (tuile.contientMonstre()) {
            BufferedImage image = RepertoireImages.getImageMonstre(Monstre.Type.INCONNU);
            g2d.drawImage(image, forme.getX(), forme.getY(), largeur, hauteur, null);
        }
    }

    private void afficherStatsMonstre(Graphics2D g2d, FormeAbstraite forme, Monstre monstre) {
        afficherAttaque(g2d, monstre, forme.getBounds());
        afficherDefense(g2d, monstre, forme.getBounds());
        afficherExperience(g2d, monstre, forme.getBounds());
    }

    private void afficherAttaque(Graphics2D g2d, Monstre monstre, Rectangle boundsForme) {
        int x = boundsForme.x;
        int y = boundsForme.y;
        int width = (int) (boundsForme.width * 0.6);
        int height = boundsForme.height;
        Rectangle zoneAttaque = new Rectangle(x - 6, y, width, height);
        String attaque = Integer.toString(monstre.getAttaque());
        afficherCercleStats(g2d, attaque, PolicesInterface.policeStatsMontres, zoneAttaque);
        g2d.setColor(Color.RED);
        UtilitairesGraphique.centrerTexte(g2d, attaque, PolicesInterface.policeStatsMontres, zoneAttaque);
    }

    private void afficherDefense(Graphics2D g2d, Monstre monstre, Rectangle boundsForme) {
        int x = (int) (boundsForme.getMaxX() - boundsForme.width * 0.6);
        int y = boundsForme.y;
        int width = (int) (boundsForme.width * 0.6);
        int height = boundsForme.height;
        Rectangle zoneDefense = new Rectangle(x + 6, y, width, height);
        String defense = Integer.toString(monstre.getDefense());
        afficherCercleStats(g2d, defense, PolicesInterface.policeStatsMontres, zoneDefense);
        g2d.setColor(Color.BLUE);
        UtilitairesGraphique.centrerTexte(g2d, defense, PolicesInterface.policeStatsMontres, zoneDefense);

    }

    private void afficherExperience(Graphics2D g2d, Monstre monstre, Rectangle boundsForme) {
        int x = boundsForme.x;
        int y = (int) (boundsForme.getMaxY() - boundsForme.height * 0.5);
        int width = boundsForme.width;
        int height = boundsForme.height / 2;
        Rectangle zoneExperience = new Rectangle(x, y + 6, width, height);
        String experience = Integer.toString(monstre.getExperience());
        afficherCercleStats(g2d, experience, PolicesInterface.policeStatsMontres, zoneExperience);
        g2d.setColor(CouleursInterface.DORE);
        UtilitairesGraphique.centrerTexte(g2d, experience, PolicesInterface.policeStatsMontres, zoneExperience);
    }

    private void afficherCercleStats(Graphics2D g2d, String chaine, Font fonte, Rectangle bornes) {
        Image imageCercle = RepertoireImages.getImageCercle();
        Dimension espaceChaine = obtenirTailleChaine(g2d, chaine, fonte);
        int plusGrand = espaceChaine.height >= espaceChaine.width ? espaceChaine.height : espaceChaine.width;
        plusGrand += 2;
        int x = (int) bornes.getX() + (bornes.width - plusGrand) / 2;
        int y = (int) bornes.getY() + (bornes.height - plusGrand) / 2;
        g2d.drawImage(imageCercle, x, y, plusGrand, plusGrand, null);
    }
    
    private Dimension obtenirTailleChaine(Graphics2D g2d, String chaine, Font fonte) {
        FontMetrics metrics = g2d.getFontMetrics(fonte);
        int height = metrics.getHeight();
        int width = metrics.stringWidth(chaine);
        return new Dimension(width, height);
    }


}
