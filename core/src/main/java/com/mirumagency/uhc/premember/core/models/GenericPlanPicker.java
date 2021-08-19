package com.mirumagency.uhc.premember.core.models;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

@Model(adaptables = SlingHttpServletRequest.class,
		adapters = GenericPlanPicker.class,
		defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class GenericPlanPicker extends WithVisibilityOptions {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@ScriptVariable
	private Resource resource;

	@ValueMapValue
	private Boolean imageEmpty = true;

	@ValueMapValue
	private Boolean showButton = true;

	@ValueMapValue
	private Boolean hideImage = true;

	@ValueMapValue
	private Boolean leftAlignImage = false;

	@ValueMapValue
	private Boolean leftAlignImageMaintainedOnMobile = false;

	@ValueMapValue
	private String imageAlignment;

	@PostConstruct
    protected void initModel() {
        Resource imageResource = resource.getChild("image");

        if (imageResource != null) {
            ValueMap imageProperties = imageResource.getValueMap();
            String fileReference = imageProperties.get("fileReference", String.class);
            if (StringUtils.isNotBlank(fileReference)) {
                imageEmpty = false;
            }
        }
    }

		public Boolean showImage() {
			return !hideImage;
		}

    public Boolean isImageNotEmpty() {
        return !imageEmpty;
    }

		public Boolean isShowButton() {
			return showButton;
		}

		public Boolean isImageLeftAligned() {
			return leftAlignImage;
		}

		public Boolean isImageLeftAlignMaintainedOnMobile() {
			return leftAlignImageMaintainedOnMobile;
		}

		public String getImageAlignment () {
			return imageAlignment;
		}
}