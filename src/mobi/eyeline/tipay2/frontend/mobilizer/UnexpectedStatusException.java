package mobi.eyeline.tipay2.frontend.mobilizer;

import mobi.eyeline.tipay2.frontend.mobilizer.bean.PaymentResponse;

/**
 * Created by gev on 12.05.17.
 */
public class UnexpectedStatusException extends RuntimeException {
  private PaymentResponse paymentResponse;

  public UnexpectedStatusException(PaymentResponse paymentResponse) {
    super(paymentResponse.toString());
    this.paymentResponse = paymentResponse;
  }

  public PaymentResponse getPaymentResponse() {
    return paymentResponse;
  }
}
