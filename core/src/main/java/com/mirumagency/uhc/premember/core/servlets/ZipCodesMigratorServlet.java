package com.mirumagency.uhc.premember.core.servlets;

import com.mirumagency.uhc.premember.core.services.migration.ZipCodesMigratorService;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.FileNotFoundException;
import java.io.IOException;


@Component(service = Servlet.class, immediate = true,
        property = {
                ServletResolverConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_POST,
                ServletResolverConstants.SLING_SERVLET_PATHS + "=/apps/premember/federal/migrator/zipcodes"
        })
@ServiceDescription("ZipCode Migrator Provider Servlet")
public class ZipCodesMigratorServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Reference
    private ZipCodesMigratorService zipcodesMigratorService;


    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws IOException {
        try {
            ResourceResolver resourceResolver = request.getResourceResolver();
            String filePath = request.getParameter("zipCodesFilePath");
            String migrationPath = request.getParameter("zipCodesMigrationPath");
            int numberOfRegionsMigrated = zipcodesMigratorService.migrationProcess(resourceResolver, filePath, migrationPath);
            String responseMessage = zipcodesMigratorService.getResponseMessage();

            if (numberOfRegionsMigrated == 0) {
                writeResponse(response, HttpStatus.SC_BAD_REQUEST,
                        responseMessage.concat("No Zip Codes per region Migrated."));
            } else {
                writeResponse(response, HttpStatus.SC_OK, responseMessage);
            }
        } catch (FileNotFoundException e) {
            logger.error("Zip codes Excel File does not exist.", e);
            writeResponse(response, HttpStatus.SC_BAD_REQUEST, "Zip codes Excel File does not exist.");
        } catch (Exception e) {
            logger.error("An uncontrolled error has occurred when trying to migrate zip codes per region.", e);
            writeResponse(response, HttpStatus.SC_INTERNAL_SERVER_ERROR,
                    "An error has occurred, please try again or contact the system administrator.");
        }
    }

    private void writeResponse(SlingHttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.getWriter().print(message);
    }
}
