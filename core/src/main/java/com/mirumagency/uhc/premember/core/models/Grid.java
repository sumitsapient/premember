package com.mirumagency.uhc.premember.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = Layout.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = Grid.RESOURCE_TYPE)
public class Grid extends WithVisibilityOptions implements Layout {

    public static final String RESOURCE_TYPE = "premember/components/structure/layout/grid";

    @ScriptVariable
    private Resource resource;

    @ValueMapValue
    private boolean sameHeight;

    @Override
    public boolean isSameHeight() {
        return sameHeight;
    }
}
