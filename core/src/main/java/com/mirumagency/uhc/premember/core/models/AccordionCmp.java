package com.mirumagency.uhc.premember.core.models;

import com.mirumagency.uhc.premember.core.exceptions.RequiredResourceNotFoundException;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = AccordionCmp.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AccordionCmp extends WithVisibilityOptions {

    private static final String PARENT_ID_TEMPLATE = "#%s";

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String icon;

    @ValueMapValue
    private String iconVariation;

    @ValueMapValue
    private boolean hideIcon;

    @ScriptVariable
    private Resource resource;

    private String parentId;
    private String titleType;

    @PostConstruct
    public void init() {
        NiceResource accordionGroup = NiceResource.of(resource).getParentOrThrow();
        if (!accordionGroup.isResourceType(AccordionGroup.RESOURCE_TYPE)) {
            throw new RequiredResourceNotFoundException("Parent resource of accordionCmp is not an " + AccordionGroup.RESOURCE_TYPE + " but: " + accordionGroup.getResourceType());
        }
        titleType = accordionGroup.getString(AccordionGroup.TITLE_TYPE);
        parentId = String.format(PARENT_ID_TEMPLATE, accordionGroup.getId());
    }

    public String getTitle() {
        return tokenizeInViewMode(title);
    }

    public String getIcon() {
        return icon;
    }

    public String getIconVariation() {
        return iconVariation;
    }

    public String getTitleType() {
        return titleType;
    }

    public String getParentId() {
        return parentId;
    }

    public boolean isHideIcon() {
        return hideIcon;
    }
}