package com.mirumagency.uhc.premember.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.via.ResourceSuperType;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = com.adobe.cq.wcm.core.components.models.Text.class,
        resourceType = "premember/components/content/text")
public class Text extends TokensForEmployer implements com.adobe.cq.wcm.core.components.models.Text {

    @Self
    @Via(type = ResourceSuperType.class)
    private com.adobe.cq.wcm.core.components.models.Text text;

    @Override
    public String getText() {
        return tokenizeInViewMode(this.getEmployer().getUHCorOxford(text.getText()));
    }

    @Override
    public boolean isRichText() {
        return text.isRichText();
    }

    @Override
    public String getExportedType() {
        return text.getExportedType();
    }
}
