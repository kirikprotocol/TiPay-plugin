<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="mobi.eyeline.tipay2.frontend.mobilizer.SystemContext" %>
<%@ page import="mobi.eyeline.tipay2.frontend.mobilizer.prequest.PaymentContext" %>
<%@ page import="javax.ws.rs.client.Entity" %>
<%@ page import="javax.ws.rs.core.MediaType" %>
<%@ page import="mobi.eyeline.tipay2.frontend.mobilizer.bean.PaymentResponse" %>
<%@ page import="java.util.regex.Pattern" %>
<%@ page import="java.util.regex.Matcher" %>
<%@ page import="mobi.eyeline.tipay2.frontend.mobilizer.Utils" %>
<%
  String id = Utils.urlToken(request, "payment");
  PaymentContext paymentContext = SystemContext.get().paymentContext(id);
  PaymentResponse paymentResponse = SystemContext.get().tipayTarget(id + "/preauth")
    .request().header("EY-ORIGIN", paymentContext.getService() + ":" + SystemContext.get().getMerchantId())
    .post(Entity.entity(paymentContext.createPreauthRequest(), MediaType.APPLICATION_JSON_TYPE))
    .readEntity(PaymentResponse.class);
  response.sendRedirect(paymentResponse.getRedirectURL());
%>