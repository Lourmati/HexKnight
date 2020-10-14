/*
 * Copyright (C) 2015 Éric Wenaas
 *
 */
package ca.qc.bdeb.hexknight.model;

import ca.qc.bdeb.hexknight.commun.Personnage;
import ca.qc.bdeb.tableaujeu.TableauHexagonal;
import ca.qc.bdeb.tableaujeu.PositionGrille;
import ca.qc.bdeb.hexknight.ai.JoueurArtificiel;
import ca.qc.bdeb.hexknight.commun.Evenement;
import ca.qc.bdeb.hexknight.commun.Message;
import ca.qc.bdeb.hexknight.commun.MomentJour;
import ca.qc.bdeb.hexknight.commun.Observable;
import ca.qc.bdeb.hexknight.commun.Observateur;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Classe de base du modèle.
 *
 * @author Eric Wenaas <eric.wenaas@bdeb.qc.ca>
 * @version 0.40
 */
public final class Partie implements Observable, Serializable {
    private final GestionnaireGrille gestionnaireCarte;
    private final OffreCartesSpeciales cartesSpeciales;
    private List<Joueur> lesJoueurs;
    private transient List<Observateur> observateurs;
    private final Random generateurAleatoire;
    private int indiceJoueurActif;
    private final GestionnaireTours gestionTours;

    /**
     * Constructeur qui fonctionne avec une carte déjà sauvegardée.
     *
     * @param fichierCarte la carte
     * @param germeAleatoire le germe aléatoire
     */
    Partie(String fichierCarte, long germeAleatoire) {
        Logs.ACTION_LOGGER.info("GERME_ALEATOIRE " + germeAleatoire);
        generateurAleatoire = new Random(germeAleatoire);
        gestionnaireCarte = chargerGrille(fichierCarte);
        cartesSpeciales = new OffreCartesSpeciales(generateurAleatoire);
        lesJoueurs = new ArrayList<>();
        observateurs = new LinkedList<>();
        gestionTours = new GestionnaireTours(4);
    }

    /**
     * Constructeur qui génère une carte aléatoire en fonction du germe.
     *
     * @param germeAleatoire le germe aléatoire
     */
    Partie(long germeAleatoire) {
        Logs.ACTION_LOGGER.info("GERME_ALEATOIRE " + germeAleatoire);
        generateurAleatoire = new Random(germeAleatoire);
        gestionnaireCarte = genererGrille();
        cartesSpeciales = new OffreCartesSpeciales(generateurAleatoire);
        lesJoueurs = new ArrayList<>();
        observateurs = new LinkedList<>();
        gestionTours = new GestionnaireTours(4);
    }

    Partie(LinkedList<String> actions) {
        long germeAleatoire = Long.parseLong((actions.pop().split(" ")[1]));
        Logs.ACTION_LOGGER.info("GERME_ALEATOIRE " + germeAleatoire);
        generateurAleatoire = new Random(germeAleatoire);
        gestionnaireCarte = genererGrille();
        cartesSpeciales = new OffreCartesSpeciales(generateurAleatoire);
        lesJoueurs = new ArrayList<>();
        observateurs = new LinkedList<>();
        gestionTours = new GestionnaireTours(4);
        enregistrerJoueurs(actions);
        demarrerPartie();
        appliquerActions(actions);
    }
    
    void appliquerActions(LinkedList<String> actions) {
        for (String action : actions) {
            String[] jetonAction = action.split(" ");
            switch(jetonAction[0]) {
                case "TRANSITION":
                    faireTransitionJoueur();
                    break;
                case "SELECTION_CARTE":
                    selectionCarteLog(jetonAction);
                    break;
                case "SELECTION_TUILE":
                    selectionTuileLog(jetonAction);
                    break;
            }
        }
    }
    
    private void enregistrerJoueurs(LinkedList<String> actions) {
        String action = actions.removeFirst();
        while (action.startsWith("JOUEUR_")) {
            String[] info_joueur = action.split(" ");
            String type_joueur = info_joueur[0];
            String personnnage = info_joueur[1];
            switch(type_joueur) {
                case "JOUEUR_AI":
                    enregistrerJoueurArtificiel(Personnage.valueOf(personnnage));
                    break;
                case "JOUEUR_HUMAIN":
                    enregistrerJoueurHumain(Personnage.valueOf(personnnage));
                    break;
                
            }
            action = actions.removeFirst();
        }
        // On replace la dernière action non traitée
        actions.addFirst(action);
    }


    
    /**
     * Permet de déterminer si c'est la dernière ronde du tour.
     *
     * @return si c'est la dernière ronde du tour
     */
    public boolean isDerniereRonde() {
        return gestionTours.isDerniereRonde();
    }

