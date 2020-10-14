/*
 * Copyright (C) 2015 Éric Wenaas
 *
 */
package ca.qc.bdeb.hexknight.model;

import ca.qc.bdeb.hexknight.commun.TypeAjoutTuile;
import ca.qc.bdeb.hexknight.commun.TypeTuile;
import ca.qc.bdeb.tableaujeu.PositionGrille;
import ca.qc.bdeb.tableaujeu.Tuile;

/**
 * Les tuiles du jeu. Une tuile contient un type ainsi qu'u ajout.
 *
 * @author Eric Wenaas <eric.wenaas@bdeb.qc.ca>
 * @version 0.1
 */
public class TuileJeu implements Tuile {
    private final PositionGrille position;
    private final TypeTuile type;
    private final TypeAjoutTuile ajout;
    private Monstre monstre;
    private boolean revelee;

    /**
     * Paramètre sans ajout à la tuile. On prend la valeur par défaut qui est AjoutTuile.NONE
     *
     * @param position La position de la tuile
     * @param typeTuile Le type de tuile
     */
    public TuileJeu(PositionGrille position, TypeTuile typeTuile) {
        this(position, typeTuile, TypeAjoutTuile.AUCUN);
    }

    /**
     * Constructeur pour les tuiles qui ont un ajout en plus du type de la tuile.
     *
     * @param position La position sur la grille
     * @param typeTuile Le type de tuile
     * @param ajout L'ajout à la tuile
     */
    public TuileJeu(PositionGrille position, TypeTuile typeTuile, TypeAjoutTuile ajout) {
        this.ajout = ajout;
        this.position = position;
        this.type = typeTuile;
        this.monstre = null;
        this.revelee = false;
    }

    /**
     * Retourne simplement l'ajout à une tuile
     *
     * @return l'ajout
     */
    public TypeAjoutTuile getAjout() {
        return ajout;
    }

    int getMouvementJour() {
        return type.getMouvementJour();        
    }

    int getMouvementNuit() {
        return type.getMouvementNuit();
    }
    
    int getVisibiliteJour() {
        int visibiliteTuile = type.getVisibiliteJour();
        int visibiliteAjout = ajout.getVisibiliteJour();
        return visibiliteTuile > visibiliteAjout ? visibiliteTuile : visibiliteAjout;        
    }
    
    int getVisibiliteNuit() {
        int visibiliteTuile = type.getVisibiliteNuit();
        int visibiliteAjout = ajout.getVisibiliteNuit();
        return visibiliteTuile > visibiliteAjout ? visibiliteTuile : visibiliteAjout;
    }
    
    int getHauteur() {
        int hauteurTuile = type.getHauteur();
        int hauteurAjout = ajout.getHauteur();
        return hauteurTuile > hauteurAjout ? hauteurTuile : hauteurAjout;
    }

    @Override
    public PositionGrille getPosition() {
        return position;
    }

    @Override
    public Enum<?> getType() {
        return type;
    }
    
    public Monstre getMonstre() {
        return monstre;
    }

    public boolean contientMonstre() {
        return monstre != null;
    }
    
    void setMonstre(Monstre unMonstre) {
        this.monstre = unMonstre;
    }
    
    void revelerTuile() {
        this.revelee = true;
    }

    public boolean estRevelee() {
        return revelee;
    }
    
    public int obtenirAjoutGuerison() {
        int valeur = 0;
        switch (ajout) {
            case MONASTERE:
            case CHATEAU:
                valeur = 1;
                break;
        }
        return valeur;
    }
    
    public int obtenirAjoutMain() {
        int valeur = 0;
        switch (ajout) {
            case VILLAGE:
            case CHATEAU:
                valeur = 1;
                break;
            
        }
        return valeur;
    }
}
