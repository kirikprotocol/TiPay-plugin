package mobi.eyeline.tipay2.frontend.mobilizer.prequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import mobi.eyeline.tipay2.frontend.mobilizer.IllegalPaymentRequestException;
import mobi.eyeline.tipay2.frontend.mobilizer.SystemContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by boris on 23/03/17.
 */
public class PaymentRequest {

    private static final Logger logger = LogManager.getLogger(SystemContext.class);

    private String receiptUrl;
    private boolean commitable;
    private boolean silentResult;

    public PaymentRequest(HttpServletRequest request)
            throws IllegalPaymentRequestException {

        this.receiptUrl = getMandatoryParameter("request.receiptUrl", request);
        this.commitable = Boolean.parseBoolean(request.getParameter("request.commitable"));
        this.commitable = Boolean.parseBoolean(request.getParameter("request.silentResult"));

        String orderId = getMandatoryParameter("order.id", request);
        String orderAmount = getMandatoryParameter("order.amount", request);
        String merchantId = getMandatoryParameter("service", request);

        String user_id = getMandatoryParameter("user_id", request);
        String subscriber = getMandatoryParameter("subscriber", request);

        String merchantTrx = request.getParameter("merchant.trx");

        this.customer = new Subject();
        this.customer.setUid(user_id);
        this.customer.setMobile(subscriber);

        this.origin = new Subject();
        this.origin.setUid(merchantId);

        this.order = new Order();



        if (merchantTrx != null) {
            addParameter("merchantTrx", merchantTrx);
        }

    }


    @JsonProperty(value = "origin")
    protected Subject origin;

    @JsonProperty(value = "order")
    protected Order order;

    @JsonProperty(value = "customer")
    protected Subject customer;

    @JsonProperty(value = "recipient")
    protected Subject recipient;

    @JsonProperty(value = "payer")
    protected Subject payer;

    @JsonProperty
    protected Map<String,Object> parameters;


    public <T> T getParameter(String name) {
        return parameters == null ? null : (T)parameters.get(name);
    }

    public Map<String, Object> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    public Object addParameter(String name, Object value) {
        if( parameters == null ) parameters = new HashMap<>(1);
        return parameters.put(name, value);
    }





    public String getReceiptUrl() {
        return receiptUrl;
    }

    public void setReceiptUrl(String receiptUrl) {
        this.receiptUrl = receiptUrl;
    }

    public boolean isCommitable() {
        return commitable;
    }

    public void setCommitable(boolean commitable) {
        this.commitable = commitable;
    }

    public boolean isSilentResult() {
        return silentResult;
    }

    public void setSilentResult(boolean silentResult) {
        this.silentResult = silentResult;
    }


    private String getMandatoryParameter(String name, HttpServletRequest request)
            throws IllegalPaymentRequestException {
        String value = request.getParameter(name);
        if (value == null || value.length() == 0) {
            throw new IllegalPaymentRequestException(
                    "Mandatory parameter " + name + " is not present in request"
            );
        }
        return value;
    }

}

