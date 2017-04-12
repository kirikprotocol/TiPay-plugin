<%@ page language="java" contentType="text/xml; charset=UTF-8"
  %><?xml version="1.0" encoding="UTF-8"?>

<page>
  <attributes>
    <attribute name="msisdn-required" value="true"/>
    <attribute name="telegram.links.realignment.enabled" value="false"/>
  </attributes>
  <div>Необходимо подтверждение номера телефона</div>
  <navigation>
    <link pageId="http://plugins.miniapps.run/msisdn-verification?lang=ru&amp;success_url=index.jsp">Авторизоваться по C2S</link>
  </navigation>
  <navigation>
    <link pageId="http://plugins.miniapps.run/msisdn-verification?lang=ru&amp;success_url=index.jsp&amp;type=sms">Авторизоваться по СМС</link>
  </navigation>
  <navigation>
    <link pageId="http://plugins.miniapps.run/msisdn-verification?lang=ru&amp;success_url=index.jsp&amp;type=ussd_dialog">Авторизоваться по USSD</link>
  </navigation>
  <navigation>
    <link pageId="index.jsp">Продолжить</link>
  </navigation>
</page>
