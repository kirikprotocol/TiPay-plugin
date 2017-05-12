<%@ page language="java" contentType="text/html; charset=UTF-8"
%><%@ page import="mobi.eyeline.tipay2.frontend.mobilizer.SystemContext"
%><%@ page import="mobi.eyeline.tipay2.frontend.mobilizer.bean.Amount"
%><%@ page import="mobi.eyeline.tipay2.frontend.mobilizer.bean.PaymentResponse"
%><%@ page import="mobi.eyeline.tipay2.frontend.mobilizer.prequest.PaymentContext"
%><%@ page import="javax.ws.rs.client.Entity"
%><%@ page import="javax.ws.rs.core.MediaType"
%><%!
  private String execute(String paymentId, String amountString) {
    if (paymentId == null) return "payment.id is not set";
    PaymentContext paymentContext = SystemContext.get().paymentContext(paymentId);
    if (paymentContext == null) return "unknown payment.id";
    if (amountString != null && !amountString.matches("\\d+")) return "order.amount malformed";
    int amount = amountString == null ? paymentContext.getOrderAmount() : Integer.parseInt(amountString);
    if (amount < 0) return "order.amount can't be less than zero";
    if (amount > paymentContext.getOrderAmount()) return "order.amount can't be more than original amount";
    try {
      PaymentResponse paymentResponse = SystemContext.get().tipayTarget(paymentId + "/commit")
        .request().header("EY-ORIGIN", paymentContext.getService() + ":" + SystemContext.get().getMerchantId())
        .post(Entity.entity(Amount.create(amount), MediaType.APPLICATION_JSON_TYPE))
        .readEntity(PaymentResponse.class);
      return null;
    } catch (Throwable e) {
      return "unexpected error(" + e.getClass().getName() + "): " + e.getMessage();
    }
  }
%><%
  String result = execute(request.getParameter("payment.id"), request.getParameter("order.amount"));
  if (result == null) {
    result = "ok";
    response.setStatus(200);
  } else {
    response.setStatus(500);
  }
%><%=result%>