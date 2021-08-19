package com.mirumagency.uhc.premember.core.domain.federal;

import lombok.Getter;
import org.apache.commons.lang.StringUtils;

import java.util.Optional;

import static java.util.Arrays.stream;
@Getter
public enum PlanType {
    HEALTH("health"),
    VISION("vision"),
    DENTAL("dental"),
    MEDICARE("medicare"),
    NOT_SELECTED("not_selected");


    private final String typeName;

    PlanType(String typeName) {
        this.typeName = typeName;
    }

    public static Optional<PlanType> from(String typeName) {
        return stream(values())
                .filter(type -> StringUtils.equalsIgnoreCase(type.typeName, typeName))
                .findFirst();
    }
}
