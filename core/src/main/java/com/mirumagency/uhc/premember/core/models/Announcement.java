package com.mirumagency.uhc.premember.core.models;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

import static org.apache.commons.lang.StringUtils.isNotEmpty;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = Announcement.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Announcement extends WithVisibilityOptions {

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String titleType;

    @ValueMapValue
    private boolean alwaysDisplay;

    @ValueMapValue
    private boolean greyBorder;

    private Boolean imageEmpty = true;

    private Boolean textEmpty = true;

    private Boolean buttonEmpty = true;

    private Boolean pdfButtonEmpty = true;

    @ChildResource
    private Resource image;

    @ChildResource
    private Resource text;

    @PostConstruct
    protected void initModel() {
        if (image != null) {
            imageEmpty = isPropertyEmpty(image, "fileReference");
        }

        if (text != null) {
            textEmpty = isPropertyEmpty(text, "text");
        }
    }

    private boolean isPropertyEmpty(Resource resource, String propertyName) {
        ValueMap resourceProperties = resource.getValueMap();
        String propertyValue = resourceProperties.get(propertyName, String.class);
        if (StringUtils.isNotBlank(propertyValue)) {
            return false;
        }
        return true;
    }

    public Boolean isImageNotEmpty() {
        return !imageEmpty;
    }

    public Boolean isTextNotEmpty() {
        return !textEmpty;
    }

    public Boolean isTitleNotEmpty() {
        return isNotEmpty(title);
    }

    public Boolean isButtonNotEmpty() {
        return !buttonEmpty;
    }

    public boolean isAlwaysDisplay() {
        return alwaysDisplay;
    }

    public boolean isGreyBorder() {
        return greyBorder;
    }

    public Boolean isPdfButtonNotEmpty() {
        return !pdfButtonEmpty;
    }

    public String getTitle() {
        return tokenizeInViewMode(title);
    }

    public String getTitleType() {
        return titleType;
    }
}
