/*
 * 
 * Copyright (C) 2015 Éric Wenaas
 * 
 */
package ca.qc.bdeb.hexknight.ai;

import ca.qc.bdeb.hexknight.commun.IDEtat;
import ca.qc.bdeb.hexknight.commun.MomentJour;
import ca.qc.bdeb.tableaujeu.TableauHexagonal;
import ca.qc.bdeb.tableaujeu.DirectionHexagonale;
import ca.qc.bdeb.tableaujeu.PositionGrille;
import ca.qc.bdeb.hexknight.commun.TypeAjoutTuile;
import ca.qc.bdeb.hexknight.model.CarteJeu;
import ca.qc.bdeb.hexknight.model.EtatAttaque;
import ca.qc.bdeb.hexknight.model.EtatAttente;
import ca.qc.bdeb.hexknight.model.EtatDefense;
import ca.qc.bdeb.hexknight.model.EtatDeplacement;
import ca.qc.bdeb.hexknight.model.EtatPreparation;
import ca.qc.bdeb.hexknight.model.EtatProvoque;
import ca.qc.bdeb.hexknight.model.EtatRepos;
import ca.qc.bdeb.hexknight.model.EtatRonde;
import ca.qc.bdeb.hexknight.model.EtatSelectionCarte;
import ca.qc.bdeb.hexknight.model.EvaluateurDeplacement;
import ca.qc.bdeb.hexknight.model.EvaluateurDeplacementJour;
import ca.qc.bdeb.hexknight.model.EvaluateurDeplacementNuit;
import ca.qc.bdeb.hexknight.model.EvaluateurDeplacementVolOiseau;
import ca.qc.bdeb.hexknight.model.HexKnightUtils;
import ca.qc.bdeb.hexknight.model.Logs;
import ca.qc.bdeb.hexknight.model.Monstre;
import ca.qc.bdeb.hexknight.model.TuileJeu;
import ca.qc.bdeb.hexknight.model.TypeCarte;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author Eric Wenaas <eric.wenaas@bdeb.qc.ca>
 * @version 0.1
 */
public class Analyse implements Serializable {
    
    // Pour l'évaluation globale d'un objectif
    private static final double FACTEUR_EXPERIENCE = 1.0;
    private static final double FACTEUR_GUERISON = 0.5;
    private static final double FACTEUR_BLESSURES = -0.3;
    private static final double FACTEUR_EMPLACEMENT = 0.2;
    private static final double FACTEUR_CARTES_RESTANTES = 0.5;
    private static final double FACTEUR_EMPLACEMENT_ADV = 0.05;

    // Pour l'évaluation d'un emplacement
    private static final double VALEUR_VILLAGE = 0.01;
    private static final double VALEUR_MONASTERE = 0.01;
    private static final double VALEUR_CHATEAU = 0.02;
    private static final double VALEUR_NON_REVELEE = 0.3;

    // Le pourcentage maximal de blessures qu'on accepte
    private static final double SEUIL_BLESSURES = 0.5;

    private final JoueurArtificiel ai;
    HashMap<String, Integer> capacites;
    ListeDestination destinations;
    ArrayList<List<CarteJeu>> permutations;
    private ObjectifAI objectifRetenu;

    Analyse(JoueurArtificiel joueur) {
        ai = joueur;
    }

    /* Cette fonction regarde quel est le maximum qu'on peut atteindre pour
     * chacun des éléments.
     */
    void evaluerMain() {
        List<CarteJeu> main = ai.getMain();
        capacites = new HashMap<>();
        capacites.put("Attaque", 0);
        capacites.put("Defense", 0);
        capacites.put("Vitesse", 0);
        capacites.put("Meditation", 0);

        for (CarteJeu carte : main) {
            ajouterValeur(capacites, "Attaque", carte.getAttaque());
            ajouterValeur(capacites, "Defense", carte.getAttaque());
            ajouterValeur(capacites, "Meditation", carte.getAttaque());
            ajouterValeur(capacites, "Vitesse", carte.getAttaque());
        }
    }

