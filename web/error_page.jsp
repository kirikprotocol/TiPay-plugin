<%@ page isErrorPage="true" contentType="text/xml; charset=UTF-8"
  %><?xml version="1.0" encoding="UTF-8"?>
<%@ page import="java.io.PrintWriter"
%><%@ page import="java.io.StringWriter"
%><%@ page import="mobi.eyeline.tipay2.frontend.mobilizer.UnexpectedStatusException"
%><%@ page import="mobi.eyeline.tipay2.frontend.mobilizer.bean.PaymentResponse"
%><%!
  private static final org.apache.logging.log4j.Logger log =
    org.apache.logging.log4j.LogManager.getLogger("mobi.eyeline.tipay2.frontend.mobilizer.error_page_jsp");
%><%
  log.error("Uncaught exception", exception);

  response.setStatus(200);
  if (exception instanceof UnexpectedStatusException) {
    PaymentResponse pr = ((UnexpectedStatusException) exception).getPaymentResponse();
%>
<page>
  <div>Платёж не удался. Статус: <%=pr.getStatus()%>, причина: <%=pr.getStatusReason()%><%=pr.getStatusInfo() != null ? " (" + pr.getStatusInfo() + ")" : ""%></div>
  <navigation>
    <link accesskey="0" pageId="cancel.jsp">Назад</link>
  </navigation>
</page>
<%
  } else {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    exception.printStackTrace(printWriter);
    printWriter.close();
    stringWriter.close();
    String stackTrace = stringWriter.toString();
%>
<page>
  <!--
  This is message about error for SADS:
  exception.class: <%=exception.getClass().getName()%>
  exception.getMessage(): <%=exception.getMessage()%>
  StackTrace: <%=stackTrace%>
  -->
  <div>Сервис временно недоступен. Попробуйте позже</div>
  <navigation>
    <link accesskey="0" pageId="cancel.jsp">Назад</link>
  </navigation>
</page>
<% } %>
