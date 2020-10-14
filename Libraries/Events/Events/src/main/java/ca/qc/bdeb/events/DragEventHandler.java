package ca.qc.bdeb.events;

/**
 * Une classe doit implémenter cette interface si elle veut gérer les DragEvent et les
 * envoyer aux écouteurs.
 * 
 * @author Eric Wenaas
 * @version 1.0
 */

public interface DragEventHandler {

    /**
     * Ajoute un écouteur
     * 
     * @param ecouteur l'écouteur
     */
    void addDragEventListener(DragEventListener ecouteur);

    /**
     * Retire un écouteur
     * 
     * @param ecouteur l'écouteur
     */
    void removeDragEventListener(DragEventListener ecouteur);
}
