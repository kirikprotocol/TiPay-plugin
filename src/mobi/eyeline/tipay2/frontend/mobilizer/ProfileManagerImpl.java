package mobi.eyeline.tipay2.frontend.mobilizer;

import mobi.eyeline.cfg.Configuration;
import mobi.eyeline.cfg.impl.MemoryConfiguration;
import mobi.eyeline.diameter.base.AVPDescriptor;
import mobi.eyeline.diameter.base.DiameterCommand;
import mobi.eyeline.diameter.base.DiameterException;
import mobi.eyeline.diameter.base.network.ClientApplication;
import mobi.eyeline.diameter.base.network.DiameterChannel;
import mobi.eyeline.pers.profile.PersonalProfile;
import mobi.eyeline.pers.profile.ProfilePropertyRequest;
import mobi.eyeline.pers.profile.commands.PersonalProfileAnswer;
import mobi.eyeline.pers.profile.commands.PersonalProfileRequest;
import mobi.eyeline.pers.protocol.PersonalizationClient;
import mobi.eyeline.pers.protocol.Property;
import mobi.eyeline.pers.protocol.attributes.PersonalizationAttributes;
import mobi.eyeline.pers.protocol.commands.*;
import mobi.eyeline.util.timer.Timers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ProfileManagerImpl implements ProfileManager {

  private static final Logger logger = LogManager.getLogger(ProfileManagerImpl.class);


  private static final String PROFILE_ATTRIBUTE = "mobi.eyeline.tipay2.frontend.mobilizer.ProfileAttribute";

  private PersonalizationClient personalizationClient;
  private ClientApplication diameterApplication;

  public static void main(String[] args) throws Exception {

    MemoryConfiguration config = new MemoryConfiguration("/memory");
    Configuration personalizationConfig = config.addChild("personalization");
    personalizationConfig.put("Application-Id", 26755);
    personalizationConfig.put("Origin-Realm", "tipay");
    personalizationConfig.put("Origin-Host", "localhost12");
    personalizationConfig.put("Destination-Realm", "pers");
    Configuration sessionsConfig = personalizationConfig.addChild("sessions");
    Configuration persConfig = sessionsConfig.addChild("pers");
    persConfig.put("remote", "192.168.1.156:6500");
    persConfig.put("Auth-Application-Id", "26755");

    ExecutorService executorService = Executors.newWorkStealingPool();
    ClientApplication app = new ClientApplication(personalizationConfig, executorService, Timers.configure(executorService)) {
      @Override
      public void handle(DiameterCommand command, DiameterChannel channel) throws DiameterException {
        System.out.println("Handling " + command + ", channel: " + channel);
      }
    };

    PersonalizationClient personalizationClient = new PersonalizationClient(personalizationConfig, app);
    personalizationClient.start();

    Thread.sleep(1000);

    // Пример синхронного чтения AVP из нескольких разрешенных realm'ов
    ProfileReference profileReference = PersonalProfile.reference("69328d09-e480-41a8-89d2-000000000001");
    PersonalProfileRequest personalProfileRequest = PersonalProfileRequest.create()
      .search(profileReference)
      .request(
        new ProfilePropertyRequest("person", "phones"),
        new ProfilePropertyRequest("tipay", "upin")
      );

    PersonalProfileAnswer personalProfileAnswer = app.request(personalProfileRequest);
    PersonalProfile personalProfile = personalProfileAnswer.getProfile(PersonalProfile::new);

    System.out.println("answer profile request: " + personalProfile);
    System.out.println("phones: " + personalProfile.getPhones());
    System.out.println("upin: " + personalProfile.getProperty("tipay", "upin"));


    // Пример асинхронной записи
    profileReference = PersonalProfile.reference("69328d09-e480-41a8-89d2-000000000001");

    ProfileUpdateRequest profileUpdateRequest = new ProfileUpdateRequest();
    profileUpdateRequest.setProfileReference(profileReference);
    profileUpdateRequest.setRealm("tipay");
    profileUpdateRequest.addProperty(new Property("upin", "7128"));

    app.submit(profileUpdateRequest, (request, answer) -> {
      System.out.println("answer update: " + answer);
    });

    // Пример асинхронного чтения всей совокупности AVP из перечисленных разрешенных realm'ов
    // profileReference можно переиспользовать после предыдущих запросов
    profileReference = PersonalProfile.reference("69328d09-e480-41a8-89d2-000000000002");
    personalProfileRequest = PersonalProfileRequest.create()
      .search(profileReference)
      .requestRealms("person", "tipay");

    app.submit(personalProfileRequest, (request, answer) -> {
      System.out.println("answer async profile request: " + answer);
      System.out.println("answer class: " + answer.getClass());
      PersonalProfile pp = ((PersonalProfileAnswer) answer).getProfile(PersonalProfile::new);
      System.out.println("personal profile: " + pp);
      System.out.println("phones: " + pp.getPhones());

    });

    Thread.sleep(1000);
    personalizationClient.shutdown();

  }

  public ProfileManagerImpl(PersonalizationClient client, ClientApplication app) {
    this.personalizationClient = client;
    this.diameterApplication = app;
  }

  @Override
  public PersonalProfile getProfile(String phone) throws DiameterException {
    PersonalProfileRequest ppr = PersonalProfileRequest.create()
      .search(PersonalProfile.reference(PersonalProfile.REALM_NAME, PersonalProfile.PHONES_PROPERTY, phone))
      .requestRealms("person", "tipay");
    PersonalProfileAnswer ppa = diameterApplication.request(ppr);
    PersonalProfile pp = ppa.getProfile(PersonalProfile::new);
    System.out.println("PersonalProfile: " + pp);
    // PersonalProfile: 69328d09-e480-41a8-89d2-000000000002:[]
    // PersonalProfile: 69328d09-e480-41a8-89d2-000000000001:[person:{phones=[79133333333]}, tipay:{card_ref=zxcvzxvxzcv, upin=7128}]
    // PersonalProfile: 69328d09-e480-41a8-89d2-000000000001:[person:{firstName=test, phones=[79133333333]}, tipay:{upin=7128}]
    // PersonalProfile: null
    return pp;
  }

  @Override
  public PersonalProfile createProfile(String uid, String phone) throws DiameterException {
    logger.debug("Creating profile for " + phone + ", uid: " + uid);
    ProfileUpdateRequest request = new ProfileUpdateRequest();
    request.setProfileReference(PersonalProfile.reference(uid));
    request.setRealm("person");
    request.addProperty(new Property("phones", Collections.singletonList(phone)));
    ProfileUpdateAnswer answer = diameterApplication.request(request);
    if (!answer.getResult().isSuccess()) throw new DiameterException("Profile update failed");
    return getProfile(phone);
  }

  @Override
  public void setPin(String uid, String pin) throws DiameterException {
    logger.debug("Setting pin for " + uid);
    ProfileUpdateRequest request = new ProfileUpdateRequest();
    request.setProfileReference(PersonalProfile.reference(uid));
    request.setRealm("tipay");
    request.addProperty(new Property("upin", createDigest(pin)));
    ProfileUpdateAnswer answer = diameterApplication.request(request);
    if (!answer.getResult().isSuccess()) throw new DiameterException("Profile update failed");
  }

  @Override
  public void saveProfileInSession(HttpServletRequest request, PersonalProfile pp) {
    request.getSession().setAttribute(PROFILE_ATTRIBUTE, pp);
  }

  public static String createDigest(String source) throws SecurityException {
    try {
      MessageDigest md = (MessageDigest) MessageDigest.getInstance("MD5").clone();
      md.update(source.getBytes());
      byte[] array = md.digest();
      if( array == null || array.length == 0 ) return null;
      return "{MD5}"+ Base64.getEncoder().encodeToString(array);
    } catch (CloneNotSupportedException e) {
      throw new SecurityException("Clone not supported", e);
    } catch (NoSuchAlgorithmException e) {
      throw new SecurityException("Algorithm not supported", e);
    }
  }


}

