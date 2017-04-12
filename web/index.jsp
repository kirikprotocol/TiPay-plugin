<%@ page import="mobi.eyeline.tipay2.frontend.mobilizer.prequest.PaymentRequest" %>
<%@ page import="mobi.eyeline.tipay2.frontend.mobilizer.IllegalPaymentRequestException" %>
<%@ page import="mobi.eyeline.tipay2.frontend.mobilizer.ProfileManager" %>
<%@ page import="mobi.eyeline.tipay2.frontend.mobilizer.SystemContext" %>
<%@ page import="mobi.eyeline.pers.profile.PersonalProfile" %>
<%@ page import="mobi.eyeline.diameter.base.DiameterException" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/xml; charset=UTF-8"
%><?xml version="1.0" encoding="UTF-8"?>
<%!
    private static final org.apache.logging.log4j.Logger log =
            org.apache.logging.log4j.LogManager.getLogger("mobi.eyeline.tipay2.frontend.mobilizer.index_jsp");
%>


<%
%>


<jsp:forward page="<%=execute(request)%>"/>

<%!
    private static String execute(HttpServletRequest request) throws DiameterException {

        try {
            PaymentRequest ppr = new PaymentRequest(request);
        } catch (IllegalPaymentRequestException e) {
            log.warn("wrong payment request", e);
            //TODO
            return "wrong_request.jsp";
        }

        ProfileManager pm = SystemContext.getInstance().getProfileManager();

        String uid = request.getParameter("user_id");

        PersonalProfile pp = pm.getProfile(uid);

        List<String> phones = pp.getPhones();

        String phone = phones != null && !phones.isEmpty() ? phones.get(0) : null;

        pm.saveProfileInSession(request, pp);


        log.debug("phone: " + phone);
        if (phone != null && isMsisdn(phone)) {
            return "define_pin.jsp";
        } else {
            return "msisdn_verification.jsp";
        }

    }

    private static boolean isMsisdn(String subscriber) {
        return subscriber.matches("\\d{8,20}");
    }

%>


