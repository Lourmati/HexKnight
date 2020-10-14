/*
 * Copyright (C) 2015 Éric Wenaas
 *
 */
package ca.qc.bdeb.hexknight.model;

import ca.qc.bdeb.hexknight.commun.TypeAjoutTuile;
import ca.qc.bdeb.hexknight.commun.TypeTuile;
import ca.qc.bdeb.tableaujeu.TableauHexagonal;
import ca.qc.bdeb.tableaujeu.DirectionHexagonale;
import ca.qc.bdeb.tableaujeu.PositionGrille;
import ca.qc.bdeb.hexknight.commun.MomentJour;
import ca.qc.bdeb.utilitaires.DomUtils;
import ca.qc.bdeb.utilitaires.IOUtils;
import ca.qc.bdeb.utilitaires.ListeChaineTriee;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Carte Hexagonale où les personnages se déplacent.
 *
 * @author Eric Wenaas <eric.wenaas@bdeb.qc.ca>
 * @version 0.1
 */
public class GestionnaireGrille implements Serializable {

    private static final int WIDTH = 16;
    private static final int HEIGHT = 16;
    private static final int BASE_X = 2;
    private static final int BASE_Y = 12;
    private final String FICHIER_METATUILES = "/metaTiles.xml";
    private TableauHexagonal<TuileJeu> carte;

    public GestionnaireGrille() {
        carte = null;
    }

    public TableauHexagonal<TuileJeu> getCarte() {
        return carte;
    }

    void chargerFichier(String nomFichier, Random generateur) {
        Document doc = DomUtils.openDocument(nomFichier);
        Element root = doc.getDocumentElement();
        int width = Integer.parseInt(root.getAttribute("width"));
        int height = Integer.parseInt(root.getAttribute("height"));
        carte = new TableauHexagonal<>(width, height);
        NodeList tileNodes = root.getChildNodes();
        int taille = tileNodes.getLength();
        for (int i = 0; i < taille; i++) {
            Node activeNode = tileNodes.item(i);
            if (activeNode.getNodeName().equals("tile")) {
                Element elem = (Element) activeNode;
                int x = Integer.parseInt(elem.getAttribute("x"));
                int y = Integer.parseInt(elem.getAttribute("y"));
                TypeTuile type = TypeTuile.valueOf(elem.getAttribute("type"));
                TypeAjoutTuile feature = TypeAjoutTuile.valueOf(elem.getAttribute("feature"));
                PositionGrille position = new PositionGrille(x, y);
                TuileJeu tuile = new TuileJeu(position, type, feature);
                carte.addElement(tuile);
            }
        }
        creerMonstres(carte, generateur);
    }
    
    void sauvegarder(File fichier) {
        Document doc = DomUtils.createDocumentBuilder().newDocument();
        Element root = doc.createElement("map");
        root.setAttribute("width", Integer.toString(carte.getWidth()));
        root.setAttribute("height", Integer.toString(carte.getHeight()));
        doc.appendChild(root);
        for (int i=0; i<carte.getWidth(); i++) {
            for (int j=0; j<carte.getHeight(); j++) {
                TuileJeu tuile = carte.getElementAt(i, j);
                Element newElement = doc.createElement("tile");
                newElement.setAttribute("feature", tuile.getAjout().toString());
                newElement.setAttribute("type", tuile.getType().toString());
                newElement.setAttribute("x", Integer.toString(i));
                newElement.setAttribute("y", Integer.toString(j));
                root.appendChild(newElement);
            }
        }
        DomUtils.sauvegarderFichier(fichier, doc);
    }

    void genererCarte(Random generateur) {
        NodeList tileNodes = obtenirNoeuds();
        LinkedList<Element> tuilesPortail = new LinkedList<>();
        LinkedList<Element> tuilesFaciles = new LinkedList<>();
        LinkedList<Element> tuilesDifficiles = new LinkedList<>();        
        remplirListeTuiles(tileNodes, tuilesPortail, tuilesFaciles, tuilesDifficiles, generateur);
        creerCarte(tuilesPortail.get(0), tuilesFaciles, tuilesDifficiles);
        creerMonstres(carte, generateur);
    }
    
    private void creerCarte(Element tuilesPortail, LinkedList<Element> tuilesFaciles, LinkedList<Element> tuilesDifficiles) {
        ArrayList<PositionGrille> tuilesCentre = obtenirTuilesCentre();
        carte = new TableauHexagonal<>(WIDTH, HEIGHT);
        ajouterTuileBase(tuilesCentre, tuilesPortail);
        ajouterTuilesFaciles(tuilesFaciles, tuilesCentre);
        ajouterTuilesDifficiles(tuilesDifficiles, tuilesCentre);
        ajouterEau();
    }
    
