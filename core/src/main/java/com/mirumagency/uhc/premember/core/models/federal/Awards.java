package com.mirumagency.uhc.premember.core.models.federal;

import lombok.Getter;
import lombok.Setter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.List;

@Model(adaptables = {SlingHttpServletRequest.class, Resource.class},
        adapters = Awards.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = Awards.RESOURCE_TYPE)
public class Awards {

    protected final static String RESOURCE_TYPE = "premember/components/federal/content/awards";

    @ChildResource
    @Getter
    private List<Award> awards;

    public boolean isEmpty() {
        return awards == null || awards.isEmpty();
    }

    @Getter
    @Setter
    @Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
    public static class Award {
        @ValueMapValue
        private String imagePath;
        @ValueMapValue
        private String title;
        @ValueMapValue
        private String description;
    }
}
