package ca.qc.bdeb.utilitaires;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Eric Wenaas <eric.wenaas@bdeb.qc.ca>
 */
public class Minuterie extends Thread {
    
    private boolean minuterieActive;
    private long tempsRestantsEnMillis;
    
    public Minuterie(long tempsInitialEnMillis) {
        tempsRestantsEnMillis = tempsInitialEnMillis;
        minuterieActive = false;
        
    }

    @Override
    public void run() {
        long tempsDepart = System.currentTimeMillis();
        long tempsAnterieur = tempsDepart;
        while (tempsRestantsEnMillis > 0) {
            long tempsActuel = System.currentTimeMillis();
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(Minuterie.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (minuterieActive) {
                long tempsEcoule = tempsActuel - tempsAnterieur;
                tempsRestantsEnMillis -= tempsEcoule;
            }
            tempsAnterieur = tempsActuel;
        }
    }
    
    public long getTempsRestantEnMillis() {
        return tempsRestantsEnMillis;
    }
    
    public void setTempsRestantsEnMillis(long temps) {
        tempsRestantsEnMillis = temps;
    }
        
    public void setMinuterieActive(boolean valeur) {
        minuterieActive = valeur;
    }
    
    public boolean isActive() {
        return minuterieActive;
    }
}
