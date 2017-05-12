<%@ page language="java" contentType="text/xml; charset=UTF-8"
%><?xml version="1.0" encoding="UTF-8"?>
<%@ page import="mobi.eyeline.diameter.base.DiameterException" %>
<%@ page import="mobi.eyeline.pers.profile.PersonalProfile" %>
<%@ page import="mobi.eyeline.tipay2.frontend.mobilizer.ProfileManager" %>
<%@ page import="mobi.eyeline.tipay2.frontend.mobilizer.SystemContext" %>
<%@ page import="mobi.eyeline.tipay2.frontend.mobilizer.prequest.PaymentContext" %>
<%@ page import="java.util.List" %>
<%@ page import="mobi.eyeline.pers.protocol.Property" %>

<%!
  private static final org.apache.logging.log4j.Logger log =
    org.apache.logging.log4j.LogManager.getLogger("mobi.eyeline.tipay2.frontend.mobilizer.index_jsp");
%>
<jsp:forward page="<%=execute(request)%>"/>
<%!
  private static String execute(HttpServletRequest request) throws DiameterException {
    PaymentContext paymentContext = new PaymentContext(request);
    request.getSession().setAttribute("PaymentContext", paymentContext);
    if (!paymentContext.hasMsisdn()) return "msisdn_verification.jsp";
    ProfileManager pm = SystemContext.get().getProfileManager();
    PersonalProfile profile = pm.getProfile(paymentContext.subscriber());
    if (profile == null) profile = pm.createProfile(
      SystemContext.get().getUidGenerator().generate(), paymentContext.subscriber());
    paymentContext.setUid(profile.getUid());
    Property pinProperty = profile.getProperty("tipay", "upin");
    String pin = pinProperty != null ? pinProperty.getValue() : null;
    Property cardRefProperty = profile.getProperty("tipay", "card_ref");
    String cardRef = cardRefProperty != null ? cardRefProperty.getValue() : null;
    paymentContext.setCardRef(cardRef);
    if (pin == null) return "define_pin.jsp";
    return "request_pin.jsp";
  }
%>


