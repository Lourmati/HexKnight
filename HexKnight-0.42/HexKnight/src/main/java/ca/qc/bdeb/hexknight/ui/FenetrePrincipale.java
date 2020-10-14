/*
 * Copyright (C) 2015 Éric Wenaas
 *
 */
package ca.qc.bdeb.hexknight.ui;

import ca.qc.bdeb.tableaujeu.PositionGrille;
import ca.qc.bdeb.tableaujeu.PositionSelectionneeEvent;
import ca.qc.bdeb.tableaujeu.PositionSelectionneeListener;
import ca.qc.bdeb.graphics.DirectionDefilement;
import ca.qc.bdeb.graphics.Geometrie;
import ca.qc.bdeb.graphics.Position;
import ca.qc.bdeb.hexknight.commun.Personnage;
import ca.qc.bdeb.hexknight.commun.Evenement;
import ca.qc.bdeb.hexknight.commun.Message;
import ca.qc.bdeb.hexknight.model.GestionnairePartie;
import ca.qc.bdeb.hexknight.model.Joueur;
import ca.qc.bdeb.hexknight.model.Partie;
import ca.qc.bdeb.hexknight.commun.Observateur;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.io.File;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

/**
 * Classe qui représente la fenêtre principale. Cette fenêtre ne comprend pas de
 * menu et gère l'ensemble du jeu.
 *
 * @author Eric Wenaas <eric.wenaas@bdeb.qc.ca>
 * @version 0.1
 */
public class FenetrePrincipale extends JFrame implements Observateur, PositionSelectionneeListener {

    private final int HAUTEUR_ZONE_JEU;
    private final int HAUTEUR_ZONE_MAIN;
    private final double RATIO_ZONE_JEU = 0.12;
    private final double RATIO_ZONE_MAIN = 0.12;
    private final double RATIO_PANEL_INFORMATION = 0.20;

    private PanelZoneMain panelMain;
    private PanelZoneJeu panelJeu;
    private PanelGrilleJeu carte;
    private JPanel panelInformation;
    private PanelMenu panelMenu;
    private PanelOffreCartes panelOffre;
    private DialogOptions dialogOption;
    private DessinateurTuileHexKnight dessinateurNormal;
    private GestionnairePartie gestionnaireParties;
    private Partie modelePartie;

    private final AbstractAction actionQuitter = new AbstractAction("Quitter") {

        @Override
        public void actionPerformed(ActionEvent ae) {
            int confirmation = JOptionPane.showConfirmDialog(FenetrePrincipale.this, "Voulez-vous vraiment quitter ?", "QUITTER", JOptionPane.OK_CANCEL_OPTION);
            if (confirmation == JOptionPane.OK_OPTION) {
                System.exit(0);
            }
        }
    };

    private final AbstractAction actionZoomIn = new AbstractAction("ZoomIn") {

        @Override
        public void actionPerformed(ActionEvent ae) {
            carte.zoomIn();
        }
    };

    private final AbstractAction actionZoomOut = new AbstractAction("ZoomOut") {

        @Override
        public void actionPerformed(ActionEvent ae) {
            carte.zoomOut();
        }
    };

    private final AbstractAction actionMontrerGrille = new AbstractAction("ToggleHexes") {
        @Override
        public void actionPerformed(ActionEvent e) {
            carte.setGrillePeinte(!carte.isGrillePeinte());
        }
    };

