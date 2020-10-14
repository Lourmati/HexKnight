package ca.qc.bdeb.utilitaires;

import java.io.Serializable;

/**
 * Represents a position in a 2D environment. This class is immutable.
 * 
 * @version 1.0
 * 
 */

public class Position2D implements Serializable
{

    
    private static final long serialVersionUID = 1L;


    public final int X; // Position in X
    public final int Y; // Position in Y

    /**
     * Creates a new Position
     * 
     * @param x
     * @param y
     */
    public Position2D(int x, int y)
    {
        X = x;
        Y = y;
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Position2D))
        {
            throw new RuntimeException();
        }
        Position2D pos = (Position2D) o;
        return pos.X == X && pos.Y == Y;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 89 * hash + this.X;
        hash = 89 * hash + this.Y;
        return hash;
    }

    /**
     * Returns the position in the specified OrthogonalDirection
     * 
     * @param d The direction
     * @return The new position
     */
    public Position2D getNeighbor(Direction d)
    {
        int x = X;
        int y = Y;

        switch (d)
        {
        case HAUT:
            --y;
            break;
        case BAS:
            ++y;
            break;
        case GAUCHE:
            --x;
            break;
        case DROIT:
            ++x;
            break;
        }
        return new Position2D(x, y);
    }
    
    @Override
    public String toString() {
        return "X:" + X + "-Y:" + Y;
    }
    
    public static Position2D parsePosition2D(String token) {
        Position2D answer = null;
        if (!token.equals("null")) {
            int x = Integer.parseInt(token.substring(2, 3));
            int y = Integer.parseInt(token.substring(6, 7));
            answer = new Position2D(x, y);
        }
        return answer;

    }

}
