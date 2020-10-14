/*
 * Copyright (C) 2015 ewenaas
 */

package ca.qc.bdeb.hexknight.model;

import ca.qc.bdeb.hexknight.commun.CouleurCarte;
import java.io.Serializable;

/**
 *
 * @author Eric Wenaas <eric.wenaas@bdeb.qc.ca>
 * @version 0.1
 */

public class CarteJeu implements Carte, Serializable {
    
    private final TypeCarte type;
    private final int attaque;
    private final int defense;
    private final int vitesse;
    private final int meditation;
    
    
    static CarteJeu construireCarteJeu(TypeCarte type) {
        CarteJeu carte = null;
        switch (type) {
            case ATTAQUE_SURPRISE:
                carte = new CarteJeu(type, 2, 1, 2, 0);
                break;
            case PROTECTION:
                carte = new CarteJeu(type, 1, 2, 2, 0);
                break;
            case CRI_GUERRE:
                carte = new CarteJeu(type, 2, 2, 1, 0);
                break;
            case CHEMIN_SECRET:
                carte = new CarteJeu(type, 1, 1, 2, 1);
                break;
            case BOUCLIER:
                carte = new CarteJeu(type, 1, 3, 2, 0);
                break;
            case FORTERESSE:
                carte = new CarteJeu(type, 2, 3, 1, 0);
                break;
            case OASIS:
                carte = new CarteJeu(type, 1, 1, 1, 2);
                break;
            case SACRIFICE:
                carte = new CarteJeu(type, 4, 1, 1, 0);
                break;
            case POLYVALENCE:
                carte = new CarteJeu(type, 2, 2, 2, 1);
                break;
            case POURSUITE:
                carte = new CarteJeu(type, 1, 1, 3, 0);
                break;
            case ARMURE:
                carte = new CarteJeu(type, 1, 4, 0, 1);
                break;
            case BLESSURE:
                carte = new CarteJeu(type, 0, 0, 0, 0);
                break;
                
        }
        return carte;
    }
    
    private CarteJeu(TypeCarte type, int attaque, int defense, int vitesse, int meditation) {
        this.type = type;
        this.attaque = attaque;
        this.vitesse = vitesse;
        this.defense = defense;
        this.meditation = meditation;
    }

    public TypeCarte getType() {
        return type;
    }
    
    public String getTexte() {
        return type.getTexte();
    }
    
    public CouleurCarte getCouleur() {
        return type.getCouleur();
    }
    
    public int getAttaque() {
        return attaque;
    }

    public int getDefense() {
        return defense;
    }

    public int getVitesse() {
        return vitesse;
    }
    
    public int getMeditation() {
        return meditation;
    }
}
