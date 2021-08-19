package com.mirumagency.uhc.premember.core.models;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.Optional;

import static org.apache.commons.lang.StringUtils.isNotEmpty;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = Hero.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Hero extends TokensForEmployer {

    private static final String IMAGE_CHILD_RESOURCE_NAME = "image";
    private static final String TEXT_CHILD_RESOURCE_NAME = "text";

    @ScriptVariable
    private Resource resource;

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String titleType;

    @ValueMapValue
    private String titleSize;

    @ValueMapValue
    private String textAlignment;

    @ValueMapValue
    private Boolean imageDisplayLeft = false;

    @ValueMapValue
    private Boolean imageAlignedBottom = false;

    @ValueMapValue
    private Boolean showButton = false;

    private Boolean textEmpty = true;

    private Boolean imageEmpty = true;

    @PostConstruct
    protected void initModel() {
        title = tokenizeInViewMode(title);
        textEmpty = isTextEmpty();
        resolveImageResource();
    }

    private boolean isTextEmpty() {
        return Optional.ofNullable(resource.getChild(TEXT_CHILD_RESOURCE_NAME))
                .map(Resource::getValueMap)
                .map(textProperties -> textProperties.get("text", String.class))
                .map(StringUtils::isBlank)
                .orElse(true);
    }

    private void resolveImageResource() {
        imageEmpty = !Optional.ofNullable(resource.getChild(IMAGE_CHILD_RESOURCE_NAME))
                .map(Resource::getValueMap)
                .map(imageProperties -> imageProperties.get("fileReference", String.class))
                .filter(StringUtils::isNotBlank)
                .isPresent();
    }

    public Boolean isTextNotEmpty() {
        return !textEmpty;
    }

    public Boolean isImageNotEmpty() {
        return !imageEmpty;
    }

    public Boolean isTitleNotEmpty() {
        return isNotEmpty(title);
    }

    public Boolean isImageAlignedBottom() {
        return imageAlignedBottom;
    }

    public Boolean isShowButton() {
      return showButton;
    }

    public Boolean isImageDisplayLeft() {
      return imageDisplayLeft;
    }

    public String getTitle() {
        return title;
    }

    public String getTitleType() {
        return titleType;
    }

    public String getTitleSize() {
        return titleSize;
    }

    public String getTextAlignment() {
        return textAlignment;
    }
}
