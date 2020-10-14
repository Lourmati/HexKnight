/*
 * Copyright (C) 2015 Éric Wenaas
 *
 */

package ca.qc.bdeb.hexknight.ui;

import ca.qc.bdeb.graphics.UtilitairesGraphique;
import ca.qc.bdeb.hexknight.commun.Personnage;
import ca.qc.bdeb.hexknight.model.Monstre;
import ca.qc.bdeb.hexknight.commun.CouleurCarte;
import ca.qc.bdeb.hexknight.commun.TypeAjoutTuile;
import ca.qc.bdeb.hexknight.commun.TypeTuile;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.EnumMap;

/**
 *
 * @author Eric Wenaas <eric.wenaas@bdeb.qc.ca>
 * @version 0.1
 */

class RepertoireImages {
    
    private final static EnumMap<CouleurCarte, BufferedImage> imagesCarte;
    private final static BufferedImage imagePaquet;
    private final static BufferedImage imageDefausse;
    private final static BufferedImage imageCercle;
    private final static EnumMap<Personnage, BufferedImage> imagesJoueurs;
    private final static EnumMap<TypeTuile, BufferedImage> imagesTuiles;
    private final static EnumMap<TypeAjoutTuile, BufferedImage> imagesFeatures;
    private final static EnumMap<Monstre.Type, BufferedImage> imagesMonstres;
    
    static {
        imagesCarte = new EnumMap<>(CouleurCarte.class);
        imagesCarte.put(CouleurCarte.BLEU, loadImage("/cartes/carteBleue.png"));
        imagesCarte.put(CouleurCarte.VERT, loadImage("/cartes/carteVerte.png"));
        imagesCarte.put(CouleurCarte.BLANC, loadImage("/cartes/carteBlanche.png"));
        imagesCarte.put(CouleurCarte.ROUGE, loadImage("/cartes/carteRouge.png"));
        imagesCarte.put(CouleurCarte.BLESSURE, loadImage("/cartes/carteBlessure.png"));
        imagePaquet = loadImage("/cartes/carteEndos.png");
        imageDefausse = loadImage("/cartes/carteEndos.png");
        imageCercle = loadImage("/divers/cercleNombre.png", (char) 192);
        
        imagesJoueurs = new EnumMap<>(Personnage.class);
        imagesJoueurs.put(Personnage.KONRAD,  loadImage("/personnages/konrad.png"));
        imagesJoueurs.put(Personnage.LISAR, loadImage("/personnages/lisar.png"));
        imagesJoueurs.put(Personnage.DELFADOR, loadImage("/personnages/delfador.png"));
        
        
        imagesTuiles = new EnumMap<>(TypeTuile.class);
        imagesTuiles.put(TypeTuile.COTE, loadImage("/tuiles/cote.png"));
        imagesTuiles.put(TypeTuile.PLAINES, loadImage("/tuiles/plaines.png"));
        imagesTuiles.put(TypeTuile.COLLINES, loadImage("/tuiles/collines.png"));
        imagesTuiles.put(TypeTuile.MARAIS, loadImage("/tuiles/marais.png"));
        imagesTuiles.put(TypeTuile.ARIDE, loadImage("/tuiles/aride.png"));
        imagesTuiles.put(TypeTuile.MONTAGNE, loadImage("/tuiles/montagne.png"));
        imagesTuiles.put(TypeTuile.FORET, loadImage("/tuiles/foret.png"));
        imagesTuiles.put(TypeTuile.DESERT, loadImage("/tuiles/desert.png"));
        imagesTuiles.put(TypeTuile.VIDE, loadImage("/tuiles/vide.png"));

        imagesFeatures = new EnumMap<>(TypeAjoutTuile.class);
        imagesFeatures.put(TypeAjoutTuile.CHATEAU, loadImage("/ajouts/chateau.png"));
        imagesFeatures.put(TypeAjoutTuile.CERCLE_MAGIQUE, loadImage("/ajouts/cercleMagique.png"));        
        imagesFeatures.put(TypeAjoutTuile.MONASTERE, loadImage("/ajouts/monastere.png"));
        imagesFeatures.put(TypeAjoutTuile.PORTAIL, loadImage("/ajouts/portail.png"));
        imagesFeatures.put(TypeAjoutTuile.VILLAGE, loadImage("/ajouts/village.png"));
        imagesFeatures.put(TypeAjoutTuile.RUINES, loadImage("/ajouts/ruines.png"));
        imagesFeatures.put(TypeAjoutTuile.TOMBEAU, loadImage("/ajouts/tombeau.png"));
        
        imagesMonstres = new EnumMap<>(Monstre.Type.class);
        imagesMonstres.put(Monstre.Type.ORQUE, loadImage("/monstres/orque.png"));
        imagesMonstres.put(Monstre.Type.DRAGON, loadImage("/monstres/dragon.png"));
        imagesMonstres.put(Monstre.Type.MORT_VIVANT, loadImage("/monstres/squelette.png"));
        imagesMonstres.put(Monstre.Type.SOLDAT, loadImage("/monstres/soldat.png"));
        imagesMonstres.put(Monstre.Type.INCONNU, loadImage("/monstres/inconnu.png"));
    }
    
    private static BufferedImage loadImage(String cheminFichier) {
        URL  urlImage = RepertoireImages.class.getResource(cheminFichier);
        if (urlImage == null) {
            throw new IllegalStateException("RepertoireImage" + ": " + cheminFichier + " pas trouvée");
        }
        BufferedImage image = UtilitairesGraphique.chargerImage(urlImage);
        return image;
    }
    
    private static BufferedImage loadImage(String cheminFichier, char alpha) {
        URL  urlImage = RepertoireImages.class.getResource(cheminFichier);
        
        if (urlImage == null) {
             throw new IllegalStateException("RepertoireImage" + ": " + cheminFichier + " pas trouvée");
       }
        BufferedImage image = UtilitairesGraphique.chargerImage(urlImage);
        image = UtilitairesGraphique.ajouterTransparence(image, alpha);
        return image;
    }

    static BufferedImage getImageCarte(CouleurCarte couleur) {
        return imagesCarte.get(couleur);
    }
    
    static BufferedImage getImageJoueur(Personnage personnage) {
        return imagesJoueurs.get(personnage);
    }
    
    static BufferedImage getImagePaquet() {
        return imagePaquet;
    }
    
    static BufferedImage getImageDefausse() {
        return imageDefausse;
    }
    
    static BufferedImage getImageCercle() {
        return imageCercle;
    }

    static BufferedImage getImageTuiles(TypeTuile type) {
        return imagesTuiles.get(type);
    }

    static BufferedImage getImageFeature(TypeAjoutTuile feature) {
        return imagesFeatures.get(feature);
    }

    static BufferedImage getImageMonstre(Monstre.Type type) {
        return imagesMonstres.get(type);
    }
}