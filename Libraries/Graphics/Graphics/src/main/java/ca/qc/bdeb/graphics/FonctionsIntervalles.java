package ca.qc.bdeb.graphics;

// TODO: Cette fonction n'a rien à faire dans le paquetage graphique. Remettre dans Utilitaires.

/**
 *
 * @author Eric Wenaas <eric.wenaas@bdeb.qc.ca>
 */
public class FonctionsIntervalles {
    
    public static boolean estDansPrecision(long value, long expected, long precision) {
        long min = expected - precision;
        long max = expected + precision;
        return value >= min && value <= max;
    }

    public static int faireDansIntervalle(int min, int max, int value) {
        int answer = value;
        answer = answer < min ? min : answer;
        answer = answer > max ? max : answer;
        return answer;
    }
}
