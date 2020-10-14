/*
 * Copyright (C) 2015 Éric Wenaas
 *
 */
package ca.qc.bdeb.hexknight.commun;

import ca.qc.bdeb.hexknight.model.Monstre;

/**
 * Énumération des différents ajouts aux tuiles. Par exemple, un monastère.
 *
 * @author Éric Wenaas
 */
public enum TypeAjoutTuile {

    AUCUN(null, 0, 0, 0),
    PORTAIL(null, 0, 0, 0),
    VILLAGE(null, 0, 0, 0),
    MONASTERE(null, 0, 0, 0),
    CHATEAU(Monstre.Type.SOLDAT, 3, 1, 3),
    CERCLE_MAGIQUE(null, 0, 0, 0),
    MINE(null, 0, 0, 0),
    TOUR(null, 3, 1, 3),
    ANTRE(null, 0, 0, 0),
    DONJON(null, 0, 0, 0),
    VILLE(null, 3, 1, 3),
    ORQUES(Monstre.Type.ORQUE, 0, 0, 0),
    DRAGONS(Monstre.Type.DRAGON, 0, 0, 0),
    RUINES(Monstre.Type.MORT_VIVANT, 0, 0, 0),
    TOMBEAU(Monstre.Type.MORT_VIVANT, 0, 0, 0);
    
    private final Monstre.Type typeMonstre;
    private final int visibiliteJour;
    private final int visibiliteNuit;
    private final int hauteur;
    
    private TypeAjoutTuile(Monstre.Type typeMonstre, int visibiliteJour, int visibiliteNuit, int hauteur) {
        this.typeMonstre = typeMonstre;
        this.visibiliteJour = visibiliteJour;
        this.visibiliteNuit = visibiliteNuit;
        this.hauteur = hauteur;
    }
    
    public Monstre.Type getTypeMonstre() {
        return typeMonstre;
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