    ObjectifAI getObjectifRetenu() {
        return objectifRetenu;
    }

    private void ajouterValeur(HashMap<String, Integer> capacite, String chaine, int ajout) {
        int valeurInitiale = capacite.get(chaine);
        valeurInitiale += ajout;
        capacite.put(chaine, valeurInitiale);
    }

    void calculerDestinations() {
        destinations = new ListeDestination(ai);
    }

    // L'algorithme utilisé pour générer les permutations est celui de Heap.
    // http://en.wikipedia.org/wiki/Heap%27s_algorithm
    void genererPermutations() {
        permutations = new ArrayList<>();
        List<CarteJeu> permutationInitiale = new ArrayList<>(ai.getMain().size());
        for (CarteJeu carte : ai.getMain()) {
            permutationInitiale.add(carte);
        }
        // On commence par enlever les blessures qui ne servent à rien avant de calculer
        // les permutations.
        ListIterator<CarteJeu> iter = permutationInitiale.listIterator();
        while (iter.hasNext()) {
            CarteJeu carte = iter.next();
            if (carte.getType().equals(TypeCarte.BLESSURE)) {
                iter.previous();
                iter.remove();
            }
        }
        calculerPermutationsRecursif(permutationInitiale.size(), permutationInitiale);
    }

    private void calculerPermutationsRecursif(int indice, List<CarteJeu> permutationInitiale) {
        if (indice == 1) {
            LinkedList<CarteJeu> nouvellePermutation = new LinkedList<>();
            for (CarteJeu carte : permutationInitiale) {
                nouvellePermutation.add(carte);
            }
            permutations.add(nouvellePermutation);
        } else {
            for (int i = 0; i < indice; i++) {
                calculerPermutationsRecursif(indice - 1, permutationInitiale);
                int j;
                if (indice % 2 == 1) {
                    j = 1;
                } else {
                    j = i;
                }
                // SWAP des deux cartes.
                CarteJeu temp = permutationInitiale.get(j);
                permutationInitiale.set(j, permutationInitiale.get(indice - 1));
                permutationInitiale.set(indice - 1, temp);
            }
        }
    }

    private double evaluerTuile(TuileJeu tuile) {        
        HashMap<PositionGrille, Integer> distances = obtenirDistancesVolOiseau(tuile);        
        double valeur = evaluerTuilesAdjacentesNonRevelees(tuile);
        valeur += evaluerTuilesRevelees(distances);
        return valeur / distances.size();
    }
    
    private HashMap<PositionGrille, Integer> obtenirDistancesVolOiseau(TuileJeu tuile) {
        return ai.getPartie().getGestionnaireGrille().appliquerDijkstra(tuile.getPosition(),
                        new EvaluateurDeplacementVolOiseau()).destinations;    
    }
    
    private double evaluerTuilesAdjacentesNonRevelees(TuileJeu tuile) {
        double valeur = 0.0;
        for (DirectionHexagonale d : DirectionHexagonale.values()) {
            TuileJeu tuileVoisine = ai.getPartie().getGrille().getAdjacentElement(tuile, d);
            if (!tuileVoisine.estRevelee()) {
                valeur += VALEUR_NON_REVELEE;
            }
        }
        return valeur;
    }
    
    private double evaluerTuilesRevelees(HashMap<PositionGrille, Integer> distances) {
        double valeur = 0.0;
        TableauHexagonal<TuileJeu> carte = ai.getPartie().getGrille();
        for (PositionGrille pos : distances.keySet()) {
            TuileJeu tuileDistante = carte.getElementAt(pos);
            assert(tuileDistante.estRevelee());
            double valeurTuile = ajouterValeurMonstre(tuileDistante);
            valeurTuile += ajouterValeurAjout(tuileDistante);
            valeur += appliquerFacteurDistanceTuile(valeurTuile, distances.get(pos));
        }
        return valeur;
    }
    
