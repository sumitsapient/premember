package com.mirumagency.uhc.premember.core.models;

import com.mirumagency.uhc.premember.core.models.visibility.ShowOnToken;
import com.mirumagency.uhc.premember.core.models.visibility.SuppressOnToken;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = WithVisibilityOptions.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class WithVisibilityOptions extends TokensForEmployer {

    @ValueMapValue
    String showOnToken;

    @ValueMapValue
    String suppressOnToken;

    @ValueMapValue
    String specificTokenValue;

    /**
     * Indicates whether this container and its content should be visible.
     */
    public boolean isVisible() {
        return isAuthoringMode() || isBlueprint() || (showOnToken() && !suppressOnToken());
    }

    private boolean suppressOnToken() {
        if (suppressOnToken == null) {
            return false;
        }
        return SuppressOnToken.builder()
                .tokenValue(resolveToken(suppressOnToken))
                .specificTokenValue(specificTokenValue)
                .build()
                .isSuppressed();
    }

    private boolean showOnToken() {
        if (showOnToken == null) {
            return true;
        }
        return ShowOnToken.builder()
                .tokenValue(resolveToken(showOnToken))
                .specificTokenValue(specificTokenValue)
                .build()
                .isShown();
    }
}
