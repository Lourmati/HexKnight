/*
 * Copyright (C) 2015 Éric Wenaas
 *
 */
package ca.qc.bdeb.hexknight.commun;

/**
 *
 * @author Éric Wenaas
 * @version 0.40
 */
public enum Personnage {
    KONRAD("Konrad"),
    LISAR("Li`sar"),
    DELFADOR("Delfador");
    
    private final String nom;
    
    private Personnage(String nom) {
        this.nom = nom;
    }
    
    public String getNom() {
        return nom;
    }
}


