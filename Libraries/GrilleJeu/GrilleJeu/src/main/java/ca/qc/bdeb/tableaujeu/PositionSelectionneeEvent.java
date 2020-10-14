package ca.qc.bdeb.tableaujeu;

import java.util.EventObject;

/**
 * Un évenement qui est lancé quand une position est sélectionner dans une grille.
 *
 * @author Éric Wenaas
 */
public class PositionSelectionneeEvent extends EventObject {

    private final PositionGrille pos;

    /**
     * L'événement comprend l'objet qui a émis l'événement ainsi que la position qui a été sélectionnée
     *
     * @param source L'objet source de l'événement
     * @param pos La position sélectionnée
     */
    public PositionSelectionneeEvent(Object source, PositionGrille pos) {
        super(source);
        this.pos = pos;
    }

    /**
     * Retourne simplement la position sélectionnée
     *
     * @return La position
     */
    public PositionGrille getPositionSelectionnee() {
        return pos;
    }
}
