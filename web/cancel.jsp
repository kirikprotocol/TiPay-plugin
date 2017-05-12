<%@ page import="mobi.eyeline.tipay2.frontend.mobilizer.SystemContext" %>
<%@ page contentType="application/xml; charset=UTF-8" language="java" %>
<%
  final String serviceId = request.getParameter("serviceId");
  final String protocol = request.getParameter("protocol");
  final String wnumber = request.getParameter("user_id");
  SystemContext.get().push(serviceId, wnumber, protocol);
%>
<page><%--attributes="telegram.keep.session: true"--%>
  <div/>
  <navigation/>
</page>