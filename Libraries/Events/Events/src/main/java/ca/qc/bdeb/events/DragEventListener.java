package ca.qc.bdeb.events;

import java.util.EventListener;

/**
 * Interface à implémenter pour écouter les événements de déplacement.
 * 
 * @author Eric Wenaas
 * @version 1.0
 */

public interface DragEventListener extends EventListener {

    /**
     * Appelé lorsque qu'on commence à faire glisser un objet.
     * 
     * @param evt l'événement
     */
    public void startDragEventHappened(DragEvent evt);
}
