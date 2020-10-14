/*
 * 
 * Copyright (C) 2015 Éric Wenaas
 * 
 */
package ca.qc.bdeb.hexknight.model;

import ca.qc.bdeb.hexknight.ai.JoueurArtificiel;
import ca.qc.bdeb.tableaujeu.PositionGrille;
import ca.qc.bdeb.hexknight.commun.Personnage;
import ca.qc.bdeb.hexknight.commun.InfosJoueur;
import ca.qc.bdeb.hexknight.commun.Observable;
import ca.qc.bdeb.hexknight.commun.Observateur;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Eric Wenaas
 */
public abstract class Joueur implements Observable, Serializable {

    private final Personnage personnage;
    private PaquetCartes<CarteJeu> paquet;
    private final List<CarteJeu> cartesEnMain;
    private final List<CarteJeu> cartesDiscartes;
    private final Experience gestionExperience;
    private PositionGrille positionSurCarte;
    private transient LinkedList<Observateur> observateurs;
    private Partie partie;
    private EtatRonde etat;
    private boolean actif;
    private boolean faitAction;
    private int nombreRuines;
    private boolean selectionOffreRequise;

    public Joueur(Partie partie, Personnage personnage) {
        this.partie = partie;
        this.personnage = personnage;
        gestionExperience = new Experience();
        cartesEnMain = new LinkedList<>();
        cartesDiscartes = new LinkedList<>();
        observateurs = new LinkedList<>();
        actif = false;
        faitAction = false;
        nombreRuines = 0;
        selectionOffreRequise = false;
        construirePaquet();
    }

    private void construirePaquet() {
        paquet = new PaquetCartes<>();
        for (int i = 0; i < 4; i++) {
            paquet.addToTop(CarteJeu.construireCarteJeu(TypeCarte.ATTAQUE_SURPRISE));
            paquet.addToTop(CarteJeu.construireCarteJeu(TypeCarte.CHEMIN_SECRET));
            paquet.addToTop(CarteJeu.construireCarteJeu(TypeCarte.CRI_GUERRE));
            paquet.addToTop(CarteJeu.construireCarteJeu(TypeCarte.PROTECTION));
        }
    }

    boolean isActif() {
        return actif;
    }

    void setActif(boolean actif) {
        this.actif = actif;
    }

    int getNombreCartesEnMain() {
        return cartesEnMain.size();
    }

    void setPartie(Partie unePartie) {
        partie = unePartie;
    }

    protected void setState(EtatRonde unEtat) {
        etat = unEtat;
    }

    public List<CarteJeu> getMain() {
        List<CarteJeu> copie = new LinkedList<>();
        for (CarteJeu carte : cartesEnMain) {
            copie.add(carte);
        }
        return copie;
    }

    public abstract boolean estJoueurAutomatise();

    public Partie getPartie() {
        return partie;
    }
    
    void retournerCarte(CarteJeu carte) {
        cartesEnMain.remove(carte);
    }


    protected void brasserCarte(Random generateur) {
        for (CarteJeu carte : cartesDiscartes) {
            paquet.ajouterDessous(carte);
        }
        for (CarteJeu carte : cartesEnMain) {
            paquet.ajouterDessous(carte);
        }
        cartesDiscartes.clear();
        cartesEnMain.clear();
        paquet.brasserCartes(generateur);
    }

    public boolean peutPiger() {
        return paquet.getNombreCartes() > 0;
    }

    public CarteJeu pigerDansMain() {
        CarteJeu carte = paquet.pigerUneCarte();
        cartesEnMain.add(carte);
        return carte;
    }

    public int getNiveauArmure() {
        return gestionExperience.getNiveauArmure();
    }

    public int getTailleMain() {
        return gestionExperience.getTailleMain();
    }
    
    public int getNombreDiscartes() {
        return cartesDiscartes.size();
    }

    public Personnage getPersonnage() {
        return personnage;
    }

    private int getExperience() {
        return gestionExperience.getExperience();
    }

    public int getProchainNiveau() {
        return gestionExperience.getProchainNiveau();
    }

    public PositionGrille getPosition() {
        return positionSurCarte;
    }

    public void setPosition(PositionGrille pos) {
        positionSurCarte = pos;
    }
    
    public boolean reposPossible() {
        int nombreBlessures = HexKnightUtils.compterBlessures(cartesEnMain);
        int nombreCartes = cartesEnMain.size();
        TuileJeu tuile = getPartie().getGrille().getElementAt(getPosition());
        boolean guerisonPossible = tuile.obtenirAjoutGuerison() > 0;
        boolean possible = false;
        if (nombreBlessures > 0 && guerisonPossible) {
            possible = true;
        } else if (nombreCartes > nombreBlessures) {
            possible = true;
        } 
        return possible;
    }

    void ajouterExperience(int exp) {
        int niveauAvant = getNiveauExperience();
        gestionExperience.ajouterExperience(exp);
        int niveauApres = getNiveauExperience();
        if (niveauApres > niveauAvant) {
            selectionOffreRequise = niveauApres % 2 == 1;
        }
    }