    private double ajouterValeurMonstre(TuileJeu tuileDistante) {
        double valeur = 0.0;
        if (tuileDistante.contientMonstre()) {
                valeur = tuileDistante.getMonstre().getExperience();
        }
        return valeur;   
    }
    
    private double ajouterValeurAjout(TuileJeu tuileDistante) {
        double valeur = 0.0;
        switch (tuileDistante.getAjout()) {
            case CHATEAU:
                valeur += VALEUR_CHATEAU;
                break;
            case MONASTERE:
                valeur += VALEUR_MONASTERE;
                break;
            case VILLAGE:
                valeur += VALEUR_VILLAGE;
        }
        return valeur;
    }
    
    private double appliquerFacteurDistanceTuile(double valeurInitiale, int distanceTuile) {
        if (distanceTuile == 0) {
            distanceTuile = 1;
        }
        return valeurInitiale / distanceTuile;
    }

    int getCoutDeplacement(TuileJeu tuile) {
        return destinations.getCoutDeplacement(tuile);
    }

    void analyserObjectifs(boolean actionPosee) {
        LinkedList<ObjectifAI> objectifsConsideres = genererObjectifsSansGuerison(actionPosee);
        LinkedList<ObjectifAI> objectifsGuerison = genererObjectifsAvecGuerison(objectifsConsideres);
        objectifsConsideres.addAll(objectifsGuerison);
        trierObjectifs(objectifsConsideres);
        enleverObjectifsNonRealisables(objectifsConsideres, actionPosee);
        Logs.AI_LOGGER.info("Taille de la liste d'objectifs: " + objectifsConsideres.size());
        // TODO: Plante quand la liste d'objectifs est vide. Dans ce cas, on crée un objectif qui ne fait rien.
        if (objectifsConsideres.size() > 0) {
            objectifRetenu = objectifsConsideres.get(0);
        } else  {
            objectifRetenu = new ObjectifAI(ai.getMain());
            objectifRetenu.tuileDepart = ai.getPartie().getGrille().getElementAt(ai.getPosition());
            objectifRetenu.tuileDestination = objectifRetenu.tuileDepart;
        }
    }

    private LinkedList<ObjectifAI> genererObjectifsSansGuerison(boolean actionPosee) {
        LinkedList<ObjectifAI> objectifs = new LinkedList<>();
        for (TuileJeu tuile : destinations) {
            List<ObjectifAI> lesObjectifs = creerObjectifsTuile(tuile, actionPosee);
            for (ObjectifAI unObjectif : lesObjectifs) {
                unObjectif.valeurObjectif = evaluerObjectif(unObjectif);
            }
            objectifs.addAll(lesObjectifs);
        }
        return objectifs;
    }

    private LinkedList<ObjectifAI> genererObjectifsAvecGuerison(LinkedList<ObjectifAI> objectifsInitiaux) {
        int nombreBlessures = HexKnightUtils.compterBlessures(ai.getMain());
        LinkedList<ObjectifAI> nouveauxObjectifs = new LinkedList<>();
        for (ObjectifAI unObjectif : objectifsInitiaux) {
            for (int i = 1; i <= nombreBlessures; i++) {
                ObjectifAI objAvecBlessureMain = unObjectif.clone();
                objAvecBlessureMain.guerison += i;
                objAvecBlessureMain.valeurObjectif = evaluerObjectif(objAvecBlessureMain);
                nouveauxObjectifs.add(objAvecBlessureMain);
            }
        }
        return nouveauxObjectifs;
    }

