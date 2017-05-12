<%@ page language="java" contentType="text/xml; charset=UTF-8"
  %><?xml version="1.0" encoding="UTF-8"?>

<%
  String pin = request.getParameter("pin");
  session.setAttribute("pin", pin);
%>
<page>
  <div>
    <input navigationId="submit" name="pin_confirm" title="Подтвердите PIN код"/>
  </div>
  <navigation id="submit">
    <link accesskey="1" pageId="define_pin_set.jsp">Ok</link>
  </navigation>
</page>
