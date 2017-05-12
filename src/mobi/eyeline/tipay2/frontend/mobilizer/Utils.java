package mobi.eyeline.tipay2.frontend.mobilizer;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zoldorn on 12.05.17.
 */
public class Utils {
  public static String urlToken(HttpServletRequest request, String token) {
    Matcher m = Pattern.compile(".*/payment/([0-9a-zA-Z]+)").matcher(request.getRequestURI());
    return m.matches() ? m.group(1) : null;

  }
}
