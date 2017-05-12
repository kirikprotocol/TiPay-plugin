<%@ page language="java" contentType="text/xml; charset=UTF-8"
%><?xml version="1.0" encoding="UTF-8"?>
<%@ page import="mobi.eyeline.tipay2.frontend.mobilizer.SystemContext" %>
<%@ page import="mobi.eyeline.tipay2.frontend.mobilizer.prequest.PaymentContext" %>
<%
  String pin = (String) session.getAttribute("pin");
  String pinConfirm = request.getParameter("pin_confirm");
  if (pinConfirm.equals(pin)) {
    PaymentContext paymentContext = (PaymentContext) session.getAttribute("PaymentContext");
    SystemContext.get().getProfileManager().setPin(paymentContext.getUid(), pin);
    paymentContext.setPin(pin);
%>
<page>
  <div>
    Вы подключились к системе TiPay. Подробности на tipay.ru
  </div>
  <navigation>
    <link accesskey="1" pageId="payment.jsp">Оплатить</link>
  </navigation>
</page>
<% } else { %>
  <page>
    <div>
      PIN-ы не совпадают
    </div>
    <navigation>
      <link accesskey="1" pageId="define_pin.jsp">Попробовать ещё</link>
      <link accesskey="0" pageId="cancel.jsp">Отмена</link>
    </navigation>
  </page>
<% } %>
