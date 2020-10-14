package ca.qc.bdeb.tableaujeu;

import ca.qc.bdeb.graphics.FormeAbstraite;
import java.awt.Point;

/**
 * Interface qui a pour but de séparer la connaissance de la façon de représenter les tuiles
 * du type de tuile. Une représentation d'une carte de tuiles devrait pouvoir représenter
 * de façon transparente une carte sans pour autant connaître la nature réelle des tuiles.
 * 
 * Toutes les tuiles d'une même carte devrait avoir la même forme. 
 * 
 * @author Eric Wenaas
 */

public interface ContexteTableau {
    /**
     * Donne la hauteur totale nécessaire pour représenter toutes les tuiles sur
     * la hauteur. La taile des tuiles est une valeur qui ne représente pas toujours
     * la même chose. Par exemple, pour un carré ce peut être la longueur d'une arête.
     * Pour un cercle ce sera plutôt le rayon.
     * 
     * Implémentation du patron de conception Stratégie.
     * 
     * @param nombreTuiles Le nombre de tuiles sur la hauteur
     * @return Le nombre de pixels requis
     */
    public double calculerHauteurRequise(int nombreTuiles);
    
    /**
     * Cette fonction doit être l'inverse de la précédente. Elle retourne la valeur
     * maximale que peut avoir la taille en pixels en fonction de la hauteur
     * disponible.
     * 
     * @param hauteurDisponible Le nombre de pixels requis pour afficher la carte
     * @param nombreTuiles Le nombre de tuiles à afficher dans cette hauteur
     * @return La taille maximale pour tout afficher
     */
    public double calculerTaillePossibleHauteur(double hauteurDisponible, int nombreTuiles);
    
    /**
     * Donne la largeur totale nécessaire pour représenter toutes les tuiles sur
     * la largeur. La taile des tuiles est une valeur qui ne représente pas toujours
     * la même chose. Par exemple, pour un carré ce peut être la longueur d'une arête.
     * Pour un cercle ce sera plutôt le rayon.
     * 
     * @param nombreTuiles Le nombre de tuiles sur la hauteur
     * @return Le nombre de pixels requis
     */
    public double calculerLargeurRequise(int nombreTuiles);
    
    /**
     * Cette fonction doit être l'inverse de la précédente. Elle retourne la valeur
     * maximale que peut avoir la taille en pixels en fonction de la largeur
     * disponible.
     * 
     * @param largeurDisponible Le nombre de pixels requis pour afficher la carte
     * @param nombreTuiles Le nombre de tuiles à afficher dans cette largeur
     * @return Le nombre de pixels requis
     */
    public double calculerTaillePossibleLargeur(double largeurDisponible, int nombreTuiles);
    
    /**
     * Permet d'obtenir la forme correspondante à la bonne stratégie.
     * 
     * @return La forme correspondante
     */
    public FormeAbstraite getForme();
       
    /**
     * Retourne la position dans la grille à partir de la position en pixel.
     * 
     * @param positionX La position en pixel de la forme
     * @param positionY La position en pixel de la forme
     * @return La position dans la grille
     */
    public PositionGrille calculerPositionDansGrille(int positionX, int positionY);
    
    /**
     * Retourne la position en pixels correspondant à la position.
     * 
     * @param position La position dans la grille
     * @return 
     */
    public Point.Double calculerCoordonneesForme(PositionGrille position);
    
    /**
     * L'espacement sur la largeur permet de déterminer à quel endroit la prochaine tuile sera placée. Il peut différer
     * de la largeur de la tuile selon sa forme. Par exemple, un hexagone,
     * 
     * @return la largeur requise
     */
    public double calculerEspacementLargeur();
    
    /**
     * L'espacement sur la hauteur permet de déterminer à quel endroit la prochaine tuile sera placée. Il peut différer
     * de la hauteur de la tuile selon sa forme. Par exemple, un hexagone,
     * 
     * @return la hauteur requise
     */
    public double calculerEspacementHauteur();
}
