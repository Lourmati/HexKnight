/*
 * 
 * Copyright (C) 2015 Éric Wenaas
 * 
 */
package ca.qc.bdeb.hexknight.ai;

import ca.qc.bdeb.hexknight.model.CarteJeu;
import ca.qc.bdeb.hexknight.model.TuileJeu;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe qui stocke les éléments essentiels du plan de l'AI pour la ronde courante. Aucune encapsulation n'est
 * nécessaire puisque la classe n'a aucun comportement.
 *
 * @author Éric Wenaas
 * @version 0.40
 */
class ObjectifAI implements Cloneable, Serializable {

    int coutDeplacement;
    int defense;
    int attaque;
    int guerison;
    int blessures;
    List<CarteJeu> meilleurePermutation;  // L'ordre dans lequel les cartes sont jouées dans la liste des cartes en main
    List<CarteJeu> cartesMain; // les cartes en main.
    TuileJeu tuileDepart;
    TuileJeu tuileDestination;
    TuileJeu tuileCombat;
    double valeurObjectif;
    
    ObjectifAI(List<CarteJeu> cartesMain) {
        coutDeplacement = 0;
        defense = 0;
        attaque = 0;
        guerison = 0;
        blessures = 0;
        meilleurePermutation = null;
        this.cartesMain = cartesMain;
        tuileDepart = null;
        tuileDestination = null;
        tuileCombat = null;
        valeurObjectif = 0.0;
    }    

    boolean estPossible(List<CarteJeu> indiceCartes, boolean actionPosee) {
        Iterator<CarteJeu> iter = indiceCartes.iterator();
        return deplacementEstPossible(iter) &&
               defenseEstPossible(iter) &&
               attaqueEstPossible(iter) && 
               guerisonEstPossible(iter, actionPosee);
    }

    private boolean deplacementEstPossible(Iterator<CarteJeu> iter) {
        int valeurAccumulee = 0;
        while (valeurAccumulee < coutDeplacement && iter.hasNext()) {
            CarteJeu carte = iter.next();
            valeurAccumulee += carte.getVitesse();
        }
        return valeurAccumulee >= coutDeplacement;
    }

    private boolean defenseEstPossible(Iterator<CarteJeu> iter) {
        int valeurAccumulee = 0;
        while (valeurAccumulee < defense && iter.hasNext()) {
            CarteJeu carte = iter.next();
            valeurAccumulee += carte.getDefense();
        }
        return valeurAccumulee >= defense;
    }

    private boolean attaqueEstPossible(Iterator<CarteJeu> iter) {
        int valeurAccumulee = 0;
        while (valeurAccumulee < attaque && iter.hasNext()) {
            CarteJeu carte = iter.next();
            valeurAccumulee += carte.getAttaque();
        }
        return valeurAccumulee >= attaque;
    }
 
    private boolean guerisonEstPossible(Iterator<CarteJeu> iter, boolean actionPosee) {
            int valeurAccumulee = tuileDestination.obtenirAjoutGuerison();
            // Si a rien fait on peut se guérir de 1 en plus.
            if (tuileDestination == tuileDepart && tuileCombat == null && ! actionPosee) {
                valeurAccumulee++;
            }
            while (valeurAccumulee < guerison && iter.hasNext()) {
                CarteJeu carte = iter.next();
                valeurAccumulee += carte.getMeditation();
            }
            return valeurAccumulee >= guerison;
    }

    int compterCartesRestantes(List<CarteJeu> cartes, boolean actionPosee) {
        int nbCartesTotales = cartes.size();
        Iterator<CarteJeu> iter = cartes.iterator();
        int nbCartesUtilisees = compterCartesDeplacement(iter) +
                              compterCartesDefense(iter) +
                              compterCartesAttaque(iter) +
                              compterCartesGuerison(iter, actionPosee);
        return nbCartesTotales - nbCartesUtilisees;
    }
    
    private int compterCartesDeplacement(Iterator<CarteJeu> iter) {
        int valeurAccumulee = 0;
        int nbCartesUtilisees = 0;
        while (valeurAccumulee < coutDeplacement && iter.hasNext()) {
            CarteJeu carte = iter.next();
            valeurAccumulee += carte.getVitesse();
            nbCartesUtilisees++;
        }
        return nbCartesUtilisees;
    }

    private int compterCartesDefense(Iterator<CarteJeu> iter) {
        int valeurAccumulee = 0;
        int nbCartesUtilisees = 0;
        while (valeurAccumulee < defense && iter.hasNext()) {
            CarteJeu carte = iter.next();
            valeurAccumulee += carte.getDefense();
            nbCartesUtilisees++;
        }
        return nbCartesUtilisees;
    }

    private int compterCartesAttaque(Iterator<CarteJeu> iter) {
        int valeurAccumulee = 0;
        int nbCartesUtilisees = 0;
        while (valeurAccumulee < attaque && iter.hasNext()) {
            CarteJeu carte = iter.next();
            valeurAccumulee += carte.getAttaque();
            nbCartesUtilisees++;
        }
        return nbCartesUtilisees;
    }

    private int compterCartesGuerison(Iterator<CarteJeu> iter, boolean actionPosee) {
        int valeurAccumulee = tuileDestination.obtenirAjoutGuerison();
        int nbCartesUtilisees = 0;

        // Si a rien fait on peut se guérir de 1 en plus.
        if (tuileDestination == tuileDepart && tuileCombat == null && ! actionPosee) {
            valeurAccumulee++;
        }
        
        while (valeurAccumulee < guerison && iter.hasNext()) {
            CarteJeu carte = iter.next();
            valeurAccumulee += carte.getMeditation();
            nbCartesUtilisees++;
        }
        return nbCartesUtilisees;
    }

    @Override
    public ObjectifAI clone() {
        ObjectifAI clone = null;
        try {
            clone = (ObjectifAI) super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(ObjectifAI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return clone;
    }
}
