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
 * @author Eric Wenaas <eric.wenaas@bdeb.qc.ca
 * @param <T>
 */

public class TableauHexagonal<T extends Tuile> implements Grllle<T>, Serializable {
    private final ArrayList<ArrayList<T>> tiles;
    private final int width;
    private final int height;

    /**
     * Creates an empty map HexGrid map of the specified size. The grid model will be built but will only contain empty
     * tiles.
     *
     * @param width
     * @param height
     */
    public TableauHexagonal(int width, int height) {
        this.height = height;
        this.width = width;
        tiles = new ArrayList<>(width);
        for (int i = 0; i < width; i++) {
            tiles.add(new ArrayList<T>(height));
            for (int j = 0; j < height; j++) {
                tiles.get(i).add(null);
            }
        }
    }


    @Override
    public void setElementAt(T tile, int x, int y) {
        tiles.get(x).set(y, tile);
    }

    @Override
    public final void addElement(T elem) {
        int x = elem.getPosition().getX();
        int y = elem.getPosition().getY();
        tiles.get(x).set(y, elem);
    }

    @Override
    public T getElementAt(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return tiles.get(x).get(y);
        }
        return null;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public T getElementAt(PositionGrille p) {
        int x = p.getX();
        int y = p.getY();
        return tiles.get(x).get(y);
    }

    @Override
    public int getHeight() {
        return height;
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
    public T getAdjacentElement(int x, int y, Direction d) {
        T answer = null;
        DirectionHexagonale direction = (DirectionHexagonale) d;

        switch (direction) {
            case N:
                y = y - 1;
                break;
            case NE:
                y = x % 2 == 0 ? y - 1 : y;
                x = x + 1;
                break;
            case SE:
                y = x % 2 == 1 ? y + 1 : y;
                x = x + 1;
                break;
            case S:
                y = y + 1;
                break;
            case SW:
                y = x % 2 == 1 ? y + 1 : y;
                x = x - 1;
                break;
            case NW:
                y = x % 2 == 0 ? y = y - 1 : y;
                x = x - 1;
                break;
            default:
                throw new NoSuchElementException();
        }
        if (y >= 0 && y < height && x >= 0 && x < width) {
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
    public T getAdjacentElement(T elem, Direction d) {
        PositionGrille p = elem.getPosition();
        return getAdjacentElement(p.getX(), p.getY(), d);
    }

    @Override
    public ContexteTableau getContexte(double tailleTuilles) {
        return new ContexteHexagonal(tailleTuilles);
    }
}
