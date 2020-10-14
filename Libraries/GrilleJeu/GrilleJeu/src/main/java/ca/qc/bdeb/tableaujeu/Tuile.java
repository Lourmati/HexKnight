package ca.qc.bdeb.tableaujeu;

import java.io.Serializable;

/**
 * Définition de base de ce que comporte une tuile.
 * 
 * @author Eric Wenaas <eric.wenaas@bdeb.qc.ca>
 */
public interface Tuile extends Serializable {

    /**
     * Retourne la position de la tuile. La classe PositionGrille est utilisée et non un Point2D pour éviter les confusion
     * entre le contexte de position dans la grille et position en pixels sur l'écran.
     *
     * @return La position de la tuile.
     */
    PositionGrille getPosition();

    /**
     * Retourne le type de la tuile. Les types de tuiles sont éterminées par l'utilisateur de l'interface.
     *
     * @return le type de la tuile
     */
    Enum<?> getType();
}
