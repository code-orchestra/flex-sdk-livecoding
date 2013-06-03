package codeOrchestra.lcs.license;

import java.util.prefs.Preferences;

/**
 * @author Alexander Eliseyev
 */
public class COLTRunningKey {
  
  public static boolean isRunning() {
    Preferences prefs = Preferences.userNodeForPackage(COLTRunningKey.class);
    return prefs.getBoolean("running", false);
  }

}
