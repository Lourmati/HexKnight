/*
 * 
 * Copyright (C) 2016 Éric Wenaas
 * 
 */
package ca.qc.bdeb.hexknight.commun;

/**
 *
 * @author ewenaas
 */
public enum IDEtat {
    
    ATTAQUE("Attaque"),
    ATTENTE("Attente"),
    DEFENSE("Defense"),
    DEPLACEMENT("Déplacement"),
    DUMMY("Dummy"),
    ECHANGE("Échange"),
    PREPARATION("Préparation"),
    PROVOQUE("Provoque"),
    REPOS("Repos"),
    SELECTION("Sélection"),
    TERMINE("Terminé");
    
    private String nom;
    
    
    private IDEtat(String nom) {
        this.nom = nom;
    }
    
    public String getNom() {
        return nom;
    }
    
}
