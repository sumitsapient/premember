package com.mirumagency.uhc.premember.core.models.visibility;

import lombok.Builder;
import org.apache.commons.lang.StringUtils;

@Builder
public class SuppressOnToken {

    private final String tokenValue;
    private final String specificTokenValue;

    public boolean isSuppressed() {
        if (specificTokenValueIsEmpty()) {
            return suppressOnTokenBooleanValue();
        }
        return specificTokenValue.equals(tokenValue);
    }

    private boolean suppressOnTokenBooleanValue() {
        if (tokenValue == null) {
            return false;
        }
        return Boolean.parseBoolean(tokenValue);
    }

    private boolean specificTokenValueIsEmpty() {
        return StringUtils.isBlank(specificTokenValue);
    }
}