    final boolean carteDansOffre(CarteJeu carte) {
        return getOffre().contains(carte);
    }

    public void setJoueurTermine(Joueur joueur, boolean value) {
        gestionTours.setJoueurTermine(joueur, value);
//        aviserObservateurs();
    }


    /**
     * Permet de choisir une carte dans l'offre. Cette action est déclenchée quand la
     * carte est sélectionnée.
     *
     * @param indice l'indice de la carte dans l'offre
     * @return la carte sélectionnée
     */
    public CarteJeu obtenirCarteOffre(int indice) {
        return cartesSpeciales.obtenirCarteOffre(indice);
    }

    /**
     * Permet de retirer une carte de l'offre.
     *
     * @param carte
     */
    public void retirerCarteOffre(CarteJeu carte) {
        cartesSpeciales.retirerCarteOffre(carte);
    }

    public MomentJour getPhaseJour() {
        return getNumeroTour() % 2 == 1 ? MomentJour.JOUR : MomentJour.NUIT;
    }

    public boolean isPartieTerminee() {
        return gestionTours.isPartieTerminee();
    }

    private void readObject(ObjectInputStream os) throws IOException, ClassNotFoundException {
        os.defaultReadObject();
        observateurs = new LinkedList<>();
    }

    public int getNumeroTour() {
        return gestionTours.getNumeroTour();
    }

    private GestionnaireGrille chargerGrille(String cheminFichier) {
        GestionnaireGrille nouvelleCarte = new GestionnaireGrille();
        try {
            nouvelleCarte.chargerFichier(cheminFichier, generateurAleatoire);
        } catch (RuntimeException ex) {
            System.err.println("Erreur d'ouverture de fichier");
            nouvelleCarte = genererGrille();
        }
        return nouvelleCarte;
    }
    
    private GestionnaireGrille genererGrille() {
        GestionnaireGrille nouvelleCarte = new GestionnaireGrille();
        nouvelleCarte.genererCarte(generateurAleatoire);
        return nouvelleCarte;        
    }
    
    public void sauverCarte(File fichier) {
       gestionnaireCarte.sauvegarder(fichier);
    }

    public TableauHexagonal<TuileJeu> getGrille() {
        return gestionnaireCarte.getCarte();
    }

    public List<Joueur> getJoueurs() {
        return lesJoueurs;
    }

    public void enregistrerJoueurHumain(Personnage personnage) {
        Logs.ACTION_LOGGER.info("JOUEUR_HUMAIN " + personnage.toString());
        lesJoueurs.add(new JoueurHumain(this, personnage));
    }

    public void enregistrerJoueurArtificiel(Personnage personnage) {
        Logs.ACTION_LOGGER.info("JOUEUR_AI " + personnage.toString());
        lesJoueurs.add(new JoueurArtificiel(this, personnage));
    }

    public List<CarteJeu> getOffre() {
        return cartesSpeciales.getOffre();
    }

    public void demarrerPartie() {
        PositionGrille positionPortail = gestionnaireCarte.getPositionPortail();
        for (Joueur joueur : lesJoueurs) {
            joueur.setPosition(positionPortail);
            joueur.brasserCarte(generateurAleatoire);
            gestionTours.setJoueurTermine(joueur, false);
            gestionnaireCarte.appliquerVisibiliteGrille(this, joueur.getPosition());
        }
        // On détermine l'ordre des joueurs
        List<Joueur> joueursEnOrdre = new ArrayList<>(3);
        while (! lesJoueurs.isEmpty()) {
            int numeroJoueur = generateurAleatoire.nextInt(lesJoueurs.size());
            joueursEnOrdre.add(lesJoueurs.remove(numeroJoueur));
        }
        lesJoueurs = joueursEnOrdre;
        indiceJoueurActif = 0;
        for (Joueur joueur : lesJoueurs) {
            joueur.getEtatRonde().initialiser();
        }
//        prochainePhase();
    }

