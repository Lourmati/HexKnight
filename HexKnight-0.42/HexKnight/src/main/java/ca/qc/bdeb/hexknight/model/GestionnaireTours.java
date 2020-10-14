/*
 * 
 * Copyright (C) 2016 Éric Wenaas
 * 
 */
package ca.qc.bdeb.hexknight.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author ewenaas
 */
class GestionnaireTours implements Serializable {
    
    private int nombreTours;
    private boolean derniereRonde;
    private int numeroTour;
    private final HashMap<Joueur, Boolean> gestionFinTour;

    GestionnaireTours(int nombreTours) {
        gestionFinTour = new HashMap<>();
        numeroTour = 1;
        derniereRonde = false;
        this.nombreTours = nombreTours;
    }

    boolean isDerniereRonde() {
        return derniereRonde;
    }

    void setJoueurTermine(Joueur joueur, boolean value) {
        gestionFinTour.put(joueur, value);
    }

    boolean isPartieTerminee() {
        return numeroTour > nombreTours;
    }    

    int getNumeroTour() {
        return numeroTour;
    }

    void declarerDerniereRonde() {
        derniereRonde = true;
    }
    
    boolean isTourTermine() {    
        return !gestionFinTour.containsValue(false);
    }

    void prochainTour() {
        numeroTour++;
        if (numeroTour <= nombreTours) {
            derniereRonde = false;
        }                        
    }
}