    private void trierObjectifs(LinkedList<ObjectifAI> objectifs) {
        // On trie en ordre décroissant de valeur
        Collections.sort(objectifs, new Comparator<ObjectifAI>() {

            @Override
            public int compare(ObjectifAI objectifGauche, ObjectifAI objectifDroit) {
                if (objectifGauche.valeurObjectif < objectifDroit.valeurObjectif) {
                    return 1;
                } else if (objectifGauche.valeurObjectif > objectifDroit.valeurObjectif) {
                    return -1;
                } else {
                    return 0;
                }
            }

        });
    }

    private void enleverObjectifsNonRealisables(LinkedList<ObjectifAI> objectifs, boolean actionPosee) {
        do {
            ObjectifAI unObjectif = objectifs.get(0);
            trouverMeilleurePermutation(unObjectif, actionPosee);
            if (unObjectif.meilleurePermutation == null) {
                objectifs.remove(0);
            } else if (depasseSeuilBlessures(unObjectif)) {
                objectifs.remove(0);
            }
        } while (! objectifs.isEmpty() && objectifs.get(0).meilleurePermutation == null);
    }

    private List<ObjectifAI> creerObjectifsTuile(TuileJeu tuile, boolean actionPosee) {
        LinkedList<ObjectifAI> objectifsTuile = new LinkedList<>();
        boolean termine = false;
        while (!termine) {
            ObjectifAI objectifDeplacement = creerObjectifDeplacement(tuile, actionPosee);
            objectifsTuile.add(objectifDeplacement);

            List<TuileJeu> tuiles = obtenirTuileAdjacentesAvecMonstres(tuile);
            for (TuileJeu tuileAdjacente : tuiles) {
                Monstre unMonstre = tuileAdjacente.getMonstre();

                // Objectif sans blessure
                ObjectifAI objectifSansBlessure = objectifDeplacement.clone();
                objectifSansBlessure.tuileCombat = tuileAdjacente;
                objectifSansBlessure.attaque = unMonstre.getDefense();
                objectifSansBlessure.defense = unMonstre.getAttaque();
                objectifsTuile.add(objectifSansBlessure);

                // Objectif avec blessure et guérison de 0 au nombre de blessures
                ObjectifAI objectifBlessureGuerison = objectifSansBlessure.clone();
                objectifBlessureGuerison.defense = 0;
                int nombreBlessure = (int) Math.ceil(unMonstre.getAttaque() / (double) ai.getNiveauArmure());
                for (int i = 0; i <= nombreBlessure; i++) {
                    ObjectifAI objectif = objectifBlessureGuerison.clone();
                    objectif.guerison = i;
                    objectif.blessures += nombreBlessure - i;
                    objectifsTuile.add(objectif);
                }
            }
            termine = true;
        }
        return objectifsTuile;
    }

    private double evaluerObjectif(ObjectifAI objectif) {
        double valeurEmplacement;
        double valeurExperience = 0.0;
        double valeurGuerison = objectif.guerison * FACTEUR_GUERISON;
        double valeurBlessure = objectif.blessures * FACTEUR_BLESSURES;
        double valeurEmplacementAdversaire = 0.0;

        valeurEmplacement = evaluerTuile(objectif.tuileDestination) * FACTEUR_EMPLACEMENT;
        if (objectif.tuileCombat != null) {
            valeurExperience = objectif.tuileCombat.getMonstre().getExperience() * FACTEUR_EXPERIENCE;
        }
        return valeurExperience + valeurEmplacement
                + valeurEmplacementAdversaire + valeurGuerison + valeurBlessure;
    }

    private ObjectifAI creerObjectifDeplacement(TuileJeu tuile, boolean actionPosee) {
        ObjectifAI nouvelObjectif = new ObjectifAI(ai.getPartie().getJoueurActif().getMain());
        TableauHexagonal<TuileJeu> carte = ai.getPartie().getGrille();
        nouvelObjectif.tuileDepart = carte.getElementAt(ai.getPosition());
        nouvelObjectif.tuileDestination = tuile;
        nouvelObjectif.coutDeplacement = getCoutDeplacement(tuile);
        nouvelObjectif.blessures = HexKnightUtils.compterBlessures(ai.getMain());
        return nouvelObjectif;
    }

