/*
 * Copyright (C) 2015 Éric Wenaas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ca.qc.bdeb.tableaujeu;

import ca.qc.bdeb.graphics.Carre;
import ca.qc.bdeb.graphics.FormeAbstraite;
import java.awt.geom.Point2D;

/**
 *
 * @author Éric Wenaas <eric.wenaas@bdeb.qc.ca>
 * @version 0.1
 */

class ContexteCarre implements ContexteTableau {
    
    private final double taille;
    
    public ContexteCarre(double taille) {
        this.taille = taille;
    }

    @Override
    public double calculerHauteurRequise(int nombreTuiles) {
        return taille * nombreTuiles;
    }

    @Override
    public double calculerTaillePossibleHauteur(double hauteurDisponible, int nombreTuiles) {
        return (double) hauteurDisponible / nombreTuiles;
    }

    @Override
    public double calculerLargeurRequise(int nombreTuiles) {
        return taille * nombreTuiles;
    }

    @Override
    public double calculerTaillePossibleLargeur(double largeurDisponible, int nombreTuiles) {
        return (double) largeurDisponible / nombreTuiles;
    }

    @Override
    public FormeAbstraite getForme() {
        return new Carre(taille, 0, 0);
    }

    @Override
    public PositionGrille calculerPositionDansGrille(int positionX, int positionY) {
        return new PositionGrille(positionX / (int) taille, positionY / (int) taille);
    }

    @Override
    public Point2D.Double calculerCoordonneesForme(PositionGrille position) {
        return new Point2D.Double(taille * position.getX(), taille * position.getY());
    }

    @Override
    public double calculerEspacementLargeur() {
        return taille;
    }

    @Override
    public double calculerEspacementHauteur() {
        return taille;
    }
    
}
