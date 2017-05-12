package mobi.eyeline.tipay2.frontend.mobilizer.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import mobi.eyeline.tipay2.frontend.mobilizer.SystemContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentRequest {

  @JsonProperty
  private Order order;

  @JsonProperty
  private Subject customer;

  @JsonProperty
  private Map<String, Object> parameters;

  public static PaymentRequest createPayment(String orderId, String orderName, int orderAmount, String userId, String pin) {
    PaymentRequest r = new PaymentRequest();
    r.order = new Order();
    r.order.id = orderId;
    r.order.amount = orderAmount;
    r.order.items = new ArrayList<>();
    OrderItem item = new OrderItem();
    item.name = orderName;
    item.price = orderAmount;
    r.order.items.add(item);
    r.customer = new Subject();
    r.customer.userId = userId;
    r.parameters = new HashMap<>();
    r.parameters.put("upin", pin);
    return r;
  }

  public static PaymentRequest createPreauth(String receiptUrl, String paymentId) {
    PaymentRequest r = new PaymentRequest();
    r.parameters = new HashMap<>();
    r.parameters.put("successURL", SystemContext.get().getPluginUrl() + "success/" + paymentId);
    r.parameters.put("failuretURL", SystemContext.get().getPluginUrl() + "failure/" + paymentId);
    r.parameters.put("receiptURL", receiptUrl);
    return r;
  }

  public static class Order {

    @JsonProperty(required = true)
    protected String id;

    @JsonProperty(required = true)
    protected int amount;

    @JsonProperty
    protected List<OrderItem> items;

  }

  public static class OrderItem {
    @JsonProperty
    protected String name;

    @JsonProperty
    protected int price;

  }

  public static class Subject {
    @JsonProperty("uid")
    protected String userId;
    @JsonProperty
    protected String email;
    @JsonProperty
    protected String mobile;
  }
}
