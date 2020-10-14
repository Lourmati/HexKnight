package ca.qc.bdeb.events;

import java.util.EventObject;

/**
 * Événement lancé quand on commence à glisser un composant
 * 
 * @author Eric Wenaas
 * @version 1.0
 */
public class DragEvent extends EventObject {

    /**
     * Constructeur
     * 
     * @param obj l'objet qui est déplacé
     */
    public DragEvent(Object obj) {
        super(obj);
    }
    
}
