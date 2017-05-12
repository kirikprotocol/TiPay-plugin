package mobi.eyeline.tipay2.frontend.mobilizer.prequest;

import mobi.eyeline.tipay2.frontend.mobilizer.SystemContext;
import mobi.eyeline.tipay2.frontend.mobilizer.bean.PaymentRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by boris on 23/03/17.
 */
public class PaymentContext {

  private static final Logger logger = LogManager.getLogger(SystemContext.class);
  private final String subscriber;
  private final String service;
  private final String orderId;
  private final String orderName;
  private final int orderAmount;
  private final String receiptUrl;
  private String uid;
  private String pin;
  private String cardRef;
  private String paymentId;

  public PaymentContext(HttpServletRequest request) {
    subscriber = request.getParameter("subscriber");
    service = request.getParameter("service");
    orderId = request.getParameter("order.id");
    orderName = request.getParameter("order.name");
    System.out.println("orderName: " + orderName);
    orderAmount = Integer.parseInt(request.getParameter("order.amount"));
    receiptUrl = request.getParameter("receipt.url");
  }

  public boolean hasMsisdn() {
    return subscriber.matches("\\d{8,20}");
  }

  public String subscriber() {
    return subscriber;
  }

  public String orderId() {
    return orderId;
  }

  public int getOrderAmount() {
    return orderAmount;
  }

  public String getOrderName() {
    return orderName;
  }

  public PaymentRequest createPaymentRequest() {
    return PaymentRequest.createPayment(orderId, orderName, orderAmount, uid, pin);
  }

  public PaymentRequest createPreauthRequest() {
    return PaymentRequest.createPreauth(receiptUrl, paymentId);
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public String getUid() {
    return uid;
  }

  public void setPin(String pin) {
    this.pin = pin;
  }

  public void setCardRef(String cardRef) {
    this.cardRef = cardRef;
  }

  public String getCardRef() {
    return cardRef;
  }

  public void setPaymentId(String paymentId) {
    this.paymentId = paymentId;
  }

  public String getPaymentId() {
    return paymentId;
  }

  public String getService() {
    return service;
  }

}

