package ca.qc.bdeb.tableaujeu;

/**
 * Une interface qui décrit le comportement de base d'une grille
 *
 * @author Eric Wenaas
 * @param <T>
 */

public interface Grllle<T extends Tuile> {
    
    /**
     * Returns the element in the position. Returns null if there is no such
     * element.
     *
     * @param p the position
     * @return the element
     */
    T getElementAt(PositionGrille p);

    /**
     * Returns the element in the x, y position.  If the element has not been set by
     * addElement then null will be returned.

     * @param x position on the x-axis
     * @param y position on the y-axis
     * @return the element
     */
    T getElementAt(int x, int y);
    
    
    /**
     * Mets la tuile en position x, y.
     * 
     * @param tile la nouvelle tuile
     * @param x poisiton en x
     * @param y position en y
     */
    void setElementAt(T tile, int x, int y);

    /**
     * The height is the amount of elements on the y-axis
     *
     * @return The height of the Grid
     */
    int getHeight();

    /**
     * The width is the amount of elements on the x-axis.
     *
     * @return The width of the Grid
     */
    int getWidth();

    /**
     * This method has to be used in order to have an element placed in the grid.
     * The element should contain the position since it is locatable.
     *
     * @param elem The element
     */
    void addElement(T elem);


    boolean hasAdjacentElement(T elem, Direction d);
    boolean hasAdjacentElement(int x, int y, Direction d);
    boolean hasAdjacentElement(PositionGrille p, Direction d);

    /**
     * Returns the adjacent element in the Direction d
     *
     * @param elem The initial element
     * @param d the Directrion
     * @return the element in the direction
     * @throws NoSuchElementException
     */
    T getAdjacentElement(T elem, Direction d);
    T getAdjacentElement(int x, int y, Direction d);
    T getAdjacentElement(PositionGrille p, Direction d);
    
    /**
     * Retourne le contexte de la carte en fonction de sa taille. Le contexte inclut le type de la carte ainsi que
     * la taille des tuiles.
     * 
     * @return 
     */
    // TODO: Rien à faire dans l'interface...
    ContexteTableau getContexte(double tailleTuiles);
}
