package com.mirumagency.uhc.premember.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = SiteExitModal.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = SiteExitModal.RESOURCE_TYPE)
public class SiteExitModal extends Component {

    public static final String RESOURCE_TYPE = "premember/components/content/siteExitModal";

    @ValueMapValue
    private String titleText;

    @ValueMapValue
    private String buttonText;

    @ValueMapValue
    private boolean includeCancelButton;

    public String getTitleText() {
        return titleText;
    }

    public String getButtonText() {
        return buttonText;
    }

    public boolean includeCancelButton() {
        return includeCancelButton;
    }
}
