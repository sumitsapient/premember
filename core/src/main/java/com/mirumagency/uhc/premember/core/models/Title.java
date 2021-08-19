package com.mirumagency.uhc.premember.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.via.ResourceSuperType;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = com.adobe.cq.wcm.core.components.models.Title.class,
        resourceType = "premember/components/content/title")
public class Title extends TokensForEmployer implements com.adobe.cq.wcm.core.components.models.Title {
    @Self
    @Via(type = ResourceSuperType.class)
    private com.adobe.cq.wcm.core.components.models.Title title;

    @Override
    public String getText() {
        return tokenizeInViewMode(this.getEmployer().getUHCorOxford(title.getText()));
    }

    @Override
    public String getType() {
        return title.getType();
    }

    @Override
    public String getLinkURL() {
        return tokenizeInViewMode(title.getLinkURL());
    }

    @Override
    public boolean isLinkDisabled() {
        return title.isLinkDisabled();
    }

    @Override
    public String getExportedType() {
        return title.getExportedType();
    }
}
