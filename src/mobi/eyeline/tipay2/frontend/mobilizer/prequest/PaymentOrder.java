package mobi.eyeline.tipay2.frontend.mobilizer.prequest;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by boris on 23/03/17.
 */
public class PaymentOrder {

    public PaymentOrder(HttpServletRequest request) {
    }

    String receiptUrl;
    boolean commitable;
    boolean silentResult;

    @JsonProperty(value = "origin")
    String origin;

    @JsonProperty(value = "prequest")
    Order order;

    @JsonProperty(value = "customer")
    Subject customer;

    @JsonProperty(value = "recipient")
    Subject recipient;

    @JsonProperty(value = "payer")
    Subject payer;

    @JsonProperty(value = "parameters")
    Parameter[] parameters;





}
