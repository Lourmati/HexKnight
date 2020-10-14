package ca.qc.bdeb.events;

import java.util.EventListener;

/**
 * Interface à implémenter pour écouter les DropEvent.
 * 
 * @author Eric Wenaas
 * @version 1.0
 */

public interface DropEventListener extends EventListener  {

    /**
     * Appelé quand un objet est lâché sur un écouteur
     * 
     * @param evt l'événement
     */
    public void dropEventHappened(DropEvent evt);
}
