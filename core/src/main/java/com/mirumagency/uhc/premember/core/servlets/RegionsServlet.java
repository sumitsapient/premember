package com.mirumagency.uhc.premember.core.servlets;

import com.mirumagency.uhc.premember.core.services.RegionsService;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.adobe.granite.rest.Constants.CT_JSON;


@Component(service=Servlet.class,
        property={
                ServletResolverConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_GET,
                ServletResolverConstants.SLING_SERVLET_PATHS + "=/apps/premember/federal/regions",
                ServletResolverConstants.SLING_SERVLET_EXTENSIONS + "=json"
        })
@ServiceDescription("Regions Servlet")
public class RegionsServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 6352522954783281443L;
    private static final Logger LOGGER = LoggerFactory.getLogger(RegionsServlet.class);


    @Reference
    private RegionsService regionsService;


    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response) {

        try {
            final String[] selectors = request.getRequestPathInfo().getSelectors();

            String zipCode = StringUtils.EMPTY;

            //Obtain zip code of selector
            if (selectors != null && selectors.length > 0) {
                zipCode = selectors[0];
            }

            //Get Region Map
            Map regions = regionsService.getRegions(zipCode);

            //Respond with JSON
            response.setContentType(CT_JSON);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
            Gson gson = new Gson();
            String regionJson = gson.toJson(regions);
            response.getWriter().print(regionJson);
        } catch (IOException ioe) {
            LOGGER.error("There was a problem constructing the response", ioe);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }
}
