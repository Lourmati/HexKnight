/*
 * Copyright (C) 2015 Éric Wenaas
 *
 */
package ca.qc.bdeb.hexknight.model;

import ca.qc.bdeb.hexknight.commun.CouleurCarte;

/**
 *
 * @author Éric Wenaas
 */
public enum TypeCarte {

    // Cartes de base
// Cartes de base
    CHEMIN_SECRET("Chemin secret", CouleurCarte.VERT),
    CRI_GUERRE("Cri de guerre", CouleurCarte.ROUGE),
    ATTAQUE_SURPRISE("Attaque surprise", CouleurCarte.BLANC),
    PROTECTION("Protection", CouleurCarte.BLEU),
    
    // Cartes spéciales
    FORTERESSE("Forteresse", CouleurCarte.BLANC),
    BOUCLIER("Bouclier", CouleurCarte.BLEU),
    SACRIFICE("Sacrifice", CouleurCarte.ROUGE),
    OASIS("Oasis", CouleurCarte.VERT),
    POLYVALENCE("Polyvalence", CouleurCarte.BLANC),
    POURSUITE("Poursuite", CouleurCarte.VERT),
    ARMURE("Armure", CouleurCarte.BLEU),
    
    // Cartes de malédiction
    BLESSURE(null, CouleurCarte.BLESSURE);
        
    private final CouleurCarte couleur;
    private String texte;

    private TypeCarte(String texte, CouleurCarte couleur) {
        this.texte = texte;
        this.couleur = couleur;
    }

    CouleurCarte getCouleur() {
        return couleur;
    }
    
    String getTexte() {
        return texte;
    }
}
