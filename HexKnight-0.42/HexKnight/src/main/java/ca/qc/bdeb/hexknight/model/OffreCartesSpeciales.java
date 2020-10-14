/*
 * 
 * Copyright (C) 2015 Éric Wenaas
 * 
 */
package ca.qc.bdeb.hexknight.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Eric Wenaas <eric.wenaas@bdeb.qc.ca> 
 * @version 0.1
 */
class OffreCartesSpeciales implements Serializable {

    private PaquetCartes<CarteJeu> cartesSpeciales;
    private List<CarteJeu> offreCartes;

    OffreCartesSpeciales(Random generateurAleatoire) {
        creerCartesSpeciales(generateurAleatoire);
        initialiserOffre();
    }

    private void creerCartesSpeciales(Random generateurAleatoire) {
        cartesSpeciales = new PaquetCartes<>();
        for (int i = 0; i < 4; i++) {
            cartesSpeciales.addToTop(CarteJeu.construireCarteJeu(TypeCarte.OASIS));
            cartesSpeciales.addToTop(CarteJeu.construireCarteJeu(TypeCarte.BOUCLIER));
            cartesSpeciales.addToTop(CarteJeu.construireCarteJeu(TypeCarte.FORTERESSE));
            cartesSpeciales.addToTop(CarteJeu.construireCarteJeu(TypeCarte.SACRIFICE));
            cartesSpeciales.addToTop(CarteJeu.construireCarteJeu(TypeCarte.POLYVALENCE));
            cartesSpeciales.addToTop(CarteJeu.construireCarteJeu(TypeCarte.POURSUITE));
            cartesSpeciales.addToTop(CarteJeu.construireCarteJeu(TypeCarte.ARMURE));
        }
        cartesSpeciales.brasserCartes(generateurAleatoire);
    }

    CarteJeu obtenirCarteOffre(int indice) {
        return offreCartes.get(indice);
    }

    void retirerCarteOffre(CarteJeu carte) {
        offreCartes.remove(carte);
        offreCartes.add(cartesSpeciales.pigerUneCarte());
    }

    List<CarteJeu> getOffre() {
        ArrayList<CarteJeu> cartes = new ArrayList<>();
        for (CarteJeu carte : offreCartes) {
            cartes.add(carte);
        }
        return cartes;
    }

    final void initialiserOffre() {
        offreCartes = new LinkedList<>();
        int tailleOffre = 3;
        for (int i = 0; i < tailleOffre; i++) {
            offreCartes.add(cartesSpeciales.pigerUneCarte());
        }
    }
}
