package ca.qc.bdeb.hexknight.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * Classe générique qui permet de gérer des cartes génériques implémentant
 * l'interface Carte. La classe contient un générateur aléatoire.
 * 
 * @author Eric Wenaas <eric.wenaas@bdeb.qc.ca>
 * @version 0.9 (Tests requis)
 * @param <T> Le type de cartes
 */
public class PaquetCartes<T extends Carte> implements Iterable<T>, Serializable {
    
    private final LinkedList<T> paquet;
    
    /**
     * Constructeur par défaut. Le générateur est initialisé à une valeur
     * différente pour chaque instance.
     */
    public PaquetCartes() {
        paquet = new LinkedList<>();
    }
    
    /**
     * Retire la carte au sommet du paquet. La carte est retournée. Lance une exception si la pile est vide.
     * 
     * @return la carte pigée
     */
    public T pigerUneCarte() {
        if (paquet.isEmpty()) {
            throw new NoSuchElementException();
        }
        return paquet.removeFirst();
    }
    
    /**
     * Retire plus d'une carte au sommet du paquet. Les cartes doivent être
     * présentes dans le paquet.
     * 
     * @param nombre Le nombre de cartes à retirer
     * @return La liste des cartes retirées
     */
    public LinkedList<T> pigerPlusieursCartes(int nombre) {
         LinkedList<T> cartesPigees = new LinkedList<>();
         for (int i=0; i<nombre; i++) {
             cartesPigees.addLast(paquet.removeFirst());
         }
         return cartesPigees;
    }
     
    /**
     * Permet de placer une carte sous le paquet.
     * 
     * @param carte 
     */
    public void ajouterDessous(T carte) {
        paquet.addLast(carte);
    }

    /**
     * Permet de placer une carte au-dessus du paquet.
     * 
     * @param carte 
     */
    public void addToTop(T carte) {
        paquet.addFirst(carte);
    }
    
    /**
     * Brasse le paquet de cartes.
     */
    public void brasserCartes(Random generateur) {
        Collections.shuffle(paquet, generateur);
    }
    
    /**
     * Rertourne le nombre de cartes dans le paquet
     * 
     * @return le nombre de cartes
     */
    public int getNombreCartes() {
        return paquet.size();
    }

    @Override
    public Iterator<T> iterator() {
        return paquet.iterator();
    }
}
