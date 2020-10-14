/*
 * 
 * Copyright (C) 2015 Éric Wenaas
 * 
 */
package ca.qc.bdeb.hexknight.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe qui encapsule la partie.
 *
 * @author Eric Wenaas <eric.wenaas@bdeb.qc.ca>
 * @version 0.40
 */
public class GestionnairePartie {

    private Partie partie;

    public GestionnairePartie() {
        partie = null;
    }

    public Partie creerPartie(String fichierCarte, long seed) {
        partie = new Partie(fichierCarte, seed);
        return partie;
    }

    public Partie creerPartie(long seed) {
        partie = new Partie(seed);
        return partie;
    }
    
    public Partie chargerLog(File fichierLog) {
        Scanner lectureActions = null;
        LinkedList<String> actions = new LinkedList<>();
        try {
            lectureActions = new Scanner(fichierLog);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GestionnairePartie.class.getName()).log(Level.SEVERE, null, ex);
        }
        while(lectureActions.hasNextLine()) {
            String line = lectureActions.nextLine();
            if (line.startsWith("ACTION-> ")) {
                line = line.substring(9);
                actions.addLast(line);
            }
        }
        return new Partie(actions);
    }

    public void sauverPartie(File fichier) {
        try {
            FileOutputStream fileOut
                    = new FileOutputStream(fichier.getAbsolutePath());
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(partie);
            out.flush();
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public Partie chargerPartie(File fichier) {
        Partie unePartie = null;
        try {
            FileInputStream fileIn = new FileInputStream(fichier.getAbsolutePath());
            ObjectInputStream in = new ObjectInputStream(fileIn);
            unePartie = (Partie) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        }
        return unePartie;
    }    
}
