package ca.qc.bdeb.tableaujeu;

import ca.qc.bdeb.graphics.DirectionDefilement;
import ca.qc.bdeb.graphics.FormeAbstraite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

/**
 * Classe qui permet d'afficher une carte dans un panel. Les tailles des tuiles doivent être passées en paramètre
 * au constructeur. Si la classe n'est pas capable de tout afficher les tuiles, alors il sera possible de défiler
 * la carte afin de consulter la carte au complet.
 * 
 * @author Eric Wenaas
 * @param <T> Le type de tuile que doit afficher la carte
 */
public class PanelTableau<T extends Tuile> extends JPanel {
        
    private static final int VITESSE_DEFILEMENT = 32;
    private static final int TAILLE_BORDURE = 0;

    // TODO: Mettre ces deux variables privées et remplacer par un Point2D
    protected int retraitX;
    protected int retraitY;
    
    private  int[] taillePossibleTuiles;
    private int indexTailleActuel;
    private DessinateurTuile strategieDessinTuile;
    private final Grllle<T> carteJeu;
    private boolean grillePeinte;
    private final EventListenerList ecouteursSelectionTuile;
    private ContexteTableau strategieCarte;

    /**
     * Classe qui permet d'afficher une grille dans un paneau. Afin de fonctionner, il faut passer ue carte à afficher
     * ainsi qu'une stratégie pour dessiner les tuiles. Le tableau correspond aux tailles de tuiles possibles permettant
     * les différents niveaux de zoom de la carte. Si le tableau ne contient qu'un seul élément alors l'opération de zoom
     * ne sera pas possible.
     * 
     * @param map La carte que l'on doit afficher
     * @param drawer Une stratégie pour dessiner les tuiles
     * @param taillesTuiles Les différentes tailles que peuvent prendre les tuiles
     * @param indiceTailleInitiale L'indice dans le tableau pour la taille initiale
     */
    public PanelTableau(Grllle<T> map, DessinateurTuile drawer, int[] taillesTuiles, int indiceTailleInitiale) {
        super();
        retraitX = retraitY = 0;
        grillePeinte = true;
        this.carteJeu = map;
        strategieDessinTuile = drawer;
        taillePossibleTuiles = taillesTuiles;
        indexTailleActuel = indiceTailleInitiale;
        ecouteursSelectionTuile = new EventListenerList();
        strategieCarte = map.getContexte(taillesTuiles[indiceTailleInitiale]);
        initComponents();
        setLayout(null);
    }
 
