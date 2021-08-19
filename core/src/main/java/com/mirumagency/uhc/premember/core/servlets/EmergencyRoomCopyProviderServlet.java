package com.mirumagency.uhc.premember.core.servlets;

import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.util.List;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;

@Component(service = Servlet.class, immediate = true,
    property = {
        ServletResolverConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_GET,
        ServletResolverConstants.SLING_SERVLET_PATHS + "=/apps/premember/dialog/dropdown/emergency-room-copy"
    })
@ServiceDescription("Emergency Room Copy Provider Servlet")
public class EmergencyRoomCopyProviderServlet extends AbstractCopyProviderServlet {


  @Override
  protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
      throws ServletException, IOException {

    super.doGet(request, response);
  }

  List<String> getContentFragmentModelProperties() {
    return ImmutableList.of("emergencyRoomPlanCopyOptionCopayThen", "emergencyRoomPlanCopyOptionAfterDeductible", "tieredPlanCopyOptionCopayCoinsuranceIsWaved");
  }
}
