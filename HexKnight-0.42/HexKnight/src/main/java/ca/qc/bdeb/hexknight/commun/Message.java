/*
 * 
 * Copyright (C) 2018 Éric Wenaas
 * 
 */
package ca.qc.bdeb.hexknight.commun;

/**
 * Classe qui est utilisér pour transmettre des informations à l'interface.
 * 
 * @author Eric Wenaas <eric.wenaas@bdeb.qc.ca>
 * @version 0.40
 */
public enum Message {
    
    
    VIDE("", false),
    VICTOIRE_POSSIBLE("Voulez-vous vraiment ne pas battre ce monstre !!!", true),
    PERTE_MOUVEMENT("Vous allez perdre des points de mouvement !!!", true),
    PERTE_GUERISON("Vous allez perdre des points de guérison", true),
    BLESSURES("Vous allez avoir %d blessures !!!", true), 
    SELECTION_REQUISE("Vous devez choisir une carte dans l'offre", false),
    PARTIE_TERMINEE("La partie est terminée", false);
    
    private final String message;
    private int valeur; 
    private final boolean confirmationRequise;
    
    private Message(String m, boolean confirmation) {
        message  = m;
        valeur = -1;
        confirmationRequise = confirmation;
        
    }
    
    public void setValeur(int valeur) {
        this.valeur = valeur;
    }
    
    public boolean isConfirmationRequise() {
        return confirmationRequise;
    }
    
    public String getDescription() {
        String reponse = message;
        if (valeur != -1) {
            reponse = String.format(message, valeur);
        }        
        valeur = -1;
        return reponse;
    }
    
}
