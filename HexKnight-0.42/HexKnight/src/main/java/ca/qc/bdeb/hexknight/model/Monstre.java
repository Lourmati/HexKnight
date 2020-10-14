/*
 * Copyright (C) 2015 Eric Wenaas
 */
package ca.qc.bdeb.hexknight.model;

import java.io.Serializable;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Classe qui conserve les données des monstres.
 * 
 * @author Eric Wenaas
 * @version 0.1
 */
public class Monstre implements Serializable {

    public enum Type {ORQUE, DRAGON, SOLDAT, MORT_VIVANT, INCONNU};
    
    private final Type typeMonstre;
    private final int attaque;
    private final int defense;
    private final int experience;
    private boolean revele;

    Monstre(Type type, int attaque, int defense, int experience) {
        this.typeMonstre = type;
        this.attaque = attaque;
        this.defense = defense;
        this.experience = experience;
        revele = true;
    }
    
    Monstre(String descriptionType, Element elem) {
        NodeList fils = elem.getChildNodes();
        int taille = fils.getLength();
        int attaqueTemp = 0;
        int defenseTemp = 0;
        int experienceTemp = 0;
        this.typeMonstre = determinerTypeMonstre(descriptionType);
        for (int i=0; i<taille; i++) {
            Node activeNode = fils.item(i);
            switch(activeNode.getNodeName()) {
                case "attack":
                    attaqueTemp = Integer.parseInt(activeNode.getTextContent());
                    break;
                case "defense":
                    defenseTemp = Integer.parseInt(activeNode.getTextContent());
                    break;
                case "points":
                    experienceTemp = Integer.parseInt(activeNode.getTextContent());
                    break;
            }
        }
        this.attaque = attaqueTemp;
        this.defense = defenseTemp;
        this.experience = experienceTemp;
        this.revele = false;
    }
    
    private Type determinerTypeMonstre(String attribute) {
        Type type = null;
        switch (attribute) {
            case ("orque"):
                type = Type.ORQUE;
                break;
            case ("dragon"):
                type = Type.DRAGON; 
                break;
            case ("soldat"):
                type = Type.SOLDAT;
                break;
            case ("mort vivant"):
                type = Type.MORT_VIVANT;
                break;
        }
        return type;
    }
    
    public Type getType() {
        return typeMonstre;
    }
    
    public int getAttaque() {
        return attaque;
    }
    
    public int getDefense() {
        return defense;
    }
    
    public int getExperience() {
        return experience;
    }
    
    public boolean estRevele() {
        return revele;
    }
    
    void revelerMonstre() {
        revele = true;
    }

    boolean estExterieur() {
        return typeMonstre.equals(Type.DRAGON) || typeMonstre.equals(Type.ORQUE);
    }

}