    private void remplirListeTuiles(NodeList tileNodes, LinkedList<Element> tuilesPortail,
                 LinkedList<Element> tuilesFaciles, LinkedList<Element> tuilesDifficiles, Random generateur) {
        int taille = tileNodes.getLength();

        for (int i = 0; i < taille; i++) {
            Node activeNode = tileNodes.item(i);
            if (activeNode.getNodeName().equals("metatile")) {
                Element descriptionTuiles = (Element) activeNode;
                String type = descriptionTuiles.getAttribute("type");
                switch (type) {
                    case "basic":
                        tuilesPortail.add(descriptionTuiles);
                        break;
                    case "easy":
                        tuilesFaciles.add(descriptionTuiles);
                        break;
                    case "hard":
                        tuilesDifficiles.add(descriptionTuiles);
                        break;
                    default:
                        throw new RuntimeException("type " + type + "absent");
                }
            }
        }
        Collections.shuffle(tuilesFaciles, generateur);
        Collections.shuffle(tuilesDifficiles, generateur);
    }

    private void ajouterTuileBase(ArrayList<PositionGrille> tuilesCentre, Element tuilesPortail) {
        ajouterMetaTuile(new PositionGrille(BASE_X, BASE_Y), tuilesPortail);
        tuilesCentre.remove(new PositionGrille(BASE_X, BASE_Y));
    }

    private NodeList obtenirNoeuds() {
        String path = IOUtils.getURIPath(FICHIER_METATUILES);
        Document doc = DomUtils.openDocument(path);
        Element root = doc.getDocumentElement();
        return root.getChildNodes();
    }

    private void ajouterTuilesFaciles(LinkedList<Element> listeTuiles, ArrayList<PositionGrille> tuilesCentre) {
        for (Element elem : listeTuiles) {
            PositionGrille position = tuilesCentre.remove(0);
            ajouterMetaTuile(position, elem);
        }
    }

    private void ajouterTuilesDifficiles(LinkedList<Element> listeTuiles, ArrayList<PositionGrille> tuilesCentre) {
        for (Element elem : listeTuiles) {
            PositionGrille position = tuilesCentre.remove(0);
            ajouterMetaTuile(position, elem);
        }
    }

