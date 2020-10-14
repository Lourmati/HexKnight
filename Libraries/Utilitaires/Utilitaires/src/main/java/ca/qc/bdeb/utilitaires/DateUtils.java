package ca.qc.bdeb.utilitaires;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Éric Wenaas
 */
public class DateUtils {
  public static String now(String dateFormat) {
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
    return sdf.format(cal.getTime());
  }    
}
