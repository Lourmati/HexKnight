/*
 * Copyright (C) 2015 Éric Wenaas
 *
 */
package ca.qc.bdeb.hexknight.ui;

import ca.qc.bdeb.graphics.FormeAbstraite;
import ca.qc.bdeb.tableaujeu.DessinateurTuile;
import ca.qc.bdeb.tableaujeu.Tuile;
import ca.qc.bdeb.hexknight.model.TuileJeu;
import ca.qc.bdeb.hexknight.model.Partie;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;
import ca.qc.bdeb.tableaujeu.ContexteTableau;

/**
 * Cette classe dessine les tuiles en utilisant la classe RepertoireImages.
 *
 * @author Éric Wenaas
 * @version 0.25
 */
public class DessinateurTuileHexKnight implements DessinateurTuile {

    List<DessinateurElementTuile> elements;
    private final DessinateurCoordonnees dessinateurCoordonnees;
    private final DessinateurMonstre dessinateurMonstres;
    private boolean coordonneesActive;
    private boolean monstresVisibles;
    
    public DessinateurTuileHexKnight(Partie modele) {
        elements = new LinkedList<>();
        dessinateurCoordonnees = new DessinateurCoordonnees();
        dessinateurMonstres = new DessinateurMonstre();
        ajouterDessinateurs(modele);
        coordonneesActive = false;
        monstresVisibles = true;
    }
    
    @Override
    public void dessinerTuile(Graphics2D g2d, Tuile tuile, ContexteTableau contexte, FormeAbstraite forme) {
        dessinerTuile(g2d, (TuileJeu) tuile, forme);
    }

    public void dessinerTuile(Graphics2D g2d, TuileJeu tuile, FormeAbstraite forme) {
        for (DessinateurElementTuile dess : elements) {
            dess.dessinerElement(g2d, tuile, forme);
        }
    }
    
    void viderListe() {
        elements = new LinkedList<>();
    }
    
    private void ajouterDessinateur(DessinateurElementTuile dess, int indice) {
        if (indice >= elements.size()) {
            elements.add(dess);
        } else {
            elements.add(indice, dess);
        }
    }
    
    private void retirerDessinateur(DessinateurElementTuile dess) {
        elements.remove(dess);
    }    

    void activerCoordonnees() {
            if (coordonneesActive) {
                retirerDessinateur(dessinateurCoordonnees);
            } else {
                ajouterDessinateur(dessinateurCoordonnees, 5);                
            }
            coordonneesActive = !coordonneesActive;
    }

    void afficherMonstres() {
            if (monstresVisibles) {
                retirerDessinateur(dessinateurMonstres);
            } else {
                ajouterDessinateur(dessinateurMonstres, 3);
            }
            monstresVisibles =  ! monstresVisibles;
    }
    
    private void ajouterDessinateurs(Partie modelePartie) {
        ajouterDessinateur(new DessinateurTerrain(), 0);
        ajouterDessinateur(new DessinateurAjout(), 1);
        ajouterDessinateur(dessinateurMonstres, 2);
        ajouterDessinateur(new DessinateurJoueur(modelePartie), 3);
        ajouterDessinateur(new DessinateurDistances(modelePartie), 4);
        ajouterDessinateur(new DessinateurTuileNoire(), 5);
    }

}
