<%@ page language="java" contentType="text/xml; charset=UTF-8"
%><?xml version="1.0" encoding="UTF-8"?>
<%@ page import="mobi.eyeline.tipay2.frontend.mobilizer.SystemContext" %>
<%@ page import="javax.ws.rs.client.Entity" %>
<%@ page import="javax.ws.rs.core.MediaType" %>
<%@ page import="mobi.eyeline.tipay2.frontend.mobilizer.prequest.PaymentContext" %>
<%@ page import="mobi.eyeline.tipay2.frontend.mobilizer.bean.PaymentResponse" %>
<%@ page import="mobi.eyeline.tipay2.frontend.mobilizer.UnexpectedStatusException" %>
<%
  PaymentContext paymentContext = (PaymentContext) session.getAttribute("PaymentContext");
  String pin = request.getParameter("pin");
  if (pin != null) paymentContext.setPin(pin);
  if (paymentContext.getCardRef() != null) {
    PaymentResponse paymentResponse = SystemContext.get().tipayTarget("")
          .request().header("EY-ORIGIN", paymentContext.getService() + ":" + SystemContext.get().getMerchantId())
          .post(Entity.entity(paymentContext.createPaymentRequest(), MediaType.APPLICATION_JSON_TYPE))
          .readEntity(PaymentResponse.class);
    // TODO: here it is possible that we get DECLINED if pin is wrong

    System.out.println("paymentResponse: " + paymentResponse);
    if ("DECLINED".equals(paymentResponse.getStatus())) {
      // TODO: show reason
    } else {

    }
    if (!"ACCEPTED".equals(paymentResponse.getStatus())) throw new UnexpectedStatusException(paymentResponse);// TODO:...
    SystemContext.get().storePayment(paymentResponse.getPaymentId(), paymentContext);
    paymentResponse = SystemContext.get().tipayTarget(paymentResponse.getPaymentId() + "/preauth")
      .request().header("EY-ORIGIN", paymentContext.getService() + ":" + SystemContext.get().getMerchantId()) // TODO:...
        .post(Entity.entity(paymentContext.createPreauthRequest(), MediaType.APPLICATION_JSON_TYPE))
        .readEntity(PaymentResponse.class);
    if (!"PROCESSING".equals(paymentResponse.getStatus())) throw new RuntimeException();// TODO:...
    // TODO: in case commitable=true we should show message
%>
<page>
  <div/>
  <navigation/>
</page>
<%
  } else {
    PaymentResponse paymentResponse = SystemContext.get().tipayTarget("")
      .request().header("EY-ORIGIN", paymentContext.getService() + ":" + SystemContext.get().getMerchantId())
      .post(Entity.entity(paymentContext.createPaymentRequest(), MediaType.APPLICATION_JSON_TYPE))
      .readEntity(PaymentResponse.class);
    System.out.println("paymentResponse: " + paymentResponse);
    if (!"ACCEPTED".equals(paymentResponse.getStatus())) throw new RuntimeException();// TODO:...
    String url = SystemContext.get().getPluginUrl() + "payment/" + paymentResponse.getPaymentId();
    SystemContext.get().storePayment(paymentResponse.getPaymentId(), paymentContext);
    %>
<page>
  <div>
    Для оплаты перейдите по ссылке <%=url%>
  </div>
</page>
<% } %>
