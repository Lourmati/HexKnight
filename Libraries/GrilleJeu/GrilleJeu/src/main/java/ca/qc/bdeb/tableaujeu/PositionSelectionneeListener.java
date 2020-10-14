package ca.qc.bdeb.tableaujeu;

import java.util.EventListener;

/**
 * Interface pour les écouteurs de l'événement PositionSelectionneeEvent
 * 
 * @author Éric Wenaas
 */
public interface PositionSelectionneeListener extends EventListener {
    /**
     * Méthode appeleée quand une position est sélectionnée
     * 
     * @param event L'événement
     */
    public void locationSelectedOccured(PositionSelectionneeEvent event);
}
