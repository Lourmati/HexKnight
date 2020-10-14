package ca.qc.bdeb.tableaujeu;

import ca.qc.bdeb.graphics.FormeAbstraite;
import java.awt.Graphics2D;

/**
 * Interface qui détermine ce que doit faire un objet dessinateur de tuiles. C'est une application du patron de
 * conception Stratégie.
 * 
 * Un dessinateur de Tuiles doit être passée à une instance de <code>PanneauCarte</code> afin de s'assurer qu'il peut
 * dessiner correctement les tuiles sans avoir à en connaître les détails.
 * 
 * @author Éric Wenaas
 */

public interface DessinateurTuile {
    /**
     * Permet de dessiner une tuile. Doit être appelé par l'objet chargé de décider si on doit dessiner la tuile
     * 
     * @param g2d l'objet graphique
     * @param tuile la tuile à dessiner
     * @param contexte le contexte de la carte à dessiner
     * @param forme la forme que possède la tuile
     */
    public void dessinerTuile(Graphics2D g2d, Tuile tuile, ContexteTableau contexte, FormeAbstraite forme);
}
