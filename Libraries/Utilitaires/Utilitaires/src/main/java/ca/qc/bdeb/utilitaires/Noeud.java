package ca.qc.bdeb.utilitaires;

public class Noeud <T>{
	private T element;//l'element contenu dans le noeud
	private Noeud <T> suivant;//Noeud dont le precedent pointe sur ce noeud
	private Noeud <T> precedent;//Le noeud qui a pointe son suivant sur ce noeud
	/**
	 * Constructeur par defaut
	 * Initialise les trois variables a null
	 */
	public Noeud (){
		this.element = null;
		this.suivant = null;
		this.precedent = null;
	}
	/**
	 * 
	 * @param elem Initialise element a cette valeur
	 */
	public Noeud (T elem){
		this.element = elem;
		this.suivant = null;
		this.precedent = null;
	}
	/**
	 * 
	 * @param elem Attribue la valeur elem a element
	 */
	public void setElement (T elem){
		this.element = elem;
	}
	/**
	 * 
	 * @param prec Attribue le noeud a precedent
	 */
	public void setPrecedent (Noeud <T> prec){
		this.precedent = prec;
	}
	/**
	 * 
	 * @param suiv Attribue le noeud a suivant
	 */
	public void setSuivant (Noeud <T> suiv){
		this.suivant = suiv;
	}
	/**
	 * 
	 * @return Renvoie element
	 */
	public T getElement(){
		return element;
	}
	/**
	 * 
	 * @return	Renvoie precedent
	 */
	public Noeud <T> getPrecedent(){
		return precedent;
	}
	/**
	 * 
	 * @return Renvoie suivant
	 */
	public Noeud <T> getSuivant(){
		return suivant;
	}
} 
