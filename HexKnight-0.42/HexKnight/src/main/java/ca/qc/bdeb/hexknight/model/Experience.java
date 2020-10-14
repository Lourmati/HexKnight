/*
 * 
 * Copyright (C) 2017 Éric Wenaas
 * 
 */
package ca.qc.bdeb.hexknight.model;

import java.io.Serializable;

/**
 *
 * @author Eric Wenaas
 */
class Experience implements Serializable {
    
    private final static int[] SEUIL_EXPERIENCE = {0, 3, 8, 15, 24, 35, 48, 63, 80, 99};
    private int niveauArmure;
    private int tailleMain;
    private int experience;
    private int niveauExperience;

    Experience() {
        this.niveauArmure = 2;
        this.tailleMain = 5;
        this.experience = 0;
        niveauExperience = 0;
    }
    
    int getNiveauArmure() {
        return niveauArmure;
    }

    int getTailleMain() {
        return tailleMain;
    }
    
    public int getExperience() {
        return experience;
    }

    int getNiveauExperience() {
        return niveauExperience;
    }

    public int getProchainNiveau() {
        return SEUIL_EXPERIENCE[niveauExperience + 1];
    }

    void ajouterExperience(int exp) {
        experience += exp;
        while (experience >= getProchainNiveau()) {
            niveauExperience++;
            if (niveauExperience % 4 == 0 && niveauExperience % 2 == 0) {
                // On monte la taille de la main
                tailleMain++;
            } else if (niveauExperience % 2 == 0) {
                niveauArmure++;
            }
        }
    }
    
}