    public Joueur getJoueurActif() {
        return lesJoueurs.get(indiceJoueurActif);
    }

    private void activerProchainJoueur() {
        indiceJoueurActif = (indiceJoueurActif + 1) % lesJoueurs.size();
        getJoueurActif().faireTransition();

    }

    public String getEtatRound() {
        return getJoueurActif().getEtatRonde().getNom();
    }
    
    public boolean estPretAChanger() {
        return getJoueurActif().getEtatRonde().pretAChanger();
    }
    
    public void selectionnerCarte(CarteJeu carte) {
        List<CarteJeu> mainJoueur = getJoueurActif().getMain();
        EtatRonde etatJoueur = getJoueurActif().getEtatRonde();
        ZoneActivationCarte endroit = null;
        if (mainJoueur.contains(carte)) {
            if (etatJoueur.etatPermetCarte(carte)) {
                etatJoueur.actionJouerCarte(carte);
            }
            endroit = ZoneActivationCarte.MAIN;
        } else if (etatJoueur.getCartesEnJeu().contains(carte)) {
            if (etatJoueur.peutRetirerCarteEnJeu(carte)) {
                etatJoueur.actionRetirerCarte(carte);
            }
            endroit = ZoneActivationCarte.JEU;
        } else if (carteDansOffre(carte)) {
            if (etatJoueur.etatPermetCarte(carte)) {
                etatJoueur.actionJouerCarte(carte);
            }
            endroit = ZoneActivationCarte.OFFRE;
        }
        String chaine = endroit != null ? endroit.toString():"";
        Logs.ACTION_LOGGER.info("SELECTION_CARTE " + chaine + " " + carte.getType());
        aviserObservateurs();
        getJoueurActif().aviserObservateurs();
    }
    
    public void selectionnerTuile(PositionGrille pos) {
        Logs.ACTION_LOGGER.info("SELECTION_TUILE " + pos.getX() + " " + pos.getY());
        getJoueurActif().tuileSelectionnee(pos);
        aviserObservateurs();
        getJoueurActif().aviserObservateurs();
    }

    public void declarerDerniereRonde() {
        gestionTours.declarerDerniereRonde();
    }
        
    public Message getMessage() {
        return getJoueurActif().getState().getMessage();
    }

    public synchronized void prochainePhase() {
        getJoueurActif().faireTransition();
        
        // TODO: getJoueurActif.isActif() ??? Serait difficile d'être moins clair
        //       car parfois c'est étonnement faux... l'idée est que le joueur
        //       peut terminer son tour après la transition. Il faudrait contrôler
        //       ça autrement.
        
        if (!getJoueurActif().isActif()) {
            if (isDerniereRonde()) {
                setJoueurTermine(getJoueurActif(), true);
            }
            if (gestionTours.isTourTermine()) {
                passerAuProchainTour();
            }
            if (isPartieTerminee()) {
                for (Joueur j : lesJoueurs) {
                    j.setState(new EtatTermine(j));
                    envoyerMessageFinPartie(j);
                }
            } else {
                activerProchainJoueur();
            }
        }

        while (getJoueurActif().estJoueurAutomatise()) {
            getJoueurActif().faireTransition();
            if (!getJoueurActif().isActif()) {
                if (isDerniereRonde()) {
                    setJoueurTermine(getJoueurActif(), true);
                }

                if (gestionTours.isTourTermine()) {
                    passerAuProchainTour();
                }
                activerProchainJoueur();
            }
        }
        aviserObservateurs();
        getJoueurActif().aviserObservateurs();

    }

    void passerAuProchainTour() {
        gestionTours.prochainTour();
        if (! gestionTours.isPartieTerminee()) {
            for (Joueur joueur : lesJoueurs) {
                joueur.preparerProchainTour(generateurAleatoire);
                getGestionnaireGrille().appliquerVisibiliteGrille(this, joueur.getPosition());
                gestionTours.setJoueurTermine(joueur, false);
                //TODO: Patch car les cartes dans l'état Attente étaient pigées avant d'être brassées.
                //      Les deux appels à initialiser sont très étranges.
                joueur.getState().initialiser();
                joueur.getState().initialiser();
            }
        }
        HexKnightUtils.revelerMonstreChateauAdjacent(this);
    }

