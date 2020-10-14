package ca.qc.bdeb.events;

/**
 * Une classe doit implémenter cette interface si elle veut gérer les ComponentSelectedEvent et les
 * envoyer aux écouteurs.
 *
 * @author Éric Wenaas
 * @version 1.0
 */
public interface ComponentSelectedHandler {
    /**
     * Ajoute un écouteur.
     *
     * @param ecouteur l'écouteur
     */
    void addComponentSelectedListener(ComponentSelectedListener ecouteur);

    /**
     * Retirer un écouteur.
     *
     * @param ecouteur L'écouteur
     */
    void removeComponentSelectedListener(ComponentSelectedListener ecouteur);
}
