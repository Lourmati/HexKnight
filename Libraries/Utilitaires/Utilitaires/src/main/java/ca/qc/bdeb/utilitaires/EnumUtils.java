package ca.qc.bdeb.utilitaires;

/**
 *
 * @author
 * @version 0.4
 */
public class EnumUtils {
    
    public static boolean hasValue(Enum<?>[] values, String value) {
        for (Enum<?> e: values) {
            if (e.toString().equals(value)) {
                return true;
            }
        }
        
        return false;
    }
}
