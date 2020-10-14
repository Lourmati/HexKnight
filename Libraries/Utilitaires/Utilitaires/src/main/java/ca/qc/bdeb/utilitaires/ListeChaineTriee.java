package ca.qc.bdeb.utilitaires;

import java.util.ListIterator;
import java.util.NoSuchElementException;


public class ListeChaineTriee <T extends Comparable<T>> implements ListeTriee<T>, Iterable <T>{
	private int taille;//La taille de la liste
	Noeud <T> debut;//Le noeud de debut (position 0)
	Noeud <T> fin;//Le noeud de fin (remorque)
	/**
	 * Constructeur par defaut
	 * Initialise le debut et la fin sur la remorque
	 * Taille est mise a 0
	 */
	public ListeChaineTriee(){
		debut = new Noeud <T>();
		fin = debut;
		taille = 0;
	}
	/**
	 * Renvoie false si la taille est 0
	 */
	@Override
	public boolean estVide() {
		return taille == 0;
	}
	/**
	 * Renvoie la taille
	 */
	@Override
	public int getTaille() {
		return taille;
	}
	/**
	 * @param elem	L'element a inserer dans la liste
	 * Insere automatiquement l'element en ordre croissant
	 */
	@Override
	public void inserer(T elem) {
		Noeud <T> nouveau = new Noeud <T>(elem);//Le nouvel element
		if (taille == 0){
			debut = nouveau;
			nouveau.setSuivant(fin);
			fin.setPrecedent(debut);
		} else {
			Noeud <T> curseur = debut;//Le curseur pour trouver la position du nouvel element
			while (curseur != fin && curseur.getElement().compareTo(elem) < 0){
				curseur = curseur.getSuivant();
			}
			if (curseur.getPrecedent() != null){
				nouveau.setPrecedent(curseur.getPrecedent());
				curseur.getPrecedent().setSuivant(nouveau);
			} else {
                            debut = nouveau;
                        }
			curseur.setPrecedent(nouveau);
			nouveau.setSuivant(curseur);
		}
		taille ++;
	}
    /**
     * @param elem	L'element a supprimer de la liste Supprime le noeud du
     * parametre de la liste
     */
    @Override
    public void retirer(T elem) {
        Noeud<T> curseur = null;//Le curseur pour trouver le noeud de l'element en parametre
        curseur = debut;
        while (curseur != fin && curseur.getElement().compareTo(elem) != 0) {
            curseur = curseur.getSuivant();
        }
        if (curseur != fin) {
            if (curseur == debut) {
                debut = curseur.getSuivant();
                debut.setPrecedent(null);
            } else {
                curseur.getSuivant().setPrecedent(curseur.getPrecedent());
                curseur.getPrecedent().setSuivant(curseur.getSuivant());
            }
            taille--;
        }
    }
	/**
	 * @param index La position de l'element dans la liste
	 * @return Renvoie l'element en position index dans la liste
	 */
	@Override
	public T getElement(int index) {
		Noeud <T> curseur = null;//Le curseur pour trouver le noeud
		T reponse = null;//L'element renvoye
		if (index < taille){
			curseur = debut;
			for (int i =0; i < index; i++){
				curseur = curseur.getSuivant();
			}
			reponse = curseur.getElement();
			
		}
		return reponse;
	}
	/**
	 * @return	Iterateur de cette liste
	 */
	@Override
	public  ListIterator <T> iterator() {
		return new Iterateur <T>(this);
	}
	/**
	 * Iterateur de liste chainee
	 * @author Laurent
	 *
	 * @param <T> Type generique recu de la ListeChaineTriee
	 */
	private class Iterateur <T extends Comparable <T>> implements ListIterator<T> {
		
		ListeChaineTriee<T> lct;//Lien vers la liste parente
		Noeud<T> suivant;//Noeud devant le curseur
		Noeud<T> precedent;//Noeud derriere le curseur
		int curseur = 0;//curseur suivant la position
		/**
		 * Constructeur
		 * @param lct	ListeChaineTriee a correspondre avec l'iterateur liste
		 */
		public Iterateur(ListeChaineTriee<T> lct){
			this.lct = lct;
			suivant = this.lct.debut;
			precedent = suivant.getPrecedent();
		}
		/**
		 * @return Renvoie true si un element est devant le curseur
		 */
		@Override
		public boolean hasNext() {
			return suivant != fin;
		}
		/**
		 * @return Renvoie true si un element est derriere le curseur
		 */
		@Override
		public boolean hasPrevious() {
			return precedent != null;
		}
		/**
		 * @return L'element devant le curseur
		 */
		@Override
		public T next() {
			T reponse = null;// L'element renvoye
			if (suivant != fin){
				suivant = suivant.getSuivant();
				precedent = suivant.getPrecedent();
				reponse = precedent.getElement();
				curseur ++;
			} else {
				throw new NoSuchElementException();
			}
			return reponse;
		}
		/**
		 * @return L'index devant le curseur
		 */
		@Override
		public int nextIndex() {
			return curseur;
		}
		/**
		 * @return L'element derriere le curseur
		 */
		@Override
		public T previous() {
			T reponse = null;
			if (precedent != null){
                            suivant = precedent;
                            precedent = precedent.getPrecedent();
                            reponse = suivant.getElement();
                            curseur--;
			} else {
				throw new NoSuchElementException();
			}
			return reponse;
		}
		/**
		 * @return L'index derriere le curseur
		 */
		@Override
		public int previousIndex() {
			return curseur - 1;
		}

		@Override
		public void remove() {
                    throw new UnsupportedOperationException();
		}
		@Override
		public void add(T arg0) {
                    throw new UnsupportedOperationException();
			
		}
		@Override
		public void set(T arg0) {
                    throw new UnsupportedOperationException();
		}
		
	}
	

}
