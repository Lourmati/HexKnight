package ca.qc.bdeb.events;

import java.util.EventObject;

/**
 * Événement déclenché lorsqu'un composant est sélectionné.
 *
 * @author Éric Wenaas
 * @version 1.0
 */
public class ComponentSelectedEvent extends EventObject {

    /**
     * Constructeur.
     *
     * @param obj l'objet sélectionné
     */
    public ComponentSelectedEvent(Object obj) {
        super(obj);
    }
}
