/*
 * 
 * Copyright (C) 2015 Éric Wenaas
 * 
 */
package ca.qc.bdeb.hexknight.model;

import ca.qc.bdeb.hexknight.commun.Personnage;
import ca.qc.bdeb.hexknight.commun.TypeAjoutTuile;
import ca.qc.bdeb.tableaujeu.PositionGrille;
import ca.qc.bdeb.utilitaires.IOUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Éric Wenaas
 * 
 */
public class SolutionExamen1H2019Test1 {

    Partie unePartie;
    Joueur joueurActif;

    public SolutionExamen1H2019Test1() {
    }

    @Before
    public void setUp() {
        String path = IOUtils.getURIPath("/hexknight-1.map");
        unePartie = new Partie(path, 0);
        unePartie.enregistrerJoueurHumain(Personnage.KONRAD);
        joueurActif = unePartie.getJoueur(Personnage.KONRAD);
        unePartie.demarrerPartie();
    }

    @After
    public void tearDown() {
        
    }

    @Test
    public void testQuestion1() {
        TuileJeu tuile = unePartie.getGestionnaireGrille().getCarte().getElementAt(7, 8);
        assertTrue(tuile.contientMonstre() && 
                tuile.getMonstre().getType().equals(Monstre.Type.MORT_VIVANT));       
    }

    @Test
    public void testQuestion2() {
        LinkedList<TuileJeu> tuiles = new LinkedList<>();
        int largeur = unePartie.getGrille().getWidth();
        int hauteur = unePartie.getGrille().getHeight();
        for (int x=0; x<largeur;x++) {
            for (int y=0; y<hauteur; y++) {
                TuileJeu tuile = unePartie.getGrille().getElementAt(x, y);
                if (tuile.getAjout().equals(TypeAjoutTuile.CHATEAU)) {
                    tuiles.add(tuile);
                }
            }
        }
        for (TuileJeu t: tuiles) {
            assertTrue(t.contientMonstre() && 
                    t.getMonstre().getType().equals(Monstre.Type.SOLDAT));
        }
    }

    @Test
    public void testQuestion3() {
        PositionGrille depart = new PositionGrille(3, 12);
        PositionGrille arrivee = new PositionGrille(6, 13);
        GestionnaireGrille gestionnaire = unePartie.getGestionnaireGrille();
        
        for (int i=0; i<6; i++) {
            EvaluateurDeplacement eval = new EvaluateurDeplacementJour(new ArrayList<>(), i);
            HashMap<PositionGrille, Integer> tuiles = gestionnaire.appliquerDijkstra(depart, eval).destinations;
            assertFalse(tuiles.containsKey(arrivee));
        }
        
        for (int i=6; i<10; i++) {
            EvaluateurDeplacement eval = new EvaluateurDeplacementJour(new ArrayList<>(), i);
            HashMap<PositionGrille, Integer> tuiles = gestionnaire.appliquerDijkstra(depart, eval).destinations;
            assertTrue(tuiles.containsKey(arrivee));
        }
    }

    @Test
    public void testQuestion4() {
        TuileJeu tuileCombat = unePartie.getGrille().getElementAt(3, 12);
        tuileCombat.setMonstre(new Monstre(Monstre.Type.ORQUE, 4, 4, 4));
        EtatAttaque etat = new EtatAttaque(joueurActif, tuileCombat); 
        joueurActif.setState(etat);
        
        while (etat.getValeurAccumulee() < 4) {
            CarteJeu carte = joueurActif.getMain().get(0);
            etat.actionJouerCarte(carte);
        }
        
        assertTrue(tuileCombat.contientMonstre());
        etat.executer();
        assertFalse(tuileCombat.contientMonstre());
    }

    @Test
    public void testQuestion5() {
        EtatRepos etat = new EtatRepos(joueurActif);
        joueurActif.setState(etat);
        
        CarteJeu blessure1 = CarteJeu.construireCarteJeu(TypeCarte.BLESSURE);
        CarteJeu blessure2 = CarteJeu.construireCarteJeu(TypeCarte.BLESSURE);
      
        joueurActif.ajouterCarteMain(blessure1);
        joueurActif.ajouterCarteMain(blessure2);
        
        int nombreBlessures = compterBlessures();
        assertEquals(2, nombreBlessures);
        unePartie.passerAuProchainTour();
        assertEquals(2, nombreBlessures);        
    }
    
    int compterBlessures() {
        int nombre = 0;
        nombre += HexKnightUtils.compterBlessures(joueurActif.getMain());
        nombre += HexKnightUtils.compterBlessures(joueurActif.getPaquet().iterator());
        return nombre;
    }
}
