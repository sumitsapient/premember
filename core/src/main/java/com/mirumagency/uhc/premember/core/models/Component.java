package com.mirumagency.uhc.premember.core.models;

import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

/**
 * Used to uniquely identify components on a page.
 */
@Model(adaptables = SlingHttpServletRequest.class,
        adapters = Component.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Component {

    @SlingObject
    private Resource resource;

    public String getId() {
        return NiceResource.of(resource).getId();
    }
}