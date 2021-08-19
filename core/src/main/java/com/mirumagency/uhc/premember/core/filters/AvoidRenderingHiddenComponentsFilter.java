package com.mirumagency.uhc.premember.core.filters;

import com.mirumagency.uhc.premember.core.models.WithVisibilityOptions;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.engine.EngineConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.component.propertytypes.ServiceRanking;

import javax.servlet.*;
import java.io.IOException;
import java.util.Optional;

/**
 * This filter avoids rendering components which should be hidden based on theirs configuration.
 *
 * @see WithVisibilityOptions
 * <p>
 * In our case we wanted to add our class before executing the WCMComponentFilter. The "order" should then be above 200
 * since WCMComponentFilter has rank 200. A higher rank means its executed earlier.
 */
@ServiceRanking(999)
@Component(service = Filter.class,
        property = {
                EngineConstants.SLING_FILTER_SCOPE + "=" + EngineConstants.FILTER_SCOPE_COMPONENT,
        })
@ServiceDescription("This filter avoids rendering components which should be hidden based on theirs configuration.")
public class AvoidRenderingHiddenComponentsFilter implements Filter {

    public static final String PREMEMBER_COMPONENTS_PREFIX = "premember/components";

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (shouldThisComponentBeHidden(request)) {
            return;
        }
        chain.doFilter(request, response);
    }

    private boolean shouldThisComponentBeHidden(ServletRequest request) {
        if (!isASlingRequest(request)) {
            return false;
        }
        final SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) request;
        if (notAPrememberComponent(slingRequest)) {
            return false;
        }
        return shouldVisibilityOptionsHideTheComponent(slingRequest);
    }

    private boolean shouldVisibilityOptionsHideTheComponent(SlingHttpServletRequest slingRequest) {
        final Optional<WithVisibilityOptions> withVisibilityOptions = Optional.ofNullable(slingRequest.adaptTo(WithVisibilityOptions.class));
        return withVisibilityOptions
                .map(it -> !it.isVisible())
                .orElse(false);
    }

    private boolean isASlingRequest(ServletRequest request) {
        return request instanceof SlingHttpServletRequest;
    }

    private boolean notAPrememberComponent(SlingHttpServletRequest slingRequest) {
        final Resource resource = slingRequest.getResource();
        return !resource.getResourceType().startsWith(PREMEMBER_COMPONENTS_PREFIX);
    }

    @Override
    public void destroy() {
    }
}