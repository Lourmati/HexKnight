/*
 * Copyright (C) 2015 Éric Wenaas
 *
 */
package ca.qc.bdeb.hexknight.commun;

/**
 * Énumération des types de tuiles dans le jeu.
 *
 * @author Éric Wenaas
 */
public enum TypeTuile {
    VIDE(15000, 15000, 0, 0, 15000),
    PLAINES(2, 2, 2, 1, 1),
    COLLINES(3, 3, 3, 1, 3),
    COTE(15000, 15000, 2, 1, 1),
    FORET(3, 5, 1, 1, 2),
    MONTAGNE(15000, 15000, 4, 2, 4),
    DESERT(5, 3, 2, 1, 1),
    MARAIS(4, 4, 2, 1, 1),
    ARIDE(3, 3, 2, 1, 1);
    
    private final int mouvementJour;
    private final int mouvementNuit;
    private final int visibiliteJour;
    private final int visibiliteNuit;
    private final int hauteur;
    
    private TypeTuile(int mouvementJour, int mouvementNuit, int visibiliteJour, int visibiliteNuit, int hauteur) {
        this.mouvementJour = mouvementJour;
        this.mouvementNuit = mouvementNuit;
        this.visibiliteJour = visibiliteJour;
        this.visibiliteNuit = visibiliteNuit;
        this.hauteur = hauteur;        
    }

    public int getMouvementJour() {
        return mouvementJour;
    }
    
    public int getMouvementNuit() {
        return mouvementNuit;        
    }
    
    public int getVisibiliteJour() {
        return visibiliteJour;
    }
    
    public int getVisibiliteNuit() {
        return visibiliteNuit;
    }
    
    public int getHauteur() {
        return hauteur;
    }
}
