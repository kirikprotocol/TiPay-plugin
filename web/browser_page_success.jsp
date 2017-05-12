<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="mobi.eyeline.tipay2.frontend.mobilizer.SystemContext" %>
<%@ page import="mobi.eyeline.tipay2.frontend.mobilizer.prequest.PaymentContext" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.regex.Pattern" %>
<%@ page import="java.util.regex.Matcher" %>

<%!
  private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
  private static final Pattern URI_PATTERN = Pattern.compile(".*/success/([0-9a-zA-Z]+)");
%>
<%
  Matcher m = URI_PATTERN.matcher(request.getRequestURI());
  String id = m.matches() ? m.group(1) : request.getParameter("paymentId");
  PaymentContext c = SystemContext.get().paymentContext(id);
%>
<html>
<head>
  <title>Платёж успешен</title>
</head>
<body>

<div>Информация о платеже</div>
<table>
  <tr>
    <td><b>Сумма платежа:</b></td>
    <td><%=c.getOrderAmount()%> руб</td>
  </tr>
  <tr>
    <td><b>Идентификатор транзакции:</b></td>
    <td><%=c.orderId()%></td>
  </tr>
  <tr>
    <td><b>Дата/время:</b></td>
    <td><%=sdf.format(new Date())%></td>
  </tr>
  <tr>
    <td><b>Результат:</b></td>
    <td><font color="green">Платеж завершен</font></td>
  </tr>
</table>
</body>
</html>
