package com.mirumagency.uhc.premember.core.services.repository.jcr.resources;

import com.mirumagency.uhc.premember.core.exceptions.ResourceGentlemanException;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component(service = ResourceGentleman.class)
public class JcrResourceGentleman implements ResourceGentleman {

    private static final String SUBSERVICE_NAME = "resourceGentleman";

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Override
    public <T> T withResolver(Function<NiceResourceResolver, T> function) {
        try (NiceResourceResolver resourceResolver = createResourceResolver()) {
            return function.apply(resourceResolver);
        }
    }

    private NiceResourceResolver createResourceResolver() {
        try {
            return NiceResourceResolver.of(resolverFactory.getServiceResourceResolver(authInfo()));
        } catch (LoginException e) {
            throw new ResourceGentlemanException("Cannot instantiate resource resolver", e);
        }
    }

    private Map<String, Object> authInfo() {
        return new HashMap<String, Object>() {{
            put(ResourceResolverFactory.SUBSERVICE, SUBSERVICE_NAME);
        }};
    }
}
