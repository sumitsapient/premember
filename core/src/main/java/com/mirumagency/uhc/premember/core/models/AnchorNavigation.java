package com.mirumagency.uhc.premember.core.models;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Optional;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = AnchorNavigation.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AnchorNavigation extends Component {

    @Inject
    private Page currentPage;

    private Boolean isVisible = true;

    @PostConstruct
    protected void initModel() {
        Boolean hideAnchorNavigation = Optional.of(currentPage)
                .map(Page::getContentResource)
                .map(Resource::getValueMap)
                .map(map -> map.get("hideAnchorNavigation", String.class))
                .map(Boolean::parseBoolean)
                .orElse(false);

        isVisible = !hideAnchorNavigation;
    }

    public Boolean isVisible() {
        return isVisible;
    }
}