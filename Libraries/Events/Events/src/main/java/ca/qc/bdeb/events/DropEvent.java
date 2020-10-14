package ca.qc.bdeb.events;

import java.util.EventObject;

/**
 * Événement envoyé lorsqu'un objet qui est en train de glisser est déposé.
 * 
 * @author Eric Wenaas
 * @version 1.0
 */

public class DropEvent extends EventObject  {

    /**
     * Constructeur
     * 
     * @param obj l'objet qui est déposé
     */
    public DropEvent(Object obj) {
        super(obj);
    }
}
