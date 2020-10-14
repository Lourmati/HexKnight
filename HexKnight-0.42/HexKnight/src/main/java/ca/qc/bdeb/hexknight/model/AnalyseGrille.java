package ca.qc.bdeb.hexknight.model;

import ca.qc.bdeb.tableaujeu.PositionGrille;
import java.util.HashMap;

/**
 *
 * @author ewenaas
 */
public class AnalyseGrille {
    public HashMap<PositionGrille, Integer> destinations;
    public HashMap<PositionGrille, PositionGrille> precedent;

    public AnalyseGrille(HashMap<PositionGrille, Integer> destinations, HashMap<PositionGrille, PositionGrille> precedent) {
        this.destinations = destinations;
        this.precedent = precedent;
    }
}