    private final AbstractAction actionDefilerDroite = new AbstractAction("scrollRight") {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (carte != null) {
                carte.defiler(DirectionDefilement.RIGHT);
            }
        }
    };

    private final AbstractAction actionDefilerGauche = new AbstractAction("scrollLeft") {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (carte != null) {
                carte.defiler(DirectionDefilement.LEFT);
            }
        }
    };

    private final AbstractAction actionDefilerHaut = new AbstractAction("scrollUp") {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (carte != null) {
                carte.defiler(DirectionDefilement.UP);
            }
        }
    };

    private final AbstractAction actionDefilerBas = new AbstractAction("scrollDown") {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (carte != null) {
                carte.defiler(DirectionDefilement.DOWN);
            }
        }
    };

    private final AbstractAction actionNouvellePartie = new AbstractAction("Nouvelle Partie") {
        @Override
        public void actionPerformed(ActionEvent ae) {
            creerPartie();
            setVisibiliteMenu(false);
        }
    };

    private final AbstractAction actionCentrerSurJoueur = new AbstractAction("Centrer sur jeton") {
        @Override
        public void actionPerformed(ActionEvent ae) {
            carte.centrerSurPosition(modelePartie.getJoueurActif().getPosition());
        }
    };

    private final AbstractAction actionActiverCoordonnees = new AbstractAction("Activer coordonnees") {
        @Override
        public void actionPerformed(ActionEvent ae) {
            dessinateurNormal.activerCoordonnees();
            carte.repaint();
        }
    };

    private final AbstractAction actionAfficherMonstre = new AbstractAction("AfficherMonstres") {
        @Override
        public void actionPerformed(ActionEvent e) {
            dessinateurNormal.afficherMonstres();
            carte.repaint();
        }
    };

    private final AbstractAction actionActiverMenu = new AbstractAction("ActiverMenu") {
        @Override
        public void actionPerformed(ActionEvent e) {
            changerVisibiliteMenu();
        }
    };

    private final AbstractAction actionSauvegarderPartie = new AbstractAction("Sauvegarder partie") {
        @Override
        public void actionPerformed(ActionEvent ae) {
            File dossierSauvegarde = new File(System.getProperty("user.home") + "/.hexknight/saves");
            dossierSauvegarde.mkdirs();
            JFileChooser selecteur = new JFileChooser(dossierSauvegarde.getAbsolutePath());
            selecteur.setFileFilter(new FileFilter() {

                @Override
                public boolean accept(File file) {
                    return file.getName().endsWith(".xkg");
                }

                @Override
                public String getDescription() {
                    return "Fichier de sauvegarde *.xkg";
                }
            });
            int option = selecteur.showSaveDialog(FenetrePrincipale.this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File fichier = selecteur.getSelectedFile();
                if (!fichier.getName().endsWith(".xkg")) {
                    fichier = new File(fichier.getAbsolutePath() + ".xkg");
                }
                gestionnaireParties.sauverPartie(fichier);
            }
            setVisibiliteMenu(false);
        }
    };

    private final AbstractAction actionSauvegarderCarte = new AbstractAction("Sauvegarder carte") {
        @Override
        public void actionPerformed(ActionEvent ae) {
            if (modelePartie == null) {
                JOptionPane.showMessageDialog(FenetrePrincipale.this, "Aucune carte n'est chargée", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
            File dossierSauvegarde = new File(System.getProperty("user.home") + "/.hexknight/maps");
            dossierSauvegarde.mkdirs();
            JFileChooser selecteur = new JFileChooser(dossierSauvegarde.getAbsolutePath());
            selecteur.setFileFilter(new FileFilter() {

                @Override
                public boolean accept(File file) {
                    return file.getName().endsWith(".xkm");
                }

                @Override
                public String getDescription() {
                    return "Fichier de sauvegarde *.xkm";
                }
            });
            int option = selecteur.showSaveDialog(FenetrePrincipale.this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File fichier = selecteur.getSelectedFile();
                if (!fichier.getName().endsWith(".xkm")) {
                    fichier = new File(fichier.getAbsolutePath() + ".xkm");
                }
                modelePartie.sauverCarte(fichier);
            }
        }
    };

    private final AbstractAction actionChargerPartie = new AbstractAction("Charger partie") {
        @Override
        public void actionPerformed(ActionEvent ae) {
            File dossierSauvegarde = new File(System.getProperty("user.home") + "/.hexknight/saves");
            dossierSauvegarde.mkdirs();
            JFileChooser selecteur = new JFileChooser(dossierSauvegarde.getAbsolutePath());
            selecteur.setFileFilter(new FileFilter() {

                @Override
                public boolean accept(File file) {
                    return file.getName().endsWith(".xkg");
                }

                @Override
                public String getDescription() {
                    return "Fichier de sauvegarde *.xkg";
                }
            });
            int option = selecteur.showOpenDialog(FenetrePrincipale.this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File fichier = selecteur.getSelectedFile();
                if (!fichier.getName().endsWith(".xkg")) {
                    fichier = new File(fichier.getAbsolutePath() + ".xkg");
                }
                Partie partieChargee = gestionnaireParties.chargerPartie(fichier);
                if (partieChargee != null) {
                    initialiserApresChargement(partieChargee);
                } else {
                    JOptionPane.showMessageDialog(FenetrePrincipale.this, "Problème de chargement de fichier", "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    };

    private final AbstractAction actionChargerLog = new AbstractAction("Charger log") {
        @Override
        public void actionPerformed(ActionEvent ae) {
            File dossierSauvegarde = new File(System.getProperty("user.home") + "/.hexknight/logs");
            dossierSauvegarde.mkdirs();
            JFileChooser selecteur = new JFileChooser(dossierSauvegarde.getAbsolutePath());
            selecteur.setFileFilter(new FileFilter() {

                @Override
                public boolean accept(File file) {
                    return file.getName().endsWith(".hklog");
                }

                @Override
                public String getDescription() {
                    return "Fichier de sauvegarde *.hklog";
                }
            });
            int option = selecteur.showOpenDialog(FenetrePrincipale.this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File fichier = selecteur.getSelectedFile();
                if (carte != null) {
                    remove(carte);
                }
                if (modelePartie != null) {
                    modelePartie.retirerObservateur(FenetrePrincipale.this);
                    modelePartie.retirerObservateur(panelJeu);
                }
                modelePartie = gestionnaireParties.chargerLog(fichier);
                modelePartie.ajouterObservateur(FenetrePrincipale.this);
                initialiserCarte();
                ajouterInfoJoueur();
                initialiserPaneauxJeux();
                repaint();
                FenetrePrincipale.this.changementEtat();
            }
            setVisibiliteMenu(false);
        }
    };

    private final AbstractAction actionOptions = new AbstractAction("Options") {
        @Override
        public void actionPerformed(ActionEvent ae) {
            dialogOption.setVisible(true);
        }

    };

    private final AbstractAction actionProchainePhase = new AbstractAction("ProchainePhase") {
        @Override
        public void actionPerformed(ActionEvent ae) {
            Message message = modelePartie.getMessage();
            if (message.equals(Message.VIDE)) {
                modelePartie.prochainePhase();
            } else if (message.equals(Message.PARTIE_TERMINEE)) {
                StringBuilder texte = new StringBuilder();
                texte.append("Score \n");
                for (Joueur unJoueur : modelePartie.getJoueurs()) {
                    texte.append("Joueur: ");
                    texte.append(unJoueur.getPersonnage().toString());
                    texte.append(" ");
                    texte.append(Integer.toString(unJoueur.calculerPoints()));
                    texte.append(" points\n");
                }
                JOptionPane.showConfirmDialog(FenetrePrincipale.this, texte.toString(),
                        "Pour votre information", JOptionPane.PLAIN_MESSAGE);

            } else if (message.isConfirmationRequise()) {
                int reponse = JOptionPane.showConfirmDialog(FenetrePrincipale.this, message.getDescription(), "ATTENTION", JOptionPane.OK_CANCEL_OPTION);
                if (reponse == JOptionPane.OK_OPTION) {
                    modelePartie.prochainePhase();
                }

            } else {
                JOptionPane.showMessageDialog(FenetrePrincipale.this, message.getDescription());
            }
        }
    };

    /**
     * Constructeur. Il faut spécifier les dimensions et après elle ne pourront
     * être changées.
     *
     * @param createurPartie
     * @param bounds Les dimension et la position de la fenêtre.
     */
    public FenetrePrincipale(GestionnairePartie createurPartie, Rectangle bounds) {
        setBounds(bounds);
        this.gestionnaireParties = createurPartie;
        HAUTEUR_ZONE_JEU = (int) (bounds.height * RATIO_ZONE_JEU) > 100 ? (int) (bounds.height * RATIO_ZONE_JEU) : 100;
        HAUTEUR_ZONE_MAIN = (int) (bounds.height * RATIO_ZONE_MAIN) > 100 ? (int) (bounds.height * RATIO_ZONE_MAIN) : 100;
        setUndecorated(true);
        setResizable(false);
        setLayout(null);
        initializeComponents();
        addKeyBindings();
        ajouterActions();
    }

    private void creerPartie() {
        if (carte != null) {
            remove(carte);
        }
        if (modelePartie != null) {
            modelePartie.retirerObservateur(this);
            modelePartie.retirerObservateur(panelJeu);
        }
        initialiserPartie();
        modelePartie.ajouterObservateur(this);
        initialiserCarte();
        ajouterInfoJoueur();
        initialiserPaneauxJeux();
        repaint();
    }

    private void initialiserPartie() {
        File fichier = null;
        if (dialogOption.isSelectionCarte()) {
            File dossierSauvegarde = new File(System.getProperty("user.home") + "/.hexknight/maps");
            dossierSauvegarde.mkdirs();
            JFileChooser selecteur = new JFileChooser(dossierSauvegarde.getAbsolutePath());
            selecteur.setFileFilter(new FileFilter() {

                @Override
                public boolean accept(File file) {
                    return file.getName().endsWith(".xkm");
                }

                @Override
                public String getDescription() {
                    return "Fichier de sauvegarde *.xkm";
                }
            });
            int option = selecteur.showOpenDialog(FenetrePrincipale.this);
            if (option == JFileChooser.APPROVE_OPTION) {
                fichier = selecteur.getSelectedFile();
            }
        }

        if (fichier != null) {
            modelePartie = gestionnaireParties.creerPartie(fichier.getAbsolutePath(), System.currentTimeMillis());
        } else {
            modelePartie = gestionnaireParties.creerPartie(System.currentTimeMillis());
        }
        for (Personnage per : Personnage.values()) {
            enregistrerJoueur(per);
        }
        modelePartie.demarrerPartie();
        modelePartie.prochainePhase();
    }

    private void changerVisibiliteMenu() {
        boolean visibilite = !panelMenu.isVisible();
        setVisibiliteMenu(visibilite);
        ajusterActions();
    }

    private void setVisibiliteMenu(boolean visibilite) {
        panelMenu.setVisible(visibilite);
        ajusterActions();
    }

    private void ajusterActions() {
        boolean menuVisible = panelMenu.isVisible();
        actionZoomIn.setEnabled(!menuVisible);
        actionZoomOut.setEnabled(!menuVisible);
        actionMontrerGrille.setEnabled(!menuVisible);
        actionDefilerBas.setEnabled(!menuVisible);
        actionDefilerHaut.setEnabled(!menuVisible);
        actionDefilerGauche.setEnabled(!menuVisible);
        actionDefilerDroite.setEnabled(!menuVisible);
        actionCentrerSurJoueur.setEnabled(!menuVisible);
        actionActiverCoordonnees.setEnabled(!menuVisible);
        actionAfficherMonstre.setEnabled(!menuVisible);
        actionProchainePhase.setEnabled(!menuVisible);
    }

    private void enregistrerJoueur(Personnage personnage) {
        String type = dialogOption.getTypeJoueur(personnage);
        switch (type) {
            case "AI":
                modelePartie.enregistrerJoueurArtificiel(personnage);
                break;
            case "Humain":
                modelePartie.enregistrerJoueurHumain(personnage);
                break;
        }
    }

    private void initialiserCarte() {
        dessinateurNormal = new DessinateurTuileHexKnight(modelePartie);
        carte = new PanelGrilleJeu(modelePartie.getGrille(), dessinateurNormal, new int[]{72, 108, 144}, 1);
        carte.setLocation(100, 0);
        int largeurJeu = (int) (getWidth() - getWidth() * RATIO_PANEL_INFORMATION) - 100;
        int y = getHeight() - HAUTEUR_ZONE_MAIN - HAUTEUR_ZONE_JEU;
        carte.setSize(largeurJeu, y);
        carte.addLocationSelectedListener(this);
        carte.centrerSurPosition(modelePartie.getJoueurActif().getPosition());
        add(carte);
    }

    private void initialiserPaneauxJeux() {
        panelMain.setPartie(modelePartie);
        panelMain.setJoueur(modelePartie.getJoueurActif());
        panelJeu.setPartie(modelePartie);
        panelOffre.setPartie(modelePartie);
        panelOffre.changementEtat();
        panelMain.changementEtat();
        panelJeu.changementEtat();
    }

    private void initialiserApresChargement(Partie partieChargee) {
        if (carte != null) {
            remove(carte);
        }

        if (modelePartie != null) {
            modelePartie.retirerObservateur(this);
            modelePartie.retirerObservateur(panelJeu);
        }

        modelePartie = partieChargee;
        modelePartie.ajouterObservateur(this);
        initialiserCarte();
        ajouterInfoJoueur();
        initialiserPaneauxJeux();
        repaint();
    }

    private void ajouterInfoJoueur() {
        for (Component c : panelInformation.getComponents()) {
            panelInformation.remove(c);
        }
        PanelPartieInfo panelInfo = new PanelPartieInfo(modelePartie);
        panelInformation.add(panelInfo);
        panelInfo.changementEtat();

        List<Joueur> joueurs = modelePartie.getJoueurs();
        for (Joueur joueur : joueurs) {
            PanelJoueurInfo panInfo = new PanelJoueurInfo(joueur);
            panelInformation.add(panInfo);
        }
        panelInformation.revalidate();
    }

    private void initializeComponents() {
        getContentPane().setBackground(CouleursInterface.FOND_GRILLE);
        creerPanelMain();
        creerPanelJeu();
        creerPanelInformation();
        creerPanelMenu();
        creerDialogOptions();
        creerPanelOffre();
    }

    private void creerPanelMain() {
        int x = 0;
        int y = getHeight() - HAUTEUR_ZONE_MAIN;
        int largeurJeu = (int) (getWidth() - getWidth() * RATIO_PANEL_INFORMATION);

        panelMain = new PanelZoneMain();
        panelMain.setLayout(null);
        panelMain.setLocation(x, y);
        panelMain.setSize(largeurJeu, HAUTEUR_ZONE_MAIN);
        panelMain.setBackground(CouleursInterface.ZONE_JOUEUR);
        add(panelMain);
    }

    private void creerPanelJeu() {
        int x = 0;
        int y = getHeight() - HAUTEUR_ZONE_MAIN * 2;
        int largeurJeu = (int) (getWidth() - getWidth() * RATIO_PANEL_INFORMATION);
        Rectangle boundsZoneCarte = new Rectangle(x, y, largeurJeu, HAUTEUR_ZONE_JEU);

        panelJeu = new PanelZoneJeu(boundsZoneCarte, actionProchainePhase);
        add(panelJeu);
    }

    private void creerPanelInformation() {
        int hauteur = getHeight();
        int largeurJeu = (int) (getWidth() - getWidth() * RATIO_PANEL_INFORMATION);
        int x = largeurJeu;

        panelInformation = new JPanel();
        panelInformation.setLocation(x, 0);
        panelInformation.setBackground(CouleursInterface.ZONE_INFO);
        panelInformation.setSize(getWidth() - largeurJeu, hauteur);
        add(panelInformation);

    }

    private void creerPanelMenu() {
        int largeurJeu = (int) (getWidth() - getWidth() * RATIO_PANEL_INFORMATION);
        panelMenu = new PanelMenu(getWidth() - largeurJeu, HAUTEUR_ZONE_JEU + HAUTEUR_ZONE_MAIN);
        Geometrie.placerSurComposant(this, panelMenu, Position.CENTRE);
        add(panelMenu);
    }

    private void creerDialogOptions() {
        dialogOption = new DialogOptions(this, true);
        dialogOption.setResizable(false);
        dialogOption.removeNotify();
        dialogOption.setUndecorated(true);
        dialogOption.setLocationRelativeTo(this);
        dialogOption.setVisible(false);
    }

    private void creerPanelOffre() {
        Rectangle boundsPanelOffre = new Rectangle(0, 0,
                100, getHeight() - HAUTEUR_ZONE_MAIN - HAUTEUR_ZONE_JEU);
        panelOffre = new PanelOffreCartes(boundsPanelOffre);
        add(panelOffre);
    }

    private void addKeyBindings() {
        JPanel contentPane = (JPanel) getContentPane();

        contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
                put(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_MASK), "zoomIn");
        contentPane.getActionMap().put("zoomIn", actionZoomIn);

        contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
                put(KeyStroke.getKeyStroke('X', InputEvent.CTRL_MASK), "zoomOut");
        contentPane.getActionMap().put("zoomOut", actionZoomOut);

        contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
                put(KeyStroke.getKeyStroke('T', InputEvent.CTRL_MASK), "montrerGrille");
        contentPane.getActionMap().put("montrerGrille", actionMontrerGrille);

        contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
                put(KeyStroke.getKeyStroke("RIGHT"), "ScrollRight");
        contentPane.getActionMap().put("ScrollRight", actionDefilerDroite);

        contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
                put(KeyStroke.getKeyStroke("LEFT"), "ScrollLeft");
        contentPane.getActionMap().put("ScrollLeft", actionDefilerGauche);

        contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
                put(KeyStroke.getKeyStroke("UP"), "ScrollUp");
        contentPane.getActionMap().put("ScrollUp", actionDefilerHaut);

        contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
                put(KeyStroke.getKeyStroke("DOWN"), "ScrollDown");
        contentPane.getActionMap().put("ScrollDown", actionDefilerBas);

        contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
                put(KeyStroke.getKeyStroke("C"), "CentrerJoueur");
        contentPane.getActionMap().put("CentrerJoueur", actionCentrerSurJoueur);

        contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
                put(KeyStroke.getKeyStroke("G"), "ActiverCoordonnees");
        contentPane.getActionMap().put("ActiverCoordonnees", actionActiverCoordonnees);

        contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
                put(KeyStroke.getKeyStroke("U"), "AfficherMonstres");
        contentPane.getActionMap().put("AfficherMonstres", actionAfficherMonstre);

        contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
                put(KeyStroke.getKeyStroke("ESCAPE"), "ActiverMenu");
        contentPane.getActionMap().put("ActiverMenu", actionActiverMenu);
    }

    private void ajouterActions() {
        panelMenu.ajouterAction(actionNouvellePartie);
        panelMenu.ajouterAction(actionSauvegarderPartie);
        panelMenu.ajouterAction(actionChargerPartie);
        panelMenu.ajouterAction(actionChargerLog);
        panelMenu.ajouterAction(actionOptions);
        panelMenu.ajouterAction(actionSauvegarderCarte);
        panelMenu.ajouterAction(actionQuitter);
    }

    @Override
    public void changementEtat() {
        repaint();
    }

    @Override
    public void changementEtat(Enum<?> property, Object o) {
        Joueur joueur = (Joueur) o;

        // Les messages non destinés aux humains sont ignorés... pour le moment. 
        if (joueur.estJoueurAutomatise()) {
            return;
        }

        StringBuilder texte = new StringBuilder();

        if (property.equals(Evenement.PARTIE_TERMINEE)) {
            texte.append("Score \n");
            for (Joueur unJoueur : modelePartie.getJoueurs()) {
                texte.append("Joueur: ");
                texte.append(unJoueur.getPersonnage().toString());
                texte.append(" ");
                texte.append(Integer.toString(unJoueur.calculerPoints()));
                texte.append(" points\n");
            }
            JOptionPane.showConfirmDialog(this, texte.toString(), "Pour votre information", JOptionPane.PLAIN_MESSAGE);
        } else if (property.equals(Evenement.NIVEAU)) {
            texte.append("Vous avez monté de niveau");
            JOptionPane.showConfirmDialog(this, texte.toString(), "Pour votre information", JOptionPane.PLAIN_MESSAGE);
        } else if (property.equals(Evenement.ERREUR_SELECTION_REQUISE)) {
            JOptionPane.showMessageDialog(this, "Vous devez choisir une carte dans l'offre", "Attention", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void locationSelectedOccured(PositionSelectionneeEvent event) {
        if (panelMenu.isVisible()) {
            return;
        }
        PositionGrille pos = event.getPositionSelectionnee();
        modelePartie.selectionnerTuile(pos);
        if (modelePartie.estPretAChanger()) {
            modelePartie.prochainePhase();
        }
    }
}
