package com.mirumagency.uhc.premember.core.servlets;

import com.mirumagency.uhc.premember.core.exceptions.InvalidPremiumRateException;
import com.mirumagency.uhc.premember.core.services.migration.PremiumRatesMigratorService;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.HttpStatus;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
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
                ServletResolverConstants.SLING_SERVLET_PATHS + "=/apps/premember/federal/migration/premium-rates"
        })
@ServiceDescription("Premium Rates Migrator Servlet")
public class PremiumRatesMigratorServlet extends SlingAllMethodsServlet {
    private static final long serialVersionUID = -2897112454396148208L;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Reference
    private PremiumRatesMigratorService migratorService;

    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws IOException {
        try {
            String premiumRatesPath = request.getParameter("premiumRatesPath");
            int migratedFiles = migratorService.migrate(request.getResourceResolver(), premiumRatesPath);
            if (migratedFiles == 0) {
                writeResponse(response, HttpStatus.SC_BAD_REQUEST, "No files with premium rates were found.");
            } else {
                writeResponse(response, HttpStatus.SC_OK,
                        migratedFiles + " premium rates files were migrated successfully.");
            }
        } catch (InvalidPremiumRateException e) {
            logger.error("Invalid premium rate data exception: file: {} plan:{}", e.getFilePath(), e.getPlanName(), e);
            String fileName = FilenameUtils.getName(e.getFilePath());
            writeResponse(response, HttpStatus.SC_BAD_REQUEST,
                    "Invalid data in file: " + fileName + " and plan: " + e.getPlanName());
        } catch (FileNotFoundException e) {
            logger.error("Premium rates folder does not exist.", e);
            writeResponse(response, HttpStatus.SC_BAD_REQUEST, "Premium rates folder does not exist");
        } catch (Exception e) {
            logger.error("An uncontrolled error has occurred when trying to migrate premium rates.", e);
            writeResponse(response, HttpStatus.SC_INTERNAL_SERVER_ERROR,
                    "An error has occurred, please try again or contact the system administrator.");
        }
    }

    private void writeResponse(SlingHttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.getWriter().print(message);
    }
}
