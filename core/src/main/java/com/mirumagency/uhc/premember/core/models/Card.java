package com.mirumagency.uhc.premember.core.models;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

import static org.apache.commons.lang.StringUtils.isNotEmpty;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = Card.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Card extends WithVisibilityOptions {

    @ScriptVariable
    private Resource resource;

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String titleType;

    @ValueMapValue
		private String titleStyle;

    @ValueMapValue
    private String subtitle;

    @ValueMapValue
    private String backgroundColor;

    @ValueMapValue
		private String imageAlignment;

    @ValueMapValue
    private Boolean hideImage = true;

    @ValueMapValue
    private Boolean hideIcon = true;

    @ValueMapValue
    private Boolean leftAlignImage = false;

    @ValueMapValue
    private Boolean leftAlignImageMaintainedOnMobile = false;

    @ValueMapValue
    private Boolean clearPadding = false;

    @ValueMapValue
    private Boolean promotionsCard = false;

		@ValueMapValue
		private Boolean showBorder = false;

    @ValueMapValue
    private Boolean centerContent = false;

    private Boolean imageEmpty = true;

    private Boolean iconEmpty = true;

    private Boolean bodyEmpty = true;

    private Boolean footerEmpty = true;

    @ValueMapValue
    private Boolean backgroundTransparent = false;

    @PostConstruct
    protected void initModel() {
        Resource imageResource = resource.getChild("image");
        Resource iconResource = resource.getChild("icon");
        Resource bodyParResource = resource.getChild("bodyPar");
        Resource footerParResource = resource.getChild("footerPar");

        if (imageResource != null) {
            ValueMap imageProperties = imageResource.getValueMap();
            String fileReference = imageProperties.get("fileReference", String.class);
            if (StringUtils.isNotBlank(fileReference)) {
                imageEmpty = false;
            }
        }

        if (iconResource != null) {
            ValueMap iconProperties = iconResource.getValueMap();
            String path = iconProperties.get("path", String.class);
            if (StringUtils.isNotBlank(path)) {
                iconEmpty = false;
            }
        }

        if (bodyParResource != null && bodyParResource.hasChildren()) {
            bodyEmpty = false;
        }

        if (footerParResource != null && footerParResource.hasChildren()) {
            footerEmpty = false;
        }
    }

    public Boolean showImage() {
        return !hideImage;
    }

    public Boolean showIcon() {
        return !hideIcon;
    }

    public Boolean showHeader() {
        return (isIconNotEmpty() && showIcon()) || isTitleNotEmpty() || isSubtitleNotEmpty();
    }

    public Boolean isImageNotEmpty() {
        return !imageEmpty;
    }

    public Boolean isIconNotEmpty() {
        return !iconEmpty;
    }

    public Boolean isTitleNotEmpty() {
        return isNotEmpty(title);
    }

    public Boolean isSubtitleNotEmpty() {
        return isNotEmpty(subtitle);
    }

    public Boolean isImageLeftAligned() {
        return leftAlignImage;
    }

    public Boolean isImageLeftAlignMaintainedOnMobile() {
        return leftAlignImageMaintainedOnMobile;
    }

    public Boolean isPaddingCleared() {
        return clearPadding;
    }

    public Boolean isPromotionsCard() {
        return promotionsCard;
    }

    public Boolean isBackgroundTransparent() {
        return backgroundTransparent;
    }

    public Boolean isBodyNotEmpty() {
        return !bodyEmpty;
    }

    public Boolean isFooterNotEmpty() {
        return !footerEmpty;
    }

		public Boolean isShowBorder() {
			return showBorder;
		}

    public Boolean isCenterContent() {
      return centerContent;
    }

    public String getTitle() {
        return tokenizeInViewMode(this.getEmployer().getUHCorOxford(title));
    }

    public String getTitleType() {
        return titleType;
    }

    public String getTitleStyle() {
        return titleStyle;
    }

    public String getSubtitle() {
        return tokenizeInViewMode(subtitle);
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public String getImageAlignment () {
    	return imageAlignment;
		}
}
