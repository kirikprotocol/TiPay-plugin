package mobi.eyeline.tipay2.frontend.mobilizer;

import mobi.eyeline.cfg.Configuration;
import mobi.eyeline.cfg.ConfigurationManager;
import mobi.eyeline.cfg.spi.ConfigurationService;
import mobi.eyeline.diameter.base.DiameterCommand;
import mobi.eyeline.diameter.base.DiameterException;
import mobi.eyeline.diameter.base.Vendor;
import mobi.eyeline.diameter.base.network.ClientApplication;
import mobi.eyeline.diameter.base.network.DiameterChannel;
import mobi.eyeline.pers.data.UIDGenerator;
import mobi.eyeline.pers.data.UUIDGenerator;
import mobi.eyeline.pers.protocol.PersonalizationClient;
import mobi.eyeline.tipay2.frontend.mobilizer.prequest.PaymentContext;
import mobi.eyeline.util.timer.Timers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@WebListener
public class SystemContext implements ServletContextListener {

  private static final Logger logger = LogManager.getLogger(SystemContext.class);


  private static SystemContext instance;
  ConfigurationManager configurationManager;
  Locale currentLocale = new Locale("ru");
  UIDGenerator uidGenerator;
  String mobilizerUrl;
  String tipayUrl;
  PersonalizationClient personalizationClient;
  ProfileManager profileManager;
  private Client client;
  private ConcurrentHashMap<String, PaymentContext> paymentMap;// TODO: this should be cache
  private String pluginUrl;
  private String merchantId;


  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    try {
      System.out.println("Initializing frontend");
      String uri = servletContextEvent.getServletContext().getInitParameter("configuration.uri");
      if (uri == null) uri = System.getProperties().getProperty("configuration.uri");
      if (uri == null) throw new RuntimeException("configuration.uri init parameter missed");
      configurationManager = ConfigurationService.manager(ConfigurationService.parseRepositoryUri(uri));
      if (!configurationManager.startAndWaitForReady(1, TimeUnit.MINUTES)) {
        throw new mobi.eyeline.cfg.ConfigurationException("Could not connect to configuration manager");
      }
      Configuration config = configurationManager.getConfiguration(ConfigurationService.extractName(uri));
      Timers.configureAndStart(configurationManager.getExecutor());
      if (Vendor.getBase() == null) throw new Exception("Diameter subsystem failed to init");

      ExecutorService executorService = Executors.newWorkStealingPool();
      ClientApplication app = new ClientApplication(config, executorService, Timers.configure(executorService)) {
        @Override
        public void handle(DiameterCommand command, DiameterChannel channel) throws DiameterException {
          logger.warn("Received command: " + command + " from channel: " + channel);
        }
      };
      personalizationClient = new PersonalizationClient(config, app);
      personalizationClient.start();
      profileManager = new ProfileManagerImpl(personalizationClient, app);

      mobilizerUrl = config.getString("Mobilizer-Url");
      tipayUrl = config.getString("Tipay-Url");
      pluginUrl = config.getString("Plugin-Url");
      merchantId = config.getString("Merchant-Id");
      logger.debug("Mobilizer-Url: " + mobilizerUrl + "; Tipay-Url: " + tipayUrl + "; pluginUrl: " + pluginUrl);

      uidGenerator = new UUIDGenerator();
      client = ClientBuilder.newClient();
      paymentMap = new ConcurrentHashMap<>();

    } catch (Exception e) {
      logger.error("Initialization error", e);
      contextDestroyed(servletContextEvent);
      throw new RuntimeException("Could not init context", e);
    }
    instance = this;
    logger.info("Initialization completed");
  }

  public static SystemContext get() {
    return instance;
  }

  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {
    instance = null;
    personalizationClient.shutdown();
    Timers.shutdown();
    if (configurationManager != null) configurationManager.close();
  }

  public String getMobilizerUrl() {
    return mobilizerUrl;
  }

  public String getTipayUrl() {
    return tipayUrl;
  }

  public String getPluginUrl() {
    return pluginUrl;
  }

  public PersonalizationClient getPersonalizationClient() {
    return personalizationClient;
  }

  public UIDGenerator getUidGenerator() {
    return uidGenerator;
  }

  public ConfigurationManager getConfigurationManager() {
    return configurationManager;
  }

  public ProfileManager getProfileManager() {
    return profileManager;
  }

  public WebTarget tipayTarget(String path) {
    return client.target(tipayUrl + path);
  }

  public String getMerchantId() {
    return merchantId;
  }

  public void storePayment(String paymentId, PaymentContext paymentContext) {
    paymentContext.setPaymentId(paymentId);
    paymentMap.put(paymentId, paymentContext);
  }

  public PaymentContext paymentContext(String id) {
    return paymentMap.get(id);
  }

  public void push(String serviceId, String wnumber, String protocol) {
    String sadsResponse = client.target(mobilizerUrl)
      .queryParam("service", serviceId)
      .queryParam("subscriber", wnumber)
      .queryParam("protocol", protocol)
      .queryParam("scenario", "default-noinform")
      .request().get(String.class);
    System.out.println("sadsResponse: " + sadsResponse);
  }
}