    private void initComponents() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                int x = me.getX();
                int y = me.getY();
                Tuile tile = getElementAtPosition(x, y);
                if (tile != null) {
                    fireLocationSelectedEvent(tile.getPosition());
                }
            }
        });
    }
    
    /**
     * Permet de changer la stratégie pour dessiner les tuiles. Arpès le changement, il
     * faut repeindre le panel. Ne se fait pas automatiquement.
     * 
     * @param dessinateur 
     */
    public void setDessinateurTuile(DessinateurTuile dessinateur) {
        strategieDessinTuile = dessinateur;
    }

    /**
     * Retourne l'instance de carte montrée par le paneau.
     * 
     * @return La carte de jeu
     */
    
    public final Grllle<T> getCarteJeu() {
        return carteJeu;
    }

    /**
     * Permet de changer la taille des tuiles possible de la carte. L'indice sert à spécifier la taille initiale dans
     * le tableau.
     * 
     * @param taillesTuilles Le tableau de taille des tuiles
     * @param indiceTaille  L'indice de la taille actuelle
     */
    protected final void setTaillesPossibles(int[] taillesTuilles, int indiceTaille) {
        taillePossibleTuiles = taillesTuilles;
        indexTailleActuel = indiceTaille;
    }

    /**
     * Permet de spécifier s'il faut peindre la grille et donc voir la séparation entre chacune des tuiles.
     * 
     * @param value vrai s'il faut peindre la grille, faux sinon
     */
    public final void setGrillePeinte(boolean value) {
        grillePeinte = value;
        repaint();
    }

    /**
     * Permet de savoir si la grille est présentement peinte et donc si on voit la séparation entre chacune
     * de tuiles.
     * 
     * @return vrai si la grille est peinte.
     */
    public boolean isGrillePeinte() {
        return grillePeinte;
    }
    
    /**
     * Ajoute un éceouteur de position sélectionnée. Lorsqu'une position est sélectionnée la classe émet un événement.
     * Il faut l'écouter afin de pouvoir agir 
     * 
     * @param listener L'écouteur a qu'on enregistre
     */
    public final void addLocationSelectedListener(PositionSelectionneeListener listener) {
        ecouteursSelectionTuile.add(PositionSelectionneeListener.class, listener);
    }

    /**
     * Retire un écouteur de position sélectionnée.
     * 
     * @param listener L'écouteur qu'on retire
     */
    public final void removeLocationSelectedListener(PositionSelectionneeListener listener) {
        ecouteursSelectionTuile.remove(PositionSelectionneeListener.class, listener);
    }

    /**
     * Permet d'envoyer un événement de position sélectionnée à chacun des écouteurs. La position sélectionnée est intégrée
     * à l'événement.
     * 
     * @param location La position qui a été sélectionnée.
     */
    protected final void fireLocationSelectedEvent(PositionGrille location) {
        PositionSelectionneeListener[] list = ecouteursSelectionTuile.getListeners(PositionSelectionneeListener.class);
        PositionSelectionneeEvent event = new PositionSelectionneeEvent(this, location);
        for (PositionSelectionneeListener li : list)
        {
            li.locationSelectedOccured(event);
        }
    }
    
    /**
     * Calcule le nombre de pixels que prend la carte sur la hauteur. Le calcul est fait en focntion du nombre
     * de tuiles et de leur tailles
     *
     * @return La hauteur en pixels
     */
    protected int calculerHauteurRequise() {
        int nombreTuiles = carteJeu.getHeight();
        return (int) (strategieCarte.calculerHauteurRequise(nombreTuiles));
    }

    /**
     * Calcule le nombre de pixels que prend la carte sur la largeur. Le calcul est fait en focntion du nombre
     * de tuiles et de leur tailles
     *
     * @return lalargeur en pixels
     */
    protected int calculerLargeurRequise() {
        int unitCount = carteJeu.getWidth();
        int unitSize = taillePossibleTuiles[indexTailleActuel];
        return (int) strategieCarte.calculerLargeurRequise(unitCount);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // FIXME: S'il n'y a pas de GameMap, il n'y a rien a montrer
        if (carteJeu == null) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        int size = taillePossibleTuiles[indexTailleActuel];
        centrerGrille();
        ZoneVisible bounds = determinerZoneVisible(size);
        FormeAbstraite shape = strategieCarte.getForme();
        // On ne boucle que sur les tuiles qu'il faut dessiner. Sinon, il y a un problème de performance lorsqu'on
        // défile sur une grosse carte.
        for (int i = bounds.colonne1; i <= bounds.colonne2; i++) {
            for (int j = bounds.ligne1; j <= bounds.ligne2; j++) {
                int posX = calculerColonne(i, j);
                int posY = calculerLigne(i, j);
                shape.setLocation(posX, posY);
                Tuile uneTuile = carteJeu.getElementAt(i, j);
                peindreTuile(g2d, uneTuile, shape);
            }
        }
    }

    private ZoneVisible determinerZoneVisible(double taille) {
        int premiereColonne = trouverPremiereColonne(taille);
        int PremiereLigne = trouverPremiereLigne(taille);
        int nombreColonnes = (int) (getWidth() / strategieCarte.calculerEspacementLargeur());
        int nombreLignes = (int) (getHeight() / strategieCarte.calculerEspacementHauteur());
        
        // Determiner le dernier en bas a droite
        int derniereColonne = premiereColonne + nombreColonnes;
        int derniereLigne = PremiereLigne + nombreLignes;

        // Ajout de 1 tuile partout pour s'assurer que ca marche
        // Patch... pour que tout soit couvert
        premiereColonne--;
        derniereColonne++;
        PremiereLigne--;
        derniereLigne++;

        // Il ne faut pas avoir d'indice négatif
        premiereColonne = premiereColonne < 0 ? 0 : premiereColonne;
        PremiereLigne = PremiereLigne < 0 ? 0 : PremiereLigne;

        // On s'assure qu'on ne défonce pas la taille de la carte
        if (derniereColonne > carteJeu.getWidth() - 1) {
            derniereColonne = carteJeu.getWidth() - 1;
        }
        if (derniereLigne > carteJeu.getHeight() - 1) {
            derniereLigne = carteJeu.getHeight() - 1;
        }

        ZoneVisible zone = new ZoneVisible();
        zone.colonne1 = premiereColonne;
        zone.colonne2 = derniereColonne;
        zone.ligne1 = PremiereLigne;
        zone.ligne2 = derniereLigne;

        return zone;
    }

    private int trouverPremiereColonne(double taille) {
        double espacement = strategieCarte.calculerEspacementLargeur();
        int nombreLargeur = (int) (getWidth() / espacement);
        int premiereColonne = (int) (Math.abs(retraitX) / espacement);
        if (premiereColonne + nombreLargeur > carteJeu.getWidth()) {
            premiereColonne = carteJeu.getWidth() - nombreLargeur;
        }
        return premiereColonne;
    }

    private int trouverPremiereLigne(double taille) {
        double espacement = strategieCarte.calculerEspacementHauteur();
        int nombreHauteur = (int) (getHeight() / espacement);
        int premiereLigne = (int) (Math.abs(retraitY) / espacement);
        if (premiereLigne + nombreHauteur > carteJeu.getHeight()) {
            premiereLigne = carteJeu.getHeight() - nombreHauteur;
        }
        return premiereLigne;
    }

    private void centrerGrille() {
        int requiredHeight = calculerHauteurRequise();
        int requiredWidth = calculerLargeurRequise();

        // Pas besoin de centrer la grille puisqu'elle est trop grande pour
        // le composant
        if (requiredHeight >= getHeight() &&
            requiredWidth >=  getWidth()) {
            return;
        }

        // Ici, on centre les tuiles dans le composant
        if (getWidth() > requiredWidth) {
            retraitX = (getWidth() - requiredWidth) / 2;
        }

        if (getHeight() > requiredHeight) {
            retraitY = (getHeight() - requiredHeight) / 2;
        }
    }

    private void peindreTuile(Graphics2D g2d, Tuile uneTuile, FormeAbstraite forme) {
        strategieDessinTuile.dessinerTuile(g2d, uneTuile, strategieCarte, forme);
        if (grillePeinte) {
            g2d.setColor(CouleursInterface.COULEUR_GRILLE);
            g2d.draw(forme);
        }
    }

    private int calculerColonne(int i, int j) {
        int colonne = (int) Math.ceil(strategieCarte.calculerCoordonneesForme(new PositionGrille(i, j)).getX());
        return colonne + retraitX;
    }

    // Returns the line in the hexgrid corresponding to the pixel identified
    // by i, j
    private int calculerLigne(int i, int j) {
        int taille = taillePossibleTuiles[indexTailleActuel];
        int ligne = (int) Math.ceil(strategieCarte.calculerCoordonneesForme(new PositionGrille(i, j)).getY());
        return ligne + retraitY;
    }

    /**
     * Fonction qui retourne la tuile à l'endroit spécifié. Attention, ici la position
     * est spécifié en pixels en fonction du paneau.
     *  
     * @param x valeur en pixel et non en coordonneés de la grille
     * @param y valeur en pixel et non en coordonneés de la grille
     * @return La tuile qui comprend le pixel
     */
    protected Tuile getElementAtPosition(int x, int y) {
        int realX  = x - retraitX;
        int realY = y - retraitY;
        PositionGrille pos = strategieCarte.calculerPositionDansGrille(realX, realY);

        int col = pos.getX();
        int line = pos.getY();

        // S'assure qu'on retourne une coordonnee valide
        if (col < 0 || col > carteJeu.getWidth()) {
            return null;
        }
        if (line < 0 || line > carteJeu.getHeight()) {
            return null;
        }
        return carteJeu.getElementAt(col, line);
    }

    private void ajusterRetraitX() {
        retraitX = retraitX > TAILLE_BORDURE ? TAILLE_BORDURE : retraitX;
        if (calculerLargeurRequise() + retraitX + TAILLE_BORDURE < getWidth()) {
            retraitX = getWidth() - TAILLE_BORDURE - calculerLargeurRequise();
        }
    }

    private void ajusterRetraitY() {
        retraitY = retraitY > TAILLE_BORDURE ? TAILLE_BORDURE : retraitY;
        int hauteur = calculerHauteurRequise();
        if (hauteur + retraitY + TAILLE_BORDURE < getHeight()) {
            retraitY = getHeight() - TAILLE_BORDURE - hauteur;
        }
    }

    /**
     * Centre la carte sur une position.
     *
     * @param p La position où il faut centrer
     */
    public void centrerSurPosition(PositionGrille p) {
        Point.Double posPixel = strategieCarte.calculerCoordonneesForme(p);
        int posX = (int) posPixel.getX();
        int posY = (int) posPixel.getY();
        retraitX = -posX + getWidth() / 2;
        retraitY = -posY + getHeight() / 2;
        // On s'assure de ne pas dépasser les bornes
        ajusterRetraitX();
        ajusterRetraitY();
        repaint();
    }

    public RectangleEnPourcentage getZoneVisible() {
        double X1 = (- retraitX) / (double)  calculerLargeurRequise();
        if (X1 < 0.0 ) {X1 = 0.0;}  // TODO: Patch... j'obtiens parfois un chiffre négatif ??
        double Y1 = (- retraitY) / (double) calculerHauteurRequise();
        if (Y1 < 0.0 ) {Y1 = 0.0;}  // TODO: Patch... j'obtiens parfois un chiffre négatif ??
        double X2 = (- retraitX + getWidth()) / (double) calculerLargeurRequise();
        if (X2 > 1.0 ) {X2 = 1.0;}  // TODO: Patch... j'obtiens parfois un chiffre plus grand que 1.0
        double Y2 = (- retraitY + getHeight()) / (double) calculerHauteurRequise();
        if (Y2 > 1.0 ) {Y2 = 0.0;}  // TODO: Patch... j'obtiens parfois un chiffre plus grand que 1.0
        return new RectangleEnPourcentage(X1, X2, Y1, Y2);
    }
    
    /**
     * Permet de défiler la carte dans la direction spécifiée.
     * 
     * @param direction La direction de défilement
     */
    public void defiler(DirectionDefilement direction) {
        switch (direction) {
            case DOWN:
                retraitY -= VITESSE_DEFILEMENT;
                break;
            case UP:
                retraitY += VITESSE_DEFILEMENT;
                break;
            case LEFT:
                retraitX += VITESSE_DEFILEMENT;
                break;
            case RIGHT:
                retraitX -= VITESSE_DEFILEMENT;
                break;
        }
        ajusterRetraitX();
        ajusterRetraitY();
        repaint();
    }

    private int calculerColonneCentre() {
        int reponse = 0;
        FormeAbstraite forme = strategieCarte.getForme();
        double largeurForme = forme.getBounds().width;
        reponse = (int) ((-retraitX + getWidth() / 2) / largeurForme);
        return reponse;
    }

    private int calculerLigneCentre() {
        int reponse = 0;
        int taille = taillePossibleTuiles[indexTailleActuel];
        FormeAbstraite forme = strategieCarte.getForme();
        double hauteurForme = forme.getBounds().getHeight();
        reponse = (int) ((- retraitY + getHeight() / 2) / hauteurForme);
        return reponse;
    }

    /**
     * Permet de centrer sur le centre de la carte montrée (la partie affichée).
     * Augmente la taille des tuiles.
     */
    public void zoomIn() {
        int x = calculerColonneCentre();
        int y = calculerLigneCentre();
        Tuile ht = carteJeu.getElementAt(x, y);
        if (indexTailleActuel == taillePossibleTuiles.length - 1) {
            return;
        }
        indexTailleActuel++;
        strategieCarte = carteJeu.getContexte(taillePossibleTuiles[indexTailleActuel]);
        centrerSurPosition(ht.getPosition());
    }

    /**
     * Permet de s'éloigner du centre de la carte montrée (la partie affichée).
     * Réduit la taille des tuiles.
     */
    public void zoomOut() {
        int x = calculerColonneCentre();
        int y = calculerLigneCentre();
        Tuile ht = carteJeu.getElementAt(x, y);
        if (indexTailleActuel == 0) {
            return;
        }
        indexTailleActuel--;
        strategieCarte = carteJeu.getContexte(taillePossibleTuiles[indexTailleActuel]);
        centrerSurPosition(ht.getPosition());
    }
}