    private void ajouterEau() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (carte.getElementAt(x, y) != null) {
                    continue;
                }
                PositionGrille position = new PositionGrille(x, y);
                TuileJeu tuileEau = new TuileJeu(position, TypeTuile.COTE, TypeAjoutTuile.AUCUN);
                carte.addElement(tuileEau);
            }
        }
    }

    private ArrayList<PositionGrille> obtenirTuilesCentre() {
        ArrayList<PositionGrille> tuilesCentre = new ArrayList<>();
        tuilesCentre.add(new PositionGrille(2, 12));
        tuilesCentre.add(new PositionGrille(3, 9));
        tuilesCentre.add(new PositionGrille(5, 11));
        tuilesCentre.add(new PositionGrille(4, 7));
        tuilesCentre.add(new PositionGrille(6, 9));
        tuilesCentre.add(new PositionGrille(8, 11));
        tuilesCentre.add(new PositionGrille(5, 4));
        tuilesCentre.add(new PositionGrille(7, 6));
        tuilesCentre.add(new PositionGrille(9, 8));
        tuilesCentre.add(new PositionGrille(11, 10));
        tuilesCentre.add(new PositionGrille(10, 13));
        tuilesCentre.add(new PositionGrille(3, 2));
        tuilesCentre.add(new PositionGrille(6, 2));
        tuilesCentre.add(new PositionGrille(8, 4));
        tuilesCentre.add(new PositionGrille(10, 6));
        tuilesCentre.add(new PositionGrille(12, 8));
        tuilesCentre.add(new PositionGrille(13, 12));
        tuilesCentre.add(new PositionGrille(11, 3));
        tuilesCentre.add(new PositionGrille(13, 5));
        return tuilesCentre;
    }

    private void ajouterMetaTuile(PositionGrille positionGrille, Element descriptionTuiles) {

        NodeList nodes = descriptionTuiles.getChildNodes();
        int taille = nodes.getLength();
        for (int i = 0; i < taille; i++) {
            Node noeud = nodes.item(i);
            if (noeud.getNodeName().equals("tile")) {
                Element descTuile = (Element) noeud;
                String position = descTuile.getAttribute("pos");
                if (position.equals("CENTER")) {
                    TuileJeu nouvelleTuile = creerTuile(positionGrille, descTuile);
                    carte.addElement(nouvelleTuile);
                } else {
                    PositionGrille nouvellePosition = calculerPosition(positionGrille, position);
                    TuileJeu nouvelleTuile = creerTuile(nouvellePosition, descTuile);
                    carte.addElement(nouvelleTuile);
                }
            }
        }
    }

    private TuileJeu creerTuile(PositionGrille positionGrille, Element descriptionTuile) {        
        TypeTuile type = TypeTuile.valueOf(descriptionTuile.getAttribute("type"));
        TypeAjoutTuile feature = TypeAjoutTuile.valueOf(descriptionTuile.getAttribute("feature"));
        
        if (feature.equals(TypeAjoutTuile.VILLE)) {
            feature = TypeAjoutTuile.CHATEAU;
        }
        return new TuileJeu(positionGrille, type, feature);
    }

    private PositionGrille calculerPosition(PositionGrille positionGrille, String position) {
        int x = positionGrille.getX();
        int y = positionGrille.getY();
        switch (position) {
            case "N":
                y = y - 1;
                break;
            case "NE":
                y = x % 2 == 0 ? y - 1 : y;
                x = x + 1;
                break;
            case "SE":
                y = x % 2 == 1 ? y + 1 : y;
                x = x + 1;
                break;
            case "S":
                y = y + 1;
                break;
            case "SW":
                y = x % 2 == 1 ? y + 1 : y;
                x = x - 1;
                break;
            case "NW":
                y = x % 2 == 0 ? y - 1 : y;
                x = x - 1;
                break;
            default:
                throw new RuntimeException("position " + position + "absente");
        }
        return new PositionGrille(x, y);
    }

    PositionGrille getPositionPortail() {
        if (carte == null) {
            throw new IllegalStateException("Aucune carte n'est chargée");
        }
        PositionGrille pos = null;
        for (int x = 0; x < carte.getWidth() && pos == null; x++) {
            for (int y = 0; y < carte.getHeight() && pos == null; y++) {
                TuileJeu tuile = carte.getElementAt(x, y);
                if (tuile.getAjout().equals(TypeAjoutTuile.PORTAIL)) {
                    pos = new PositionGrille(x, y);
                }
            }
        }
        return pos;
    }

    private void creerMonstres(TableauHexagonal<TuileJeu> carte, Random generateur) {
        GestionnaireMonstre gestionMonstres = new GestionnaireMonstre(generateur);
        gestionMonstres.brasserMonstres();
        int width = carte.getWidth();
        int height = carte.getHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                TuileJeu tuile = carte.getElementAt(x, y);
                TypeAjoutTuile ajout = tuile.getAjout();
                Monstre.Type type = ajout.getTypeMonstre();
                if (type != null) {
                    Monstre unMonstre = gestionMonstres.obtenirMonstre(type);
                    if (unMonstre == null) {
                        throw new IllegalStateException("Monstre pas trouvée");
                    }
                    tuile.setMonstre(unMonstre);
                    if (! tuile.getAjout().equals(TypeAjoutTuile.CHATEAU)) {
                        unMonstre.revelerMonstre();
                    }
                }
            }
        }
    }
    
    public AnalyseGrille appliquerDijkstra(PositionGrille depart, EvaluateurDeplacement evaluateur) {
        ListeChaineTriee<InfoPosition> candidats = new ListeChaineTriee<>();
        HashMap<PositionGrille, Integer> destinations = new HashMap<>();
        HashMap<PositionGrille, PositionGrille> precedent = new HashMap();
        precedent.put(depart, null);
        candidats.inserer(new InfoPosition(depart, 0));

        while (!candidats.estVide()) {
            InfoPosition actuelle = candidats.getElement(0);
            TuileJeu tuileActuelle = carte.getElementAt(actuelle.pos);
            candidats.retirer(actuelle);
            destinations.put(actuelle.pos, actuelle.cout);
            for (DirectionHexagonale dir : DirectionHexagonale.values()) {
                TuileJeu tuileVoisine = carte.getAdjacentElement(actuelle.pos, dir);
                if (tuileVoisine == null || destinations.containsKey(tuileVoisine.getPosition())) {
                    continue;
                }

                int coutDeplacement = actuelle.cout + evaluateur.obtenirCout(tuileVoisine);
                if (evaluateur.peutAller(carte, tuileActuelle, tuileVoisine, coutDeplacement)) {
                    InfoPosition ancienCandidat = trouverPosition(candidats, tuileVoisine.getPosition());
                    if (ancienCandidat == null) {
                        candidats.inserer(new InfoPosition(tuileVoisine.getPosition(), coutDeplacement));
                        precedent.put(tuileVoisine.getPosition(), tuileActuelle.getPosition());
                    } else if (ancienCandidat.cout > coutDeplacement) {
                        candidats.retirer(ancienCandidat);
                        candidats.inserer(new InfoPosition(tuileVoisine.getPosition(), coutDeplacement));
                        precedent.put(tuileVoisine.getPosition(), tuileActuelle.getPosition());
                    }
                }
            }
        }
        return new AnalyseGrille(destinations, precedent);
    }
        
    private InfoPosition trouverPosition(ListeChaineTriee<InfoPosition> candidats, PositionGrille position) {
        InfoPosition candidat = null;
        for (InfoPosition elem : candidats) {
            if (position.equals(elem.pos)) {
                candidat = elem;
                break;
            }
        }
        return candidat;
    }

    void appliquerVisibiliteGrille(Partie partie, PositionGrille position) {
        TuileJeu tuile = getCarte().getElementAt(position);
        getCarte().getElementAt(position).revelerTuile();
        revelerTuilesAdjacentes(tuile);
        EvaluateurVisibilite evaluateur = new EvaluateurVisibilite();
        HashMap<PositionGrille, Integer> tuilesVisibles = appliquerDijkstra(position, evaluateur).destinations;
        for (PositionGrille pos : tuilesVisibles.keySet()) {
            Integer cout = tuilesVisibles.get(pos);
            int visibilite = obtenirVisibiliteTuile(partie, tuile);
            appliquerVisibiliteTuile(cout, visibilite, getCarte().getElementAt(pos));
        }
    }

    private int obtenirVisibiliteTuile(Partie partie, TuileJeu tuile) {
        if (partie.getPhaseJour().equals(MomentJour.JOUR)) {
            return tuile.getVisibiliteJour();
        } else {
            return tuile.getVisibiliteNuit();
        }
    }
    
    private void appliquerVisibiliteTuile(int cout, int visibilite, TuileJeu tuile) {
            if (cout < visibilite) {
                tuile.revelerTuile();
                revelerTuilesAdjacentes(getCarte().getElementAt(tuile.getPosition()));
            } else if (cout == visibilite) {
                tuile.revelerTuile();
            }
    }

    private void revelerTuilesAdjacentes(TuileJeu tuile) {
        PositionGrille position = tuile.getPosition();
        for (DirectionHexagonale dr : DirectionHexagonale.values()) {
            TuileJeu tuileAdj = getCarte().getAdjacentElement(position, dr);
            if (tuileAdj != null) {
                tuileAdj.revelerTuile();
            }
        }
        
    }

    private class InfoPosition implements Comparable<InfoPosition> {

        final PositionGrille pos;
        final Integer cout;

        public InfoPosition(PositionGrille pos, Integer cout) {
            this.pos = pos;
            this.cout = cout;
        }

        @Override
        public int compareTo(InfoPosition t) {
            return this.cout.compareTo(t.cout);
        }
    }

    private class GestionnaireMonstre {

        private final LinkedList<Monstre> monstres;
        private final LinkedList<Monstre> monstreUtilises;
        private final Random generateur;

        GestionnaireMonstre(Random generateur) {
            this.generateur = generateur;
            monstres = new LinkedList<>();
            monstreUtilises = new LinkedList<>();
            chargerMonstres();
        }

        private void chargerMonstres() {
            Document doc = DomUtils.openDocument(IOUtils.getURIPath("/creatures.xml"));
            Element root = doc.getDocumentElement();
            NodeList nodes = root.getChildNodes();
            int taille = nodes.getLength();
            for (int i = 0; i < taille; i++) {
                Node activeNode = nodes.item(i);
                if (activeNode.getNodeName().equals("type")) {
                    creerMonstres((Element) activeNode);
                }
            }
        }

        private void creerMonstres(Element noeudType) {
            NodeList creatures = noeudType.getElementsByTagName("creature");
            String descriptionType = noeudType.getAttribute("nom");
            int nombre = creatures.getLength();
            for (int i = 0; i < nombre; i++) {
                Element elem = (Element) creatures.item(i);
                int nombreCreatures = Integer.parseInt(elem.getAttribute("nombre"));
                for (int j = 0; j < nombreCreatures; j++) {
                    Monstre nouveauMonstre = new Monstre(descriptionType, elem);
                    monstres.add(nouveauMonstre);
                }
            }
        }

        Monstre obtenirMonstre(Monstre.Type type) {
            Iterator<Monstre> iter = monstres.iterator();
            Monstre monstreCorrespondant = null;
            while (monstreCorrespondant == null && iter.hasNext()) {
                Monstre candidat = iter.next();
                if (candidat.getType().equals(type)) {
                    monstreCorrespondant = candidat;
                }
            }
            monstres.remove(monstreCorrespondant);
            monstreUtilises.add(monstreCorrespondant);
            return monstreCorrespondant;
        }

        void brasserMonstres() {
            Collections.shuffle(monstres, generateur);
        }
    }
}
