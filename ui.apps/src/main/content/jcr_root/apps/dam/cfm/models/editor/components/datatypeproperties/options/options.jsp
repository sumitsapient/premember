<%--
  ADOBE CONFIDENTIAL
  __________________
   Copyright 2017 Adobe Systems Incorporated
   All Rights Reserved.
  NOTICE:  All information contained herein is, and remains
  the property of Adobe Systems Incorporated and its suppliers,
  if any.  The intellectual and technical concepts contained
  herein are proprietary to Adobe Systems Incorporated and its
  suppliers and are protected by trade secret or copyright law.
  Dissemination of this information or reproduction of this material
  is strictly forbidden unless prior written permission is obtained
  from Adobe Systems Incorporated.
--%><%
%><%@include file="/libs/granite/ui/global.jsp" %><%
%><%@ page session="false" contentType="text/html" pageEncoding="utf-8"
           import="com.adobe.granite.ui.components.formbuilder.FormResourceManager,
         		 org.apache.sling.api.resource.Resource,
         		 org.apache.sling.api.resource.ValueMap,
                 com.adobe.granite.ui.components.Config,
         		 java.util.HashMap" %><%
    ValueMap fieldProperties = resource.adaptTo(ValueMap.class);
    Config cfg = new Config(resource);
    String nameBase = xssAPI.encodeForHTMLAttr("./content/items/" + resource.getName());
    HashMap<String, Object> values = new HashMap<String, Object>();
    values.put("granite:class", "field-text-descriptor");
    values.put("fieldLabel",   i18n.get("Options"));
    // Overriding content fragments option input to support option separation using semicolon (;) instead of a comma (,).
    // Only this one line changed:
    // Begin
    values.put("emptyText",    i18n.get("Semicolon separated list of options"));
    // End
    values.put("value",        cfg.get("options", ""));
    values.put("name",         nameBase + "/options");
    FormResourceManager formResourceManager = sling.getService(FormResourceManager.class);
    Resource labelFieldResource = formResourceManager.getDefaultPropertyFieldResource(resource, values);

%><sling:include resource="<%= labelFieldResource %>" resourceType="granite/ui/components/coral/foundation/form/textfield"/>
<input type="hidden" name="<%= nameBase %>/datasource/sling:resourceType"
       value="dam/cfm/admin/components/datasources/optionrenderer">
<input type="hidden" name="<%= nameBase %>/datasource/variant" value="default">
<input type="hidden" name="<%= nameBase %>/emptyOption" value="true">
<input type="hidden" name="<%= nameBase %>/emptyOption@TypeHint" value="Boolean">