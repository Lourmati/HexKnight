package ca.qc.bdeb.events;

/**
 * Une classe doit implémenter cette interface si elle veut gérer les DropEvent et les
 * envoyer aux écouteurs.
 * 
 * @author Eric Wenaas
 * @version 1.0
 */

public interface DropEventHandler {

    /**
     * Ajoute un éouteur
     * 
     * @param ecouteur l'écouteur
     */
    void addDropEventListener(DropEventListener ecouteur);

    /**
     * Retirer un écouteur
     * 
     * @param ecouteur l'écouteur
     */
    void removeDropEventListener(DropEventListener ecouteur);
}
