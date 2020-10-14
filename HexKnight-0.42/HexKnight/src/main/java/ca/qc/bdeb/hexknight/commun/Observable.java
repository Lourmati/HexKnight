package ca.qc.bdeb.hexknight.commun;


/**
 * 
 * @author Eric Wenaas <eric.wenaas@bdeb.qc.ca>
 * @version 0.40
 * 
 */

public interface Observable {

    /**
     * @param ob the observer
     */
    public void ajouterObservateur(Observateur ob);

    /**
     * @param ob the observer
     */
    public void retirerObservateur(Observateur ob);

    /**
     */
    public void aviserObservateurs();

    /**
     * @param propriete La propriété modifiée
     * @param o Valeur associée a la propriété
     */
    public void aviserObservateurs(Enum<?> propriete, Object o);

}
