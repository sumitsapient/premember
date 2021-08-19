package com.mirumagency.uhc.premember.core.models.visibility;

import lombok.Builder;
import org.apache.commons.lang.StringUtils;

@Builder
public class ShowOnToken {

    private final String tokenValue;
    private final String specificTokenValue;

    public boolean isShown() {
        if (specificTokenValueIsEmpty()) {
            return showOnTokenBooleanValue();
        }
        return specificTokenValue.equals(tokenValue);
    }

    private boolean showOnTokenBooleanValue() {
        if (tokenValue == null) {
            return true;
        }
        return Boolean.parseBoolean(tokenValue);
    }

    private boolean specificTokenValueIsEmpty() {
        return StringUtils.isBlank(specificTokenValue);
    }
}
