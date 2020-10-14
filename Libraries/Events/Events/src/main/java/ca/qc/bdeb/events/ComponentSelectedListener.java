package ca.qc.bdeb.events;

import java.util.EventListener;

/**
 * L'interface à implémenter si on veut écouter des ComponenentSelectedEvent
 *
 * @author Éric Wenaas
 * @version 1.0
 */
public interface ComponentSelectedListener extends EventListener {

    /**
     * Cette fonction est appelée sur les écouteurs lorsqu'un composant est sélectionné
     *
     * @param evt l'événement
     */
    public void onComponentSelectedEvent(ComponentSelectedEvent evt);
}