    private void trouverMeilleurePermutation(ObjectifAI objectif, boolean actionPosee) {
        List<CarteJeu> permutation = null;
        int nombreCarteRestantes = -1;  // pour le moment c'est ce qui permet d'évaluer une permutation
        for (List<CarteJeu> permutationActive : permutations) {
            if (objectif.estPossible(permutationActive, actionPosee)) {
                int nbCartes = objectif.compterCartesRestantes(permutationActive, actionPosee);
                if (nbCartes > nombreCarteRestantes) {
                    nombreCarteRestantes = nbCartes;
                    permutation = permutationActive;
                }
            }
        }
        objectif.meilleurePermutation = permutation;
    }

    private List<TuileJeu> obtenirTuileAdjacentesAvecMonstres(TuileJeu tuile) {
        List<TuileJeu> tuiles = new LinkedList<>();
        TableauHexagonal<TuileJeu> carte = ai.getPartie().getGrille();

        for (DirectionHexagonale dir : DirectionHexagonale.values()) {
            TuileJeu tuileAdjacente = carte.getAdjacentElement(tuile, dir);
            if (tuileAdjacente.estRevelee() && tuileAdjacente.contientMonstre()) {
                tuiles.add(tuileAdjacente);
            }
        }
        return tuiles;
    }

    private boolean depasseSeuilBlessures(ObjectifAI objectif) {
        // On dépasse le seuil si des nouvelles blessures cause un dépassement.
        // Si les blessures sont déjà là, on en accepte pas de nouvelles
        boolean reponse = (double) objectif.blessures / tailleMainProchainTour(objectif) >= SEUIL_BLESSURES;
        if (reponse) {
            reponse = objectif.blessures > HexKnightUtils.compterBlessures(ai.getMain());
        }
        return reponse;
    }

    private double tailleMainProchainTour(ObjectifAI objectif) {
        int taille = ai.getTailleMain();
        TuileJeu destination = objectif.tuileDestination;
        if (destination.getAjout().equals(TypeAjoutTuile.VILLAGE)) {
            taille++;
        } else if (destination.getAjout().equals(TypeAjoutTuile.MONASTERE)) {
            taille++;
        }
        // TODO: Ne tient pas compte du fait que le personnage peut monter de niveau et augmenter la taille de sa
        // main
        return taille;
    }
    
    public void appliquerActions(EtatRonde etat) {
        
        ActionAI action = creerActionAI(etat);
        action.appliquerAction(etat, objectifRetenu);
    }
    
    private ActionAI creerActionAI(EtatRonde etat) {
        ActionAI action = null;
        IDEtat idEtat = etat.getID();
        switch (idEtat) {
            case REPOS:
                action = new ActionRepos(ai);
                break;
            case ATTENTE:
                action = new ActionAttente(ai);
                break;
            case PREPARATION:
                action = new ActionPreparation(ai);
                break;
            case ATTAQUE:
                action =  new ActionAttaque(ai);
                break;
            case DEFENSE:
                action = new ActionDefense(ai);
                break;
            case DEPLACEMENT:
                action = new ActionDeplacement(ai);
                break;
            case PROVOQUE:
                action = new ActionProvoque(ai);
                break;
            case SELECTION:
                action = new ActionSelection((ai));
                break;
            case ECHANGE:
                action = new ActionEchange(ai);
                break;
                
            default:
                System.err.println(idEtat);
                System.exit(1);
                break;
        }
        return action;
    }
    
    void loggerCartes() {
        List<CarteJeu> cartes = ai.getMain();
        StringBuilder builder = new StringBuilder();
        builder.append("Cartes en main AI: ");
        for (CarteJeu carte : cartes) {
            builder.append(carte.getType().toString());
            builder.append(" ");
        }
        Logs.AI_LOGGER.info(builder.toString());
    }
}
