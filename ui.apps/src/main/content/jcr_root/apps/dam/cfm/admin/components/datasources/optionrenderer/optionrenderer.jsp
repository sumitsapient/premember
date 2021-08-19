<%--
  ADOBE CONFIDENTIAL

  Copyright 2017 Adobe Systems Incorporated
  All Rights Reserved.

  NOTICE:  All information contained herein is, and remains
  the property of Adobe Systems Incorporated and its suppliers,
  if any.  The intellectual and technical concepts contained
  herein are proprietary to Adobe Systems Incorporated and its
  suppliers and may be covered by U.S. and Foreign Patents,
  patents in process, and are protected by trade secret or copyright law.
  Dissemination of this information or reproduction of this material
  is strictly forbidden unless prior written permission is obtained
  from Adobe Systems Incorporated.
--%><%
%>
<%@include file="/libs/granite/ui/global.jsp" %>
<%
%>
<%@page session="false"
        import="com.adobe.granite.ui.components.ds.DataSource,
                com.adobe.granite.ui.components.ds.SimpleDataSource,
                com.adobe.granite.ui.components.ds.ValueMapResource,
                org.apache.commons.collections.Transformer,
                org.apache.commons.collections.iterators.TransformIterator,
                org.apache.sling.api.resource.ResourceMetadata,
                org.apache.sling.api.resource.ResourceResolver,
                org.apache.sling.api.resource.ValueMap,
                org.apache.sling.api.wrappers.ValueMapDecorator,
                java.util.ArrayList,
                java.util.Arrays,
                java.util.HashMap,
                java.util.List" %>
<%

    final ResourceResolver resolver = resourceResolver;
    ValueMap props = resource.getValueMap();
    String optionsStr = props.get("options", "");
    // Overriding content fragments option input to support option separation using semicolon (;) instead of a comma (,).
    // Only this one line changed:
    // Begin
    String[] optionsArray = optionsStr.split(";");
    // End
    List<String> options = new ArrayList<String>(4);
    options.addAll(Arrays.asList(optionsArray));

    @SuppressWarnings("unchecked")
    DataSource ds = new SimpleDataSource(new TransformIterator(options.iterator(), new Transformer() {
        public Object transform(Object input) {
            try {
                String text = ((String) input).trim();
                ValueMap vm = new ValueMapDecorator(new HashMap<String, Object>());

                vm.put("value", text);
                vm.put("text", text);

                return new ValueMapResource(
                        resolver, new ResourceMetadata(), "nt:unstructured", vm);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }));

    request.setAttribute(DataSource.class.getName(), ds);

%>