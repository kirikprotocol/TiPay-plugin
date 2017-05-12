package mobi.eyeline.tipay2.frontend.mobilizer.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

/**
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentResponse {

  // {"paymentId":"58f0678d58fbdd2fa13e03ed","status":"ACCEPTED"}
  // {"paymentId":"58f0678d58fbdd2fa13e03ed","status":"PROCESSING","attachment":{"payload":{"data":{"className":"mobi.eyeline.tipay2.payment.system.bank.gazprom.GPBPayment"}}},"redirectURL":"http://localhost:8080/gpbtest/index.jsp?lang=RU&merch_id=MERCH_ID&o.mer_trx_id=58f0678d58fbdd2fa13e03ed&back_url_s=&back_url_f="}
  // {"paymentId":"5914ab80ebedb61c34ffa1b3","status":"DECLINED","statusReason":"unauthorized","statusInfo":"User pins mismatch"}

  @JsonProperty(required = true)
  String paymentId;

  @JsonProperty(required = true)
  String status;

  @JsonProperty
  String redirectURL;

  @JsonProperty
  String statusReason;

  @JsonProperty
  String statusInfo;

  public String getPaymentId() {
    return paymentId;
  }

  public String getStatus() {
    return status;
  }

  public String getRedirectURL() {
    return redirectURL;
  }

  public String getStatusReason() {
    return statusReason;
  }

  public String getStatusInfo() {
    return statusInfo;
  }

  @Override
  public String toString() {
    return "PaymentResponse{" +
      "paymentId='" + paymentId + '\'' +
      ", status='" + status + '\'' +
      ", redirectURL='" + redirectURL + '\'' +
      ", statusReason='" + statusReason + '\'' +
      ", statusInfo='" + statusInfo + '\'' +
      '}';
  }
}