    Joueur getJoueur(Personnage personnage) {
        Joueur joueur = null;
        for (Joueur j : lesJoueurs) {
            if (j.getPersonnage().equals(personnage)) {
                joueur = j;
                break;
            }
        }
        return joueur;
    }

    public GestionnaireGrille getGestionnaireGrille() {
        return gestionnaireCarte;
    }

    public HashMap<PositionGrille, Integer> getDestinations() {
        return getJoueurActif().getDestinations();
    }

    @Override
    public void ajouterObservateur(Observateur ob) {
        observateurs.add(ob);
    }

    @Override
    public void retirerObservateur(Observateur ob) {
        observateurs.remove(ob);
    }

    @Override
    public void aviserObservateurs() {
        for (Observateur ob : observateurs) {
            ob.changementEtat();
        }
    }

    @Override
    public void aviserObservateurs(Enum<?> propriete, Object o) {
        for (Observateur ob : observateurs) {
            ob.changementEtat(propriete, o);
        }
    }

    private void envoyerMessageFinPartie(Joueur destinataire) {
        for (Joueur j: lesJoueurs) {
            j.aviserObservateurs();
        }
        aviserObservateurs();
        aviserObservateurs(Evenement.PARTIE_TERMINEE, destinataire);
    }

    public List<Joueur> getAdversaires(Joueur joueurConcerne) {
        List<Joueur> adversaires = new LinkedList<>();
        for (Joueur joueur : lesJoueurs) {
            if (joueur !=  joueurConcerne) {
                adversaires.add(joueur);
            }
        }
        return  adversaires;
    }
    
    public HashMap<PositionGrille, Integer> getVolOiseau() {
        return gestionnaireCarte.appliquerDijkstra(getJoueurActif().getPosition(), new EvaluateurDeplacementVolOiseau()).destinations;
    }

    public int getValeurAccumulee() {
        return getJoueurActif().getValeurAccumulee();
    }

    public Integer getValeurCible() {
        return getJoueurActif().getEtatRonde().getValeurCible();        
    }

   List<CarteJeu> getCartesEnJeu() {
        return getJoueurActif().getEtatRonde().getCartesEnJeu();
    }

    private void faireTransitionJoueur() {
        getJoueurActif().getState().executer();
        EtatRonde nouvelEtat = getJoueurActif().getState().obtenirProchainEtat();
        getJoueurActif().setState(nouvelEtat);
        Logs.ACTION_LOGGER.info("TRANSITION");
        nouvelEtat.initialiser();
        if (!getJoueurActif().isActif()) {
            if (isDerniereRonde()) {
                setJoueurTermine(getJoueurActif(), true);
            }
            if (gestionTours.isTourTermine()) {
                passerAuProchainTour();
            }
            if (isPartieTerminee()) {
                for (Joueur j : lesJoueurs) {
                    j.setState(new EtatTermine(j));
                    envoyerMessageFinPartie(j);
                }
            }
            indiceJoueurActif = (indiceJoueurActif + 1) % lesJoueurs.size();
        }    
    }

    private void selectionCarteLog(String[] action) {
        ZoneActivationCarte endroit = ZoneActivationCarte.valueOf(action[1]);
        String nomCarte = action[2];
        CarteJeu carte = null;
        switch (endroit) {
            case MAIN:
                carte = trouverCarteDansListe(getJoueurActif().getMain(), nomCarte);
                break;
            case JEU:
                carte = trouverCarteDansListe(getJoueurActif().getCartesEnJeu(), nomCarte);
                break;
            case OFFRE:
                carte = trouverCarteDansListe(cartesSpeciales.getOffre(), nomCarte);
                break;
        }
        selectionnerCarte(carte);
    }

    private void selectionTuileLog(String[] action) {
        Integer x = Integer.parseInt(action[1]);
        Integer y = Integer.parseInt(action[2]);
        selectionnerTuile(new PositionGrille(x, y));
        
    }

    private CarteJeu trouverCarteDansListe(List<CarteJeu> liste, String nomCarte) {
        CarteJeu carteTrouve = null;
        
        for (CarteJeu carte : liste) {
            if (carte.getType().toString().equals(nomCarte)) {
                carteTrouve = carte;
                break;
            }
        }
        return carteTrouve;
    }
}
