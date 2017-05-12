package mobi.eyeline.tipay2.frontend.mobilizer;

import mobi.eyeline.diameter.base.DiameterException;
import mobi.eyeline.pers.profile.PersonalProfile;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by boris on 23/03/17.
 */
public interface ProfileManager {

  PersonalProfile getProfile(String uid) throws DiameterException;
  PersonalProfile createProfile(String uid, String phone) throws DiameterException;
  void setPin(String uid, String pin) throws DiameterException;
  void saveProfileInSession(HttpServletRequest request, PersonalProfile pp);
}
