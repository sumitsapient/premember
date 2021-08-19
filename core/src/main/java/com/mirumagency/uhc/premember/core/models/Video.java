package com.mirumagency.uhc.premember.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = Video.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Video extends TokensForEmployer {

    @ScriptVariable
    private Resource resource;

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String description;

    @ValueMapValue
    private String transcript;

    @ValueMapValue
    private String transcriptLinkText;

    @ValueMapValue
    private String accessibility;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTranscript() {
        return transcript;
    }

    public String getAccessibility() {
        return accessibility;
    }

    public String getTranscriptLinkText() {
        return transcriptLinkText;
    }
}
