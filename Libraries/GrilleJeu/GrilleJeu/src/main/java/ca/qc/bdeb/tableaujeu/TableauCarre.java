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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 *
 * @author Eric Wenaas <eric.wenaas@bdeb.qc.ca>
 */
public class TableauCarre<T extends Tuile> implements Grllle<T>, Serializable {

    private final ArrayList<ArrayList<T>> tiles;
    private final int hauteur;
    private final int largeur;

    public TableauCarre(int largeur, int hauteur) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        tiles = new ArrayList<>(largeur);
        for (int i = 0; i < largeur; i++) {
            tiles.add(new ArrayList<T>(hauteur));
            for (int j = 0; j < hauteur; j++) {
                tiles.get(i).add(null);
            }
        }

    }

    @Override
    public T getElementAt(PositionGrille p) {
        return getElementAt(p.getX(), p.getY());
    }

    @Override
    public T getElementAt(int x, int y) {
        return tiles.get(x).get(y);
    }

    @Override
    public void setElementAt(T tile, int x, int y) {
        tiles.get(x).set(y, tile);
    }

    @Override
    public int getHeight() {
        return hauteur;
    }

    @Override
    public int getWidth() {
        return largeur;
    }

    @Override
    public void addElement(T elem) {
        int x = elem.getPosition().getX();
        int y = elem.getPosition().getY();
        tiles.get(x).set(y, elem);
    }

    @Override
    public boolean hasAdjacentElement(T elem, Direction d) {
        int x = elem.getPosition().getX();
        int y = elem.getPosition().getY();
        return hasAdjacentElement(x, y, d);
    }

    @Override
    public boolean hasAdjacentElement(int x, int y, Direction d) {
        return getAdjacentElement(x, y, d) != null;
    }

    @Override
    public boolean hasAdjacentElement(PositionGrille pos, Direction d) {
        return hasAdjacentElement(pos.getX(), pos.getY(), d);
    }

    @Override
    public T getAdjacentElement(T elem, Direction d) {
        PositionGrille p = elem.getPosition();
        return getAdjacentElement(p.getX(), p.getY(), d);
    }

    @Override
    public T getAdjacentElement(int x, int y, Direction d) {
        T answer = null;
        DirectionCarre direction = (DirectionCarre) d;

        switch (direction) {
            case NORD:
                y = y - 1;
                break;
            case SUD:
                y = y + 1;
                break;
            case OUEST:
                x = x - 1;
                break;
            case EST:
                x = x + 1;
                break;
            default:
                throw new NoSuchElementException();
        }
        if (y >= 0 && y < hauteur && x >= 0 && x < largeur) {
            answer = tiles.get(x).get(y);
        }
        // Returns null if we are out of bounds.
        return answer;
    }

    @Override
    public T getAdjacentElement(PositionGrille p, Direction d) {
        return getAdjacentElement(p.getX(), p.getY(), d);
    }
    
    @Override
    public ContexteTableau getContexte(double tailleTuilles) {
        return new ContexteCarre(tailleTuilles);
    }

}
