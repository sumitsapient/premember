package com.mirumagency.uhc.premember.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;

import javax.annotation.PostConstruct;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = Layout.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = Column.RESOURCE_TYPE)
public class Column extends WithVisibilityOptions implements Layout {

    public static final String RESOURCE_TYPE = "premember/components/structure/layout/column";
    private static final String SAME_HEIGHT_PROPERTY = "sameHeight";

    @ScriptVariable
    private Resource resource;

    private boolean sameHeight = false;

    @PostConstruct
    protected void initModel()
    {
        Resource gridResource = resource.getParent();
        if(gridResource != null && gridResource.isResourceType(Grid.RESOURCE_TYPE))
        {
            ValueMap gridProperties = gridResource.getValueMap();
            sameHeight = gridProperties.get(SAME_HEIGHT_PROPERTY, false);
        }
    }
    @Override
    public boolean isSameHeight() {
        return sameHeight;
    }
}