    protected void faireTransition() {
        StringBuilder log = new StringBuilder("TRANSITION ");
        log.append(etat.getClass().getSimpleName());
        log.append(" ");
        if (estJoueurAutomatise()) {
            ((JoueurArtificiel) this).getAnalyse().appliquerActions(etat);
        }
        etat.executer();
        EtatRonde nouvelEtat = etat.obtenirProchainEtat();
        etat = nouvelEtat;
        log.append(etat.getClass().getSimpleName());
        Logs.ACTION_LOGGER.info(log.toString());
        nouvelEtat.initialiser();
    }

    public int getTaillePaquet() {
        return paquet.getNombreCartes();
    }

    protected void retirerCarteMain(CarteJeu carte) {
        cartesEnMain.remove(carte);
    }

    protected int ajouterCarteMain(CarteJeu carte) {
        cartesEnMain.add(carte);
        return cartesEnMain.size() - 1;
    }

    protected void discarter(CarteJeu carte) {
        cartesDiscartes.add(carte);
    }

    public int calculerPoints() {
        int points = gestionExperience.getExperience();
        points += getAjustementBlessures();
        points += getAjustementCartes();
        points += nombreRuines;
        return points;
    }
    
    private int compterBlessures() {
        int nbBlessures = HexKnightUtils.compterBlessures(cartesEnMain);
        nbBlessures +=  HexKnightUtils.compterBlessures(cartesDiscartes);
        nbBlessures +=  HexKnightUtils.compterBlessures(paquet.iterator());
        nbBlessures +=  HexKnightUtils.compterBlessures(etat.getCartesEnJeu());
        return nbBlessures;
    }
    
    void incrementerNombreRuines() {
        nombreRuines++;
    }

    int getNiveauExperience() {
        return gestionExperience.getNiveauExperience();
    }

    protected void ajouterCarteSommetPaquet(CarteJeu carte) {
        paquet.addToTop(carte);
    }

    EtatRonde getEtatRonde() {
        return etat;
    }

    public int getValeurAccumulee() {
        return etat.getValeurAccumulee();
    }

    public HashMap<PositionGrille, Integer> getDestinations() {
        return etat.actionCalculerDestinations();
    }

    public void tuileSelectionnee(PositionGrille pos) {
        etat.actionSelectionnerTuile(pos);
    }

    protected PaquetCartes<CarteJeu> getPaquet() {
        return paquet;
    }

    protected void preparerProchainTour(Random generateur) {
        brasserCarte(generateur);
    }

    @Override
    public void ajouterObservateur(Observateur ob) {
        observateurs.add(ob);
    }

    @Override
    public void retirerObservateur(Observateur ob) {
        observateurs.remove(ob);
    }

    private void readObject(ObjectInputStream os) throws IOException, ClassNotFoundException {
        os.defaultReadObject();
        observateurs = new LinkedList<>();
    }

    @Override
    public void aviserObservateurs() {
        for (Observateur ob : observateurs) {
            ob.changementEtat();
        }
    }

    @Override
    public void aviserObservateurs(Enum<?> propriete, Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    EtatRonde getState() {
        return etat;
    }

    public boolean getFaitAction() {
        return faitAction;
    }

    void setFaitAction(boolean value) {
        faitAction = value;
    }

    public List<CarteJeu> getCartesEnJeu() {
        return etat.getCartesEnJeu();
    }

    private int getAjustementBlessures() {
        int nombreBlessures = compterBlessures();
        return nombreBlessures * -2;
    }

    private int getAjustementCartes() {
        int nombreTotal = cartesEnMain.size() + cartesDiscartes.size() +
                paquet.getNombreCartes() + etat.getCartesEnJeu().size();
        int nombreBlessures = compterBlessures();
        return nombreTotal - nombreBlessures - 16;
    }

    public int getNombreRuines() {
        return nombreRuines;
    }

    public int getTailleDefausse() {
        return cartesDiscartes.size();
    }

    public InfosJoueur getInfosJoueur() {
        InfosJoueur infos = new InfosJoueur();
        infos.nomPersonnage = getPersonnage().getNom();
        infos.ajustementBlessures = getAjustementBlessures();
        infos.ajustementCartes = getAjustementCartes();
        infos.carteEnMains = cartesEnMain.size();
        infos.experience = getExperience();
        infos.niveauArmure = getNiveauArmure();
        infos.nombreRuines = nombreRuines;
        infos.points = calculerPoints();
        infos.prochainNiveau = getProchainNiveau();
        infos.tailleMain = getTailleMain();
        infos.taillePaquet = paquet.getNombreCartes();
        return infos;
    }

    boolean doitChoisirOffre() {
        return selectionOffreRequise;
    }

    void setSelectionOffreRequise(boolean b) {
        selectionOffreRequise = b;
    }
}
