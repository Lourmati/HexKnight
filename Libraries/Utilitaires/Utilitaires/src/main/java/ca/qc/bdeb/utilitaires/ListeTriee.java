package ca.qc.bdeb.utilitaires;

import java.util.ListIterator;

public interface ListeTriee<T extends Comparable<T>> extends Iterable<T> {

    boolean estVide();

    int getTaille();

    void inserer(T elem);

    void retirer(T elem);
    
    T getElement(int index);
    
    @Override
    ListIterator<T> iterator();
}
